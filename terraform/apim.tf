# https://www.terraform.io/docs/providers/azurerm/r/user_assigned_identity.html
resource "azurerm_user_assigned_identity" "apim_identity" {
  resource_group_name = azurerm_resource_group.funcrg.name
  location            = azurerm_resource_group.funcrg.location

  name = "${var.deployment_name}apimid"
}

resource "azurerm_role_assignment" "apimservicebus" {
  scope                = azurerm_servicebus_namespace.messagingbus.id
  role_definition_name = "Azure Service Bus Data Sender"
  principal_id         = azurerm_user_assigned_identity.apim_identity.principal_id
}

# https://www.terraform.io/docs/providers/azurerm/r/api_management.html
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
    type = "UserAssigned"
    identity_ids = [ azurerm_user_assigned_identity.apim_identity.id ]
  }
}

resource "azurerm_api_management_named_value" "ServiceBusSasToken" {
  name                = "ServiceBusSasToken"
  api_management_name = azurerm_api_management.apim.name
  resource_group_name = azurerm_resource_group.funcrg.name
  display_name        = "ServiceBusSasToken"
  value               = "Placeholder value"
}

resource "azurerm_api_management_named_value" "ServiceBusQueue" {
  name                = "ServiceBusQueue"
  api_management_name = azurerm_api_management.apim.name
  resource_group_name = azurerm_resource_group.funcrg.name
  display_name        = "ServiceBusQueue"
  value               = "https://${azurerm_servicebus_namespace.messagingbus.name}.servicebus.windows.net/${azurerm_servicebus_queue.inputqueue.name}/messages"
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
        <authentication-managed-identity resource="https://servicebus.azure.net" output-token-variable-name="msi-access-token" ignore-error="false" />
        <set-header name="Authorization" exists-action="override">
          <value>@((string)context.Variables["msi-access-token"])</value>
        </set-header>
        <set-header name="Content-type" exists-action="override">
            <value>application/json</value>
        </set-header>
        <set-header name="Ocp-Apim-Subscription-Key" exists-action="delete" />
        <set-backend-service base-url="{{ServiceBusQueue}}" />
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