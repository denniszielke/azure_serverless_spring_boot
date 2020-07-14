# https://www.terraform.io/docs/providers/azurerm/r/application_insights.html
resource "azurerm_application_insights" "funcainsights" {
  name                = "${var.deployment_name}-ai"
  application_type    = "java"
  location            = azurerm_resource_group.funcrg.location
  resource_group_name = azurerm_resource_group.funcrg.name

  tags = {
    environment = var.deployment_name
  }
}