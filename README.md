# Demo project on serverless options for Java on Azure

## Scenario

This demo project is about demonstrating how to build event driven applications on Azure using:
- Azure API Management for managing APIs
- Azure Service Bus for routing messages
- Azure Functions for Hosting Serverless containers
- Azure Container Instances for hosting containers
- Azure KeyVault for hosting secret values
- Azure CosmosDB as NoSQL Database
- Azure Monitor + Application Insights for Telemetry and Logs

![](/images/overview.png)

### Microservice 1

Microservice 1 is a plain spring boot starter with the azure service bus sdk to subscribe to messages from `inputqueue` and route them to `inputtopic`.

![](/images/functionscontainer.png)

Link: https://docs.microsoft.com/de-de/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-service-bus

Sourcecode in the servicebusmessenger folder

### Microservice 2

Microservice 2 is using Azure Java Containers to using bindings to receive messages that have been published on the `inputtopic` and route them to `outputtopic`.

![](/images/functionbindings.png)

Link: https://docs.microsoft.com/de-de/azure/azure-functions/functions-create-function-linux-custom-image?tabs=bash%2Cportal&pivots=programming-language-java

Sourcecode in the azurefunctiondemo folder

### Microservice 3

Microservice 3 is using a Spring Boot app and a dapr container, which are running in an azure container instances. The Dapr sidecar is subscribing to the `outputtopic` and posts each messages via http to the Spring Boot app which does not have any azure sdk.

![](/images/acihosting.png)

Sourcecode in the springrestmessaging folder

## How to deploy the infrastructure using terraform
```
TERRAFORM_PATH=terraform # path to your terraform executable
DEPLOYMENT_NAME=apimfunc431 # dns compatible deployment name, 5-8 letters + 3 numbers

./deploy.sh $DEPLOYMENT_NAME $TERRAFORM_PATH
```

## Additional links
###  application insights
https://docs.microsoft.com/de-de/azure/developer/java/spring-framework/configure-spring-boot-java-applicationinsights#configure-springboot-application-to-send-log4j-logs-to-application-insights


### KeyVault references for Environment variables
https://docs.microsoft.com/en-us/azure/app-service/app-service-key-vault-references

### Enable Logs in container
https://docs.microsoft.com/en-us/azure/app-service/troubleshoot-diagnostic-logs#enable-application-logging-linuxcontainer

### Create SaS Token for authenticating to service bus via HTTP Header
https://docs.microsoft.com/en-us/rest/api/eventhub/generate-sas-token#using-the-shared-access-signature-at-http-level
https://docs.microsoft.com/en-us/rest/api/servicebus/send-message-to-queue


## Post test message to apim
```
apim_uri=https://${DEPLOYMENT_NAME}apim.azure-api.net/messages
curl -X POST $apim_uri -H "Name: dennis" -d ""
```
