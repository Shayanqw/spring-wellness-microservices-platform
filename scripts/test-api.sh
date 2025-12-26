#!/bin/bash

echo "=== Smoke test: API Gateway + Keycloak + Services ==="

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 25

# Get Keycloak token for staff user
echo "Getting staff token from Keycloak..."
KEYCLOAK_BASE_URL=${KEYCLOAK_BASE_URL:-http://localhost:8086}

STAFF_TOKEN=$(curl -s -X POST \
  "$KEYCLOAK_BASE_URL/realms/gbc-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=staff1&password=password&grant_type=password&client_id=api-gateway&client_secret=gateway-secret" \
  | jq -r '.access_token')

echo "Staff token acquired: ${#STAFF_TOKEN} chars"

# Get Keycloak token for student user
echo "Getting student token from Keycloak..."
STUDENT_TOKEN=$(curl -s -X POST \
  "$KEYCLOAK_BASE_URL/realms/gbc-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=student1&password=password&grant_type=password&client_id=api-gateway&client_secret=gateway-secret" \
  | jq -r '.access_token')

echo "Student token acquired: ${#STUDENT_TOKEN} chars"

# Test API Gateway routes
echo "Testing API Gateway routes..."

# Test wellness service through gateway
echo "1. Testing wellness service..."
curl -s -H "Authorization: Bearer $STAFF_TOKEN" \
  http://localhost:8082/api/resources | jq '.[0:2]'

# Test goal service through gateway
echo "2. Testing goal service..."
curl -s -H "Authorization: Bearer $STUDENT_TOKEN" \
  http://localhost:8082/api/goals | jq '.[0:2]'

# Test event service through gateway
echo "3. Testing event service..."
curl -s -H "Authorization: Bearer $STUDENT_TOKEN" \
  http://localhost:8082/api/events | jq '.[0:2]'

echo "=== Test Complete ==="