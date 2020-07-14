
# https://www.terraform.io/docs/providers/azurerm/r/container_registry.html

resource "azurerm_container_registry" "funcacr" {
  name                     = "${var.deployment_name}acr"
  resource_group_name      = azurerm_resource_group.funcrg.name
  location                 = azurerm_resource_group.funcrg.location
  sku                      = "Premium"
  admin_enabled            = true

  tags = {
    environment = var.deployment_name
  }
}