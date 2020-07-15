# Azure Functions Java with Bindings

https://docs.microsoft.com/de-de/azure/azure-functions/functions-create-function-linux-custom-image?tabs=bash%2Cportal&pivots=programming-language-java

Output bindings: https://docs.microsoft.com/en-us/azure/azure-functions/functions-bindings-service-bus-output?tabs=javascript

## Build
```
mvn clean package  
```

## Fetch environment config for local testing and run

```

func azure functionapp fetch-app-settings ${DEPLOYMENT_NAME}func2app

mvn azure-functions:run
```