#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Base URL
BASE_URL="http://localhost:8081/api"

# Function to print colored output
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
    fi
}

# Function to make API calls and check response
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local expected_status=$4
    local description=$5

    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d "$data")
    fi

    status_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    if [ "$status_code" -eq "$expected_status" ]; then
        print_result 0 "$description"
        echo "Response: $body"
    else
        print_result 1 "$description (Expected $expected_status, got $status_code)"
        echo "Response: $body"
    fi
}

echo "Testing API endpoints..."

# 1. Register a new user
echo -e "\n1. Registering new user..."
USER_DATA='{"username":"testuser","firstname":"Test","email":"test@example.com","password":"password123"}'
make_request "POST" "/auth/account" "$USER_DATA" 200 "Register new user"

# 2. Login to get JWT token
echo -e "\n2. Logging in..."
LOGIN_DATA='{"email":"test@example.com","password":"password123"}'
response=$(curl -s -X POST "$BASE_URL/auth/token" -H "Content-Type: application/json" -d "$LOGIN_DATA")
TOKEN=$(echo $response | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    print_result 1 "Login failed"
    exit 1
else
    print_result 0 "Login successful"
fi

# 3. Create a test product (as admin)
echo -e "\n3. Creating test product..."
PRODUCT_DATA='{"name":"Test Product","description":"A test product","price":99.99,"quantity":100,"code":"TEST001","inventoryStatus":"INSTOCK"}'
make_request "POST" "/products" "$PRODUCT_DATA" 201 "Create test product"

# 4. Test cart endpoints
echo -e "\n4. Testing cart endpoints..."

# Get cart
make_request "GET" "/cart" "" 200 "Get cart"

# Add item to cart
CART_ITEM_DATA='{"productId":1,"quantity":2}'
make_request "POST" "/cart/items" "$CART_ITEM_DATA" 200 "Add item to cart"

# Update cart item
UPDATE_CART_DATA='{"quantity":3}'
make_request "PUT" "/cart/items/1" "$UPDATE_CART_DATA" 200 "Update cart item"

# Get cart again
make_request "GET" "/cart" "" 200 "Get updated cart"

# 5. Test wishlist endpoints
echo -e "\n5. Testing wishlist endpoints..."

# Get wishlist
make_request "GET" "/wishlist" "" 200 "Get wishlist"

# Add item to wishlist
make_request "POST" "/wishlist/items/1" "" 200 "Add item to wishlist"

# Get wishlist again
make_request "GET" "/wishlist" "" 200 "Get updated wishlist"

# Remove item from wishlist
make_request "DELETE" "/wishlist/items/1" "" 200 "Remove item from wishlist"

echo -e "\nTesting completed!" 