# https://www.terraform.io/docs/providers/azurerm/r/storage_account.html
resource "azurerm_storage_account" "function2_storage" {
  name                     = "${var.deployment_name}func2"
  resource_group_name      = azurerm_resource_group.funcrg.name
  location                 = azurerm_resource_group.funcrg.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  enable_https_traffic_only = true
}

resource "azurerm_app_service_plan" "function2_plan" {
  name                = "${var.deployment_name}func2plan"
  resource_group_name      = azurerm_resource_group.funcrg.name
  location                 = azurerm_resource_group.funcrg.location
  kind                =  "linux" # "Linux" #"FunctionApp"
  reserved = true

  sku {
    tier = "ElasticPremium"
    size = "EP2"
  }
}

# https://www.terraform.io/docs/providers/azurerm/r/function_app.html
resource "azurerm_function_app" "function2_app" {
  name                      = "${var.deployment_name}func2app"
  resource_group_name      = azurerm_resource_group.funcrg.name
  location                 = azurerm_resource_group.funcrg.location
  app_service_plan_id       = azurerm_app_service_plan.function2_plan.id
  storage_account_name       = azurerm_storage_account.function2_storage.name
  storage_account_access_key = azurerm_storage_account.function2_storage.primary_access_key

  os_type                   = "linux"
  version                    = "~3"

  identity {
    type = "SystemAssigned"
  }

  app_settings = {
    #FUNCTION_APP_EDIT_MODE                    = "readOnly"
    FUNCTIONS_EXTENSION_VERSION               = "~3"
    https_only                                = true
    DOCKER_REGISTRY_SERVER_URL                = "https://index.docker.io"  #"https://mcr.microsoft.com" # azurerm_container_registry.funcacr.login_server
    DOCKER_REGISTRY_SERVER_USERNAME           = "" #azurerm_container_registry.funcacr.admin_username
    DOCKER_REGISTRY_SERVER_PASSWORD           = "" #azurerm_container_registry.funcacr.admin_password
    SB_CONNECTIONSTRING                       = azurerm_servicebus_namespace.messagingbus.default_primary_connection_string
    SB_CONNECTIONSTRING_VAULT                 = "@Microsoft.KeyVault(VaultName=${azurerm_key_vault.funcvault.name};SecretName=servicebus-connectionstring;SecretVersion=${azurerm_key_vault_secret.servicebus_connectionstring.version})"
    APPINSIGHTS_INSTRUMENTATIONKEY            = azurerm_application_insights.funcainsights.instrumentation_key
    APPLICATIONINSIGHTS_CONNECTION_STRING     = "InstrumentationKey=${azurerm_application_insights.funcainsights.instrumentation_key}"
    WEBSITES_ENABLE_APP_SERVICE_STORAGE       = false
    WEBSITES_PORT                             = 8080
  }
    
  site_config {
    #pre_warmed_instance_count = 1
    #always_on         = true
    linux_fx_version  = "DOCKER|denniszielke/azurefunctiondemo"  #"DOCKER|mcr.microsoft.com/azure-functions/dotnet:2.0-appservice-quickstart"
    #linux_fx_version  = "DOCKER|${data.azurerm_container_registry.funcacr.login_server}/${var.func1containerimage}"
  }
}