package com.springazuremessaging.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication{

	

	// public static void main(String[] args) {
	// 	SpringApplication.run(DemoApplication.class, args);
	// }

    // @EventListener(ApplicationReadyEvent.class)
    // public void doSomethingAfterStartup() {

    //     String env = System.getenv(SB_CONNECTIONSTRING);
    //     if (env != null) {
    //         ConnectionString = env;
    //     }

    //     // Create a QueueClient instance for receiving using the connection string builder
    //     // We set the receive mode to "PeekLock", meaning the message is delivered
    //     // under a lock and must be acknowledged ("completed") to be removed from the queue
    //     QueueClient receiveClient = new QueueClient(new ConnectionStringBuilder(connectionString, "inputqueue"), ReceiveMode.PEEKLOCK);
    //     // We are using single thread executor as we are only processing one message at a time
    // 	ExecutorService executorService = Executors.newSingleThreadExecutor();
    //     this.registerReceiver(receiveClient, executorService);

    //     // wait for ENTER or 10 seconds elapsing
    //     waitForEnter(10000000);

    //     // shut down receiver to close the receive loop
    //     receiveClient.close();
    //     executorService.shutdown();
    // }

	// public void run(String connectionString) throws Exception {

    //     // Create a QueueClient instance for receiving using the connection string builder
    //     // We set the receive mode to "PeekLock", meaning the message is delivered
    //     // under a lock and must be acknowledged ("completed") to be removed from the queue
    //     QueueClient receiveClient = new QueueClient(new ConnectionStringBuilder(connectionString, "inputqueue"), ReceiveMode.PEEKLOCK);
    //     // We are using single thread executor as we are only processing one message at a time
    // 	ExecutorService executorService = Executors.newSingleThreadExecutor();
    //     this.registerReceiver(receiveClient, executorService);

    //     // wait for ENTER or 10 seconds elapsing
    //     waitForEnter(10000000);

    //     // shut down receiver to close the receive loop
    //     receiveClient.close();
    //     executorService.shutdown();
    // }

    // void registerReceiver(QueueClient queueClient, ExecutorService executorService) throws Exception {

    	
    //     // register the RegisterMessageHandler callback with executor service
    //     queueClient.registerMessageHandler(new IMessageHandler() {
    //                                            // callback invoked when the message handler loop has obtained a message
    //                                            public CompletableFuture<Void> onMessageAsync(IMessage message) {
    //                                                // receives message is passed to callback
    //                                                if (message.getContentType() != null && message.getContentType()=="application/json" ) {

    //                                                    byte[] body = message.getBody();
    //                                                    Map payload = GSON.fromJson(new String(body, UTF_8), Map.class);

    //                                                    System.out.printf(
    //                                                            "\n\t\t\t\tMessage received: \n\t\t\t\t\t\tSequenceNumber = %s, \n\t\t\t\t\t\tEnqueuedTimeUtc = %s," +
    //                                                                    "\n\t\t\t\t\t\tExpiresAtUtc = %s, \n\t\t\t\t\t\tContentType = \"%s\",  \n\t\t\t\t\t\tContent: [ name = %s ]\n",
    //                                                            message.getSequenceNumber(),
    //                                                            message.getEnqueuedTimeUtc(),
    //                                                            message.getExpiresAtUtc(),
    //                                                            message.getContentType(),
    //                                                            payload != null ? payload.get("name") : "");

	// 													try {
	// 														TopicClient sendClient = new TopicClient(new ConnectionStringBuilder(DemoApplication.ConnectionString, "outputtopic"));
															
	// 														sendClient.sendAsync(message).thenRunAsync(() -> {
	// 															System.out.printf("\tMessage sent acknowledged: Id = %s\n", message.getMessageId());
	// 															sendClient.closeAsync();
	// 														});
	// 											   		}catch(Exception e){
	// 														   System.out.printf("%s", e.toString());
	// 													   }
    //     												// sendMessagesAsync(sendClient).thenRunAsync(() -> sendClient.closeAsync());
    //                                                }
    //                                                return CompletableFuture.completedFuture(null);
    //                                            }

    //                                            // callback invoked when the message handler has an exception to report
    //                                            public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
    //                                                System.out.printf(exceptionPhase + "-" + throwable.getMessage());
    //                                            }
    //                                        },
    //             // 1 concurrent call, messages are auto-completed, auto-renew duration
    //             new MessageHandlerOptions(1, true, Duration.ofMinutes(1)),
    //             executorService);

    // }


    public static void main(String[] args) {
        ConnectionString = "Empty";
        String env = System.getenv(SB_CONNECTIONSTRING);
        if (env != null) {
            ConnectionString = env;
        }

		SpringApplication.run(DemoApplication.class, args);
      
        // System.exit(runApp(args, (connectionString) -> {
        //     DemoApplication app = new DemoApplication();
        //     try {
		// 		// app.run(PingApplication.class, args);
        //         app.run(connectionString);
        //         return 0;
        //     } catch (Exception e) {
        //         System.out.printf("%s", e.toString());
        //         return 1;
        //     }
        // }));
    }

    static final String SB_CONNECTIONSTRING = "SB_CONNECTIONSTRING";
	static String ConnectionString = null;

    // public static int runApp(String[] args, Function<String, Integer> run) {
    //     try {
    //         // get overrides from the environment
    //         String env = System.getenv(SB_CONNECTIONSTRING);
    //         if (env != null) {
    //             ConnectionString = env;
    //         }

    //         if (ConnectionString == null) {
    //             return 2;
    //         }
    //         return run.apply(ConnectionString);
    //     } catch (Exception e) {
    //         System.out.printf("%s", e.toString());
    //         return 3;
    //     }
    // }

    // private void waitForEnter(int seconds) {
    //     ExecutorService executor = Executors.newCachedThreadPool();
    //     try {
    //         executor.invokeAny(Arrays.asList(() -> {
    //             System.in.read();
    //             return 0;
    //         }, () -> {
    //             Thread.sleep(seconds * 1000);
    //             return 0;
    //         }));
    //     } catch (Exception e) {
    //         // absorb
    //     }
    // }

}
