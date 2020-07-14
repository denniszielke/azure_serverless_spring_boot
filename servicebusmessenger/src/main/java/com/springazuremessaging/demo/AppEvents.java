package com.springazuremessaging.demo;

import com.google.gson.reflect.TypeToken;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

import org.apache.commons.cli.*;

@Component
public class AppEvents {

    static final Gson GSON = new Gson();

    @EventListener(ApplicationReadyEvent.class)
    public void startApp() throws Exception  {

        System.out.println("hello world, I have just started up");
        System.out.println(DemoApplication.ConnectionString);

        if(DemoApplication.ConnectionString == null || DemoApplication.ConnectionString == "Empty"){
            Thread.sleep(5000);
            System.out.println("connection string empty - doing nothing");
        }
        else{
            // Create a QueueClient instance for receiving using the connection string builder
            // We set the receive mode to "PeekLock", meaning the message is delivered
            // under a lock and must be acknowledged ("completed") to be removed from the queue
            QueueClient receiveClient = new QueueClient(new ConnectionStringBuilder(DemoApplication.ConnectionString, "inputqueue"), ReceiveMode.PEEKLOCK);
            // We are using single thread executor as we are only processing one message at a time
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            this.registerReceiver(receiveClient, executorService);

            // wait for ENTER or 10 seconds elapsing
            waitForEnter(10000000);

            // shut down receiver to close the receive loop
            receiveClient.close();
            executorService.shutdown();
        }
    }

    void registerReceiver(QueueClient queueClient, ExecutorService executorService) throws Exception {

    	
        // register the RegisterMessageHandler callback with executor service
        queueClient.registerMessageHandler(new IMessageHandler() {
                                               // callback invoked when the message handler loop has obtained a message
                                               public CompletableFuture<Void> onMessageAsync(IMessage message) {
                                                   // receives message is passed to callback
                                                   if (message.getContentType() != null && message.getContentType()=="application/json" ) {

                                                       byte[] body = message.getBody();
                                                       Map payload = GSON.fromJson(new String(body, UTF_8), Map.class);

                                                       System.out.printf(
                                                               "\n\t\t\t\tMessage received: \n\t\t\t\t\t\tSequenceNumber = %s, \n\t\t\t\t\t\tEnqueuedTimeUtc = %s," +
                                                                       "\n\t\t\t\t\t\tExpiresAtUtc = %s, \n\t\t\t\t\t\tContentType = \"%s\",  \n\t\t\t\t\t\tContent: [ name = %s ]\n",
                                                               message.getSequenceNumber(),
                                                               message.getEnqueuedTimeUtc(),
                                                               message.getExpiresAtUtc(),
                                                               message.getContentType(),
                                                               payload != null ? payload.get("name") : "");

														try {
															TopicClient sendClient = new TopicClient(new ConnectionStringBuilder(DemoApplication.ConnectionString, "outputtopic"));
															
															sendClient.sendAsync(message).thenRunAsync(() -> {
																System.out.printf("\tMessage sent acknowledged: Id = %s\n", message.getMessageId());
																sendClient.closeAsync();
															});
												   		}catch(Exception e){
															   System.out.printf("%s", e.toString());
														   }
        												// sendMessagesAsync(sendClient).thenRunAsync(() -> sendClient.closeAsync());
                                                   }
                                                   return CompletableFuture.completedFuture(null);
                                               }

                                               // callback invoked when the message handler has an exception to report
                                               public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
                                                   System.out.printf(exceptionPhase + "-" + throwable.getMessage());
                                               }
                                           },
                // 1 concurrent call, messages are auto-completed, auto-renew duration
                new MessageHandlerOptions(1, true, Duration.ofMinutes(1)),
                executorService);

    }

     private void waitForEnter(int seconds) {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            executor.invokeAny(Arrays.asList(() -> {
                System.in.read();
                return 0;
            }, () -> {
                Thread.sleep(seconds * 1000);
                return 0;
            }));
        } catch (Exception e) {
            // absorb
        }
    }
}