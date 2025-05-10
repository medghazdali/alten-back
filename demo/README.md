# Product Management API

This is a Spring Boot application that implements a RESTful API for product management with user authentication and shopping features.

## Requirements

- Java 17 or higher
- Maven
- PostgreSQL
- Postman (for API testing)

## Setup Instructions

1. Clone the repository:
```bash
git clone http://github.com/medghazdali/alten-back
cd alten-back
```

2. Configure the database:
   - Create a PostgreSQL database
   - Update the `application.properties` file with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Build the application:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Authentication Endpoints

#### Create Account
- **POST** `/account`
- **Payload:**
```json
{
  "username": "string",
  "firstname": "string",
  "email": "string",
  "password": "string"
}
```

#### Login
- **POST** `/token`
- **Payload:**
```json
{
  "email": "string",
  "password": "string"
}
```
- Returns JWT token for authentication

### Product Management Endpoints

#### Products Collection
- **POST** `/products` - Create new product (Admin only)
- **GET** `/products` - List all products

#### Single Product
- **GET** `/products/{id}` - Get product details
- **PATCH** `/products/{id}` - Update product (Admin only)
- **DELETE** `/products/{id}` - Delete product (Admin only)

### Shopping Features

#### Shopping Cart
- **GET** `/cart` - View cart
- **POST** `/cart/items` - Add item to cart
- **DELETE** `/cart/items/{id}` - Remove item from cart
- **PUT** `/cart/items/{id}` - Update item quantity

#### Wishlist
- **GET** `/wishlist` - View wishlist
- **POST** `/wishlist/items` - Add item to wishlist
- **DELETE** `/wishlist/items/{id}` - Remove item from wishlist

## Product Model

```typescript
class Product {
  id: number;
  code: string;
  name: string;
  description: string;
  image: string;
  category: string;
  price: number;
  quantity: number;
  internalReference: string;
  shellId: number;
  inventoryStatus: "INSTOCK" | "LOWSTOCK" | "OUTOFSTOCK";
  rating: number;
  createdAt: number;
  updatedAt: number;
}
```

## Security

- JWT-based authentication
- Admin access (email: admin@admin.com) required for product management operations
- All endpoints except login and registration require authentication

## Testing

### Swagger Documentation
Access the Swagger UI at `http://localhost:8080/swagger-ui.html` for interactive API documentation.

## Features Implemented

- [x] Product CRUD operations
- [x] User authentication with JWT
- [x] Admin-only product management
- [x] Shopping cart functionality
- [x] Wishlist management
- [x] API documentation with Swagger
- [x] Postman test collection

## Notes

- The application uses JWT for authentication
- Only users with admin@admin.com email can manage products
- All endpoints (except /account and /token) require authentication
- The application supports Postgre SQL connection
