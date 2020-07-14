# azure subscription id

# Terraform backend
terraform {
  backend "azurerm" {
    resource_group_name  = "VAR_KUBE_RG"
    storage_account_name = "VAR_TERRAFORM_NAME"
    container_name       = "tfstate"
    key                  = "terraform.tfstate"
  }
}

variable "subscription_id" {
    default = "VAR_SUBSCRIPTION_ID"
}

# azure ad tenant id
variable "tenant_id" {
    default = "VAR_TENANT_ID"
}

# Object id of your ad account
variable "object_id" {
    default = "MYOBJECT_ID_PLACEHOLDER"
}

# default tags applied to all resources
variable "deployment_name" {
    default = "VAR_TEAM_NAME"
}

variable "location" {
    default = "WestEurope"
}

variable "func1containerimage" {
    default = "servicebusmessaging:1"
}