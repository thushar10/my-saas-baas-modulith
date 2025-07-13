#!/bin/sh

# Exit immediately if a command exits with a non-zero status.
set -e
set -x # <--- ADD THIS LINE FOR VERBOSE DEBUGGING

# --- Configuration Variables (passed from docker-compose) ---
SOLACE_HOST="http://solace:8080"
SEMP_USER="${SEMP_USER:-admin}"
SEMP_PASS="${SEMP_PASS:-admin}"
VPN_NAME="${VPN_NAME:-default}"
QUEUE_NAME="${QUEUE_NAME:-q/accounts/created}"
TOPIC_NAME="${TOPIC_NAME:-accounts.created}"

# --- URL Encode QUEUE_NAME for the path ---
# This function will URL-encode forward slashes in the QUEUE_NAME for the URL path
urlencode() {
    local string="$1"
    local length="${#string}"
    local url_encoded=""
    for i in $(seq 0 $((length - 1))); do
        local char="${string:$i:1}"
        case "$char" in
            [a-zA-Z0-9.~_-]) url_encoded+="$char" ;;
            *) printf -v hex '%02X' "'$char"
               url_encoded+="%$hex" ;;
        esac
    done # Corrected
    echo "$url_encoded"
}

ENCODED_QUEUE_NAME=$(urlencode "$QUEUE_NAME")

# --- Wait for Broker to be Ready ---
echo "Waiting for Solace SEMP API to be ready at ${SOLACE_HOST}..."
while ! curl -s -u ${SEMP_USER}:${SEMP_PASS} ${SOLACE_HOST}/SEMP/v2/config > /dev/null; do
    echo "Solace not ready yet, waiting 5 seconds..."
    sleep 5
done
echo "Solace SEMP API is ready."

# --- Create the Queue ---
echo "Creating queue: ${QUEUE_NAME}"
curl -X POST -u "${SEMP_USER}:${SEMP_PASS}" -H "Content-Type: application/json" -d "{
  \"queueName\": \"${QUEUE_NAME}\",
  \"accessType\": \"non-exclusive\",
  \"permission\": \"delete\",
  \"ingressEnabled\": true,
  \"egressEnabled\": true
}" "${SOLACE_HOST}/SEMP/v2/config/msgVpns/${VPN_NAME}/queues"

# --- Create the Topic Subscription for the Queue ---
echo "Subscribing queue '${QUEUE_NAME}' to topic '${TOPIC_NAME}'"
# Use ENCODED_QUEUE_NAME for the URL path here
curl -X POST -u "${SEMP_USER}:${SEMP_PASS}" -H "Content-Type: application/json" -d "{
  \"subscriptionTopic\": \"${TOPIC_NAME}\"
}" "${SOLACE_HOST}/SEMP/v2/config/msgVpns/${VPN_NAME}/queues/${ENCODED_QUEUE_NAME}/subscriptions"

echo "Solace configuration complete. Configurator is exiting."