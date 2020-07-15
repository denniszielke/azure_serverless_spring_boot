
# Running dapr

## Set up dapr component file with service bus connection string

```
az servicebus namespace authorization-rule keys list --name RootManageSharedAccessKey --namespace-name ${DEPLOYMENT_NAME}sb --resource-group $DEPLOYMENT_NAME --query "primaryConnectionString" | tr -d '"'
SB_CONNECTIONSTRING=$(az servicebus namespace authorization-rule keys list --name RootManageSharedAccessKey --namespace-name ${DEPLOYMENT_NAME}sb --resource-group $DEPLOYMENT_NAME --query "primaryConnectionString" | tr -d '"')

cat <<EOF > ./components/servicebus.yaml
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: pubsub-azure-service-bus
  namespace: default
spec:
  metadata:
  - name: connectionString
    value: '$SB_CONNECTIONSTRING'
  - name: timeoutInSec
    value: 80
  - name: maxDeliveryCount
    value: 15
  - name: lockDurationInSec
    value: 5
  - name: defaultMessageTimeToLiveInSec
    value: 2
  type: pubsub.azure.servicebus
EOF
```

## Run dapr locally 
```
dapr run --components-path ./components --app-id subscriber --app-port 8080 --port 3005 -- mvn spring-boot:run
```
## Run dapr app in server mode
 * mvn clean install
 * 2. Run in server mode:
 * dapr run --app-id invokedemo --app-port 3000 --port 3005 \
 *   -- java -jar examples/target/dapr-java-sdk-examples-exec.jar \
 *   io.dapr.examples.invoke.http.DemoService -p 3000