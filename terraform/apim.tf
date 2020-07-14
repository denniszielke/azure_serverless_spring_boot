# https://www.terraform.io/docs/providers/azurerm/r/api_management_backend.html

resource "azurerm_api_management" "apim" {
  name                = "${var.deployment_name}apim"
  resource_group_name = azurerm_resource_group.funcrg.name
  location            = azurerm_resource_group.funcrg.location
  publisher_name      = "${var.deployment_name}Org"
  publisher_email     = "dummy@email.com"
  tags = {
    environment = var.deployment_name
  }

  sku_name            = "Developer_1"# #"Consumption_1" https://github.com/terraform-providers/terraform-provider-azurerm/issues/6730

  identity {
    type = "SystemAssigned"
  }
}

resource "azurerm_api_management_named_value" "ServiceBusSasToken" {
  name                = "ServiceBusSasToken"
  api_management_name = azurerm_api_management.apim.name
  resource_group_name = azurerm_resource_group.funcrg.name
  display_name        = "ServiceBusSasToken"
  value               = "Placeholder value"
}

# https://www.terraform.io/docs/providers/azurerm/r/api_management_api.html
resource "azurerm_api_management_api" "messages" {
  name                  = "messages"
  api_management_name = azurerm_api_management.apim.name
  resource_group_name   = azurerm_resource_group.funcrg.name
  revision              = "1"
  display_name          = "messages"
  path                  = "messages"
  protocols             = ["https"]
  subscription_required = false
}

# https://www.terraform.io/docs/providers/azurerm/r/api_management_api_operation.html
resource "azurerm_api_management_api_operation" "new" {
  operation_id        = "new"
  api_name            = azurerm_api_management_api.messages.name
  api_management_name = azurerm_api_management.apim.name
  resource_group_name = azurerm_resource_group.funcrg.name
  display_name        = "New Message"
  method              = "POST"
  url_template        = "/"
  description         = "creates a new message"

  response {
    status_code = 200
  }
}

# https://www.terraform.io/docs/providers/azurerm/r/api_management_api_operation_policy.html
resource "azurerm_api_management_api_operation_policy" "newmessage" {
  api_name            = azurerm_api_management_api.messages.name
  api_management_name = azurerm_api_management.apim.name
  resource_group_name = azurerm_resource_group.funcrg.name
  operation_id        = azurerm_api_management_api_operation.new.operation_id

  xml_content = <<XML
<policies>
    <inbound>
        <base />
        <set-header name="Authorization" exists-action="override">
            <value>{{ServiceBusSasToken}}</value>
        </set-header>
        <set-header name="Content-type" exists-action="override">
            <value>application/json</value>
        </set-header>
        <set-header name="Ocp-Apim-Subscription-Key" exists-action="delete" />
        <set-backend-service base-url="https://apim400sb.servicebus.windows.net/inputqueue/messages" />
        <set-body>@{
    string messagepayload = "{ name: 'someone' }"; 
    string[] value;
    if (context.Request.Headers.TryGetValue("Name", out value))
    {
        if(value != null && value.Length > 0 && !String.IsNullOrEmpty(value[0]))
        {
            messagepayload = "{ name: '" + value[0] +"' }"; 
        }
    }
    return messagepayload;
}</set-body>
    </inbound>
    <backend>
        <base />
    </backend>
    <outbound>
        <base />
    </outbound>
    <on-error>
        <base />
    </on-error>
</policies>
XML

}