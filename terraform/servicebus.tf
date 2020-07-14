# https://www.terraform.io/docs/providers/azurerm/r/servicebus_namespace.html
resource "azurerm_servicebus_namespace" "messagingbus" {
  name                = "${var.deployment_name}sb"
  location            = azurerm_resource_group.funcrg.location
  resource_group_name = azurerm_resource_group.funcrg.name
  sku                 = "Standard"

  tags = {
    environment = var.deployment_name
  }
}

resource "azurerm_servicebus_queue" "inputqueue" {
  name                = "inputqueue"
  resource_group_name = azurerm_resource_group.funcrg.name
  namespace_name      = azurerm_servicebus_namespace.messagingbus.name

  enable_partitioning = true
}

resource "azurerm_servicebus_topic" "outputtopic" {
  name                = "outputtopic"
  resource_group_name = azurerm_resource_group.funcrg.name
  namespace_name      = azurerm_servicebus_namespace.messagingbus.name

  enable_partitioning = true
}

# https://www.terraform.io/docs/providers/azurerm/r/servicebus_subscription.html
resource "azurerm_servicebus_subscription" "output" {
  name                = "output"
  resource_group_name = azurerm_resource_group.funcrg.name
  namespace_name      = azurerm_servicebus_namespace.messagingbus.name
  topic_name          = azurerm_servicebus_topic.outputtopic.name
  max_delivery_count  = 1
}