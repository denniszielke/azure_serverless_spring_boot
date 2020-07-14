# # https://www.terraform.io/docs/providers/azurerm/r/api_management_backend.html

# resource "azurerm_api_management" "apim" {
#   name                = "${var.deployment_name}apim"
#   resource_group_name = azurerm_resource_group.funcrg.name
#   location            = azurerm_resource_group.funcrg.location
#   publisher_name      = "${var.deployment_name}Org"
#   publisher_email     = "dummy@email.com"
#   tags = {
#     environment = var.deployment_name
#   }

#   sku_name            = "Developer_1"# #"Consumption_1" https://github.com/terraform-providers/terraform-provider-azurerm/issues/6730

#   identity {
#     type = "SystemAssigned"
#   }
# }