
# If it is not possible to create a Sas Token for Service Bus via Terraform

Here is how you can create one after the deployment went through.
Afterwards you need to update the named value for ServiceBusSasToken in the api management instance.

```
servicebus_uri=${DEPLOYMENT_NAME}sb.servicebus.windows.net/inputqueue
shared_access_key_name=sendonly

az servicebus queue authorization-rule create --name $shared_access_key_name --namespace-name ${DEPLOYMENT_NAME}sb --queue-name inputqueue --resource-group $DEPLOYMENT_NAME --rights Send

shared_access_key=$(az servicebus queue authorization-rule keys list --resource-group $DEPLOYMENT_NAME --namespace-name ${DEPLOYMENT_NAME}sb --queue-name inputqueue --name $shared_access_key_name -o tsv --query primaryKey)

EXPIRY=${EXPIRY:=$((60 * 60 * 24))}
ENCODED_URI=$(echo -n $servicebus_uri | jq -s -R -r @uri)
TTL=$(($(date +%s) + $EXPIRY))
UTF8_SIGNATURE=$(printf "%s\n%s" $ENCODED_URI $TTL | iconv -t utf8)

HASH=$(echo -n "$UTF8_SIGNATURE" | openssl sha256 -hmac $shared_access_key -binary | base64)

ENCODED_HASH=$(echo -n $HASH | jq -s -R -r @uri)

echo "put the following value into the named value for ServiceBusSasToken in your azure api management:"
echo -n "SharedAccessSignature sr=$ENCODED_URI&sig=$ENCODED_HASH&se=$TTL&skn=$shared_access_key_name"

curl -X POST https://$servicebus_uri/messages -H "Authorization: SharedAccessSignature sr=$ENCODED_URI&sig=$ENCODED_HASH&se=$TTL&skn=$shared_access_key_name" -H "Content-type: application/json" -H "Host: ${DEPLOYMENT_NAME}sb.servicebus.windows.net" --data "{\"name\":\"peter\"}" 

```