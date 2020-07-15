# https://www.terraform.io/docs/providers/azurerm/r/container_group.html
resource "azurerm_container_group" "function3" {
  name                = "function3"
  resource_group_name = azurerm_resource_group.funcrg.name
  location            = azurerm_resource_group.funcrg.location
  ip_address_type     = "public"
  dns_name_label      = "${var.deployment_name}aci"
  os_type             = "Linux"

  identity {
      type = "SystemAssigned"
  }

  container {
    name   = "springrestmessaging"
    image  = "denniszielke/springrestmessaging"
    cpu    = "1"
    memory = "3"

    ports {
      port     = 8080
      protocol = "TCP"
    }

    environment_variables = {
      "SB_CONNECTIONSTRING"  = "offen"
    }

    secure_environment_variables = {
      "SB_CONNECTIONSTRING_SECURE"  = "geheim"
    }
  }

  container {
    name   = "sidecar"
    image  = "microsoft/aci-tutorial-sidecar"
    cpu    = "0.5"
    memory = "1.5"
  }

  tags = {
    environment = var.deployment_name
  }
}