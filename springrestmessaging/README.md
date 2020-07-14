 * mvn clean install
 * 2. Run in server mode:
 * dapr run --app-id invokedemo --app-port 3000 --port 3005 \
 *   -- java -jar examples/target/dapr-java-sdk-examples-exec.jar \
 *   io.dapr.examples.invoke.http.DemoService -p 3000

https://github.com/dapr/java-sdk/tree/master/examples/src/main/java/io/dapr/examples/pubsub/http

 https://github.com/dapr/java-sdk/blob/master/examples/src/main/java/io/dapr/examples/invoke/http/DemoService.java


     DaprClient client = (new DaprClientBuilder()).build();
    for (String message : args) {
      byte[] response = client.invokeService(
          Verb.POST, SERVICE_APP_ID, "say", message, null, byte[].class).block();
      System.out.println(new String(response));
    }