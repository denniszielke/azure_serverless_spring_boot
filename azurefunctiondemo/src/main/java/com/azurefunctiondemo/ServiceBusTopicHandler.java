package com.azurefunctiondemo;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class ServiceBusTopicHandler {
    /**
     */
    @FunctionName("ServiceBusTopicHandler")
    @ServiceBusTopicOutput(name = "outputMessage", topicName = "outputtopic", subscriptionName = "output", connection = "SB_CONNECTIONSTRING")
    public void run(
        @ServiceBusTopicTrigger(
            name = "inputMessage",
            topicName = "inputtopic",
            subscriptionName = "input",
            connection = "SB_CONNECTIONSTRING"
        ) String inputMessage
        , @ServiceBusTopicOutput(
            name = "outputMessage", 
            topicName = "outputtopic", 
            subscriptionName = "output", 
            connection = "SB_CONNECTIONSTRING") OutputBinding<String> outputMessage,
        final ExecutionContext context
    ) {
        context.getLogger().info("Topic Msg Received " + inputMessage.toString());
        context.getLogger().info(inputMessage);

        outputMessage.setValue("thank to for sending" + inputMessage.toString());
    }
}