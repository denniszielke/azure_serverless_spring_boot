# https://www.terraform.io/docs/providers/azurerm/r/key_vault.html
resource "azurerm_key_vault" "funcvault" {
  name                        = "${var.deployment_name}-vault"
  location                    = azurerm_resource_group.funcrg.location
  resource_group_name         = azurerm_resource_group.funcrg.name
  enabled_for_disk_encryption = false
  tenant_id                   = var.tenant_id

  sku_name = "standard"

  tags = {
    environment = var.deployment_name
  }
}

# https://www.terraform.io/docs/providers/azurerm/r/key_vault_access_policy.html
resource "azurerm_key_vault_access_policy" "aksvault_policy_forme" {
  key_vault_id = azurerm_key_vault.funcvault.id

  tenant_id = var.tenant_id
  object_id = var.object_id

  secret_permissions = [
      "get",
      "list",
      "set"
  ]
}

# https://www.terraform.io/docs/providers/azurerm/r/key_vault_secret.html
resource "azurerm_key_vault_secret" "appinsights_secret" {
  name         = "appinsights-key"
  value        = azurerm_application_insights.funcainsights.instrumentation_key
  key_vault_id = azurerm_key_vault.funcvault.id
  
  tags = {
    environment = var.deployment_name
  }
}

# https://www.terraform.io/docs/providers/azurerm/r/key_vault_secret.html
resource "azurerm_key_vault_secret" "servicebus_connectionstring" {
  name         = "servicebus-connectionstring"
  value        = azurerm_servicebus_namespace.messagingbus.default_primary_connection_string
  key_vault_id = azurerm_key_vault.funcvault.id
  
  tags = {
    environment = var.deployment_name
  }
}
