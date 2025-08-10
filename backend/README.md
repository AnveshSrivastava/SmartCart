# SmartCart Backend API

A comprehensive Spring Boot backend for the SmartCart e-commerce platform with JWT authentication, MongoDB integration, and RESTful APIs.

## 🚀 Features

- **JWT Authentication & Authorization** - Secure user authentication with role-based access
- **MongoDB Integration** - NoSQL database for scalable data storage
- **RESTful APIs** - Complete CRUD operations for products, cart, and orders
- **Spring Security** - Comprehensive security with CORS support
- **Lombok** - Reduced boilerplate code
- **Global Exception Handling** - Centralized error handling
- **Validation** - Input validation with proper error messages
- **Logging** - Comprehensive logging for debugging and monitoring

## 🛠 Tech Stack

- **Spring Boot 3.3.0** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data MongoDB** - Database integration
- **JWT (jjwt 0.12.3)** - Stateless authentication
- **Lombok** - Code generation
- **MongoDB** - NoSQL database
- **Java 20** - Programming language

## 📁 Project Structure

```
com.smartcart.backend/
├── config/
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
├── controller/
│   ├── AuthController.java
│   ├── ProductController.java
│   ├── CartController.java
│   └── OrderController.java
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   └── ProductDTO.java
├── exception/
│   └── GlobalExceptionHandler.java
├── model/
│   ├── User.java
│   ├── Product.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Order.java
│   └── OrderItem.java
├── repository/
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   ├── CartRepository.java
│   └── OrderRepository.java
├── service/
│   ├── AuthService.java
│   ├── ProductService.java
│   ├── CartService.java
│   └── OrderService.java
├── util/
│   └── JwtUtil.java
└── BackendApplication.java
```

## 🚀 Quick Start

### Prerequisites

- Java 20 or higher
- Maven 3.6+
- MongoDB 4.4+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd SmartCart/backend
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure MongoDB**
   - Start MongoDB service
   - Create database `smartcart`
   - Update `application.properties` if needed

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 🔧 Configuration

### Application Properties

Key configurations in `application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/smartcart

# JWT Configuration
jwt.secret=your-secret-key
jwt.expiration=86400000
jwt.refresh-expiration=604800000

# CORS Configuration
cors.allowed-origins=http://localhost:3000,http://localhost:5173
```

### Environment Variables

For production, set these environment variables:

```bash
export MONGODB_URI=mongodb://your-mongodb-uri
export JWT_SECRET=your-production-secret-key
export JWT_EXPIRATION=86400000
```

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

#### Refresh Token
```http
POST /api/auth/refresh
Authorization: Bearer <refresh-token>
```

### Product Endpoints

#### Get All Products
```http
GET /api/products
GET /api/products?page=0&size=10
```

#### Get Product by ID
```http
GET /api/products/{id}
```

#### Search Products
```http
GET /api/products/search?q=keyword
```

#### Get Products by Category
```http
GET /api/products/category/{category}
```

#### Admin: Create Product
```http
POST /api/products/admin
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "title": "Product Name",
  "description": "Product description",
  "price": 99.99,
  "category": "Electronics",
  "stock": 100,
  "imageUrl": "https://example.com/image.jpg"
}
```

### Cart Endpoints

#### Get User Cart
```http
GET /api/cart
Authorization: Bearer <user-token>
```

#### Add to Cart
```http
POST /api/cart/add?productId={id}&quantity=1
Authorization: Bearer <user-token>
```

#### Update Cart Item
```http
PUT /api/cart/update?productId={id}&quantity=2
Authorization: Bearer <user-token>
```

#### Remove from Cart
```http
DELETE /api/cart/remove/{productId}
Authorization: Bearer <user-token>
```

### Order Endpoints

#### Place Order
```http
POST /api/order/place?shippingAddress=123 Main St&paymentInfo=card
Authorization: Bearer <user-token>
```

#### Get Order History
```http
GET /api/order/history
Authorization: Bearer <user-token>
```

#### Get Order by ID
```http
GET /api/order/{orderId}
Authorization: Bearer <user-token>
```

## 🔐 Security

### JWT Token Format
```
Authorization: Bearer <jwt-token>
```

### Role-Based Access

- **USER**: Can access cart and order endpoints
- **ADMIN**: Can access all endpoints including product management

### Protected Endpoints

- `/api/cart/**` - Requires USER role
- `/api/order/**` - Requires USER role  
- `/api/products/admin/**` - Requires ADMIN role
- `/api/admin/**` - Requires ADMIN role

## 🗄 Database Schema

### User Collection
```json
{
  "_id": "ObjectId",
  "fullName": "String",
  "email": "String (unique)",
  "password": "String (hashed)",
  "role": "USER|ADMIN",
  "enabled": "Boolean",
  "createdAt": "DateTime",
  "updatedAt": "DateTime"
}
```

### Product Collection
```json
{
  "_id": "ObjectId",
  "title": "String",
  "description": "String",
  "price": "BigDecimal",
  "category": "String",
  "rating": "Double",
  "reviewCount": "Integer",
  "stock": "Integer",
  "imageUrl": "String",
  "active": "Boolean",
  "createdAt": "DateTime",
  "updatedAt": "DateTime"
}
```

### Cart Collection
```json
{
  "_id": "ObjectId",
  "userId": "String",
  "items": [
    {
      "productId": "String",
      "quantity": "Integer",
      "productTitle": "String",
      "productImageUrl": "String",
      "productPrice": "Double"
    }
  ],
  "total": "BigDecimal",
  "createdAt": "DateTime",
  "updatedAt": "DateTime"
}
```

### Order Collection
```json
{
  "_id": "ObjectId",
  "userId": "String",
  "items": [
    {
      "productId": "String",
      "productTitle": "String",
      "productImageUrl": "String",
      "quantity": "Integer",
      "price": "BigDecimal",
      "total": "BigDecimal"
    }
  ],
  "total": "BigDecimal",
  "shippingAddress": "String",
  "status": "PENDING|CONFIRMED|SHIPPED|DELIVERED|CANCELLED",
  "paymentInfo": "String",
  "trackingNumber": "String",
  "createdAt": "DateTime",
  "updatedAt": "DateTime"
}
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### API Testing with curl

#### Register a user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### Get products
```bash
curl -X GET http://localhost:8080/api/products
```

## 🚀 Deployment

### Docker Deployment

1. **Build Docker image**
   ```bash
   docker build -t smartcart-backend .
   ```

2. **Run container**
   ```bash
   docker run -p 8080:8080 \
     -e MONGODB_URI=mongodb://host.docker.internal:27017/smartcart \
     -e JWT_SECRET=your-secret-key \
     smartcart-backend
   ```

### Production Considerations

- Use strong JWT secrets
- Enable HTTPS
- Configure proper CORS origins
- Set up MongoDB authentication
- Use environment variables for sensitive data
- Implement rate limiting
- Add monitoring and logging

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support, please open an issue in the repository or contact the development team. 