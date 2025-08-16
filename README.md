# SmartCart - Full-Stack E-commerce Platform

A modern e-commerce platform built with Spring Boot + MongoDB backend and React.js + Tailwind CSS frontend, featuring role-based access control for users and administrators.

## Features

### User Portal
- Browse products with search and filtering
- Add items to cart
- Checkout and place orders
- Track order history
- Responsive design for all devices

### Admin Portal
- Product management (CRUD operations)
- Order management and status updates
- Stock monitoring and alerts
- Dashboard with analytics
- Role-based access control

### Authentication & Security
- JWT-based authentication
- Role-based authorization (USER/ADMIN)
- Secure API endpoints
- CORS configuration

## Tech Stack

### Backend
- **Spring Boot 3.x** - Java framework
- **Spring Security** - Authentication & authorization
- **MongoDB** - NoSQL database
- **JWT** - JSON Web Tokens
- **Maven** - Dependency management

### Frontend
- **React 18** - JavaScript library
- **TypeScript** - Type safety
- **Tailwind CSS** - Utility-first CSS framework
- **React Router** - Client-side routing
- **Axios** - HTTP client
- **Framer Motion** - Animation library

## Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- MongoDB 5.0 or higher
- Maven 3.6 or higher

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd project
```

### 2. Backend Setup

#### Start MongoDB
```bash
# Start MongoDB service
mongod

# In a new terminal, initialize the database
mongo < backend/init-mongo.js
```

#### Configure Backend
```bash
cd backend

# Update application.properties if needed
# Default configuration:
# - Port: 8080
# - MongoDB: localhost:27017/smartcart
# - JWT Secret: configured in application.properties
```

#### Run Backend
```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

#### Install Dependencies
```bash
cd ../
npm install
```

#### Configure Environment
Create `.env` file in the root directory:
```env
VITE_API_BASE_URL=http://localhost:8080/api
```

#### Run Frontend
```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

## Default Credentials

### Admin Account
- **Email:** admin@smartcart.com
- **Password:** admin123
- **Role:** ADMIN

### User Registration
- Users can register new accounts at `/signup`
- New users automatically get `USER` role

## API Endpoints

### Public Endpoints
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/search` - Search products
- `GET /api/products/category/{category}` - Get products by category
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### User Endpoints (Requires USER role)
- `GET /api/cart` - Get user cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/update` - Update cart item quantity
- `DELETE /api/cart/remove/{productId}` - Remove item from cart
- `POST /api/order/place` - Place order
- `GET /api/order/history` - Get user order history

### Admin Endpoints (Requires ADMIN role)
- `POST /api/products/admin` - Create product
- `PUT /api/products/admin/{id}` - Update product
- `DELETE /api/products/admin/{id}` - Delete product
- `GET /api/order/admin/all` - Get all orders
- `PUT /api/order/admin/{id}/status` - Update order status
- `GET /api/products/admin/stats` - Get product statistics
- `GET /api/order/admin/stats` - Get order statistics

## Testing

### 1. Test Admin Access
1. Login with admin credentials
2. Navigate to `/admin/dashboard`
3. Verify access to product management, order management, and stock alerts

### 2. Test User Access
1. Register a new user account
2. Login with user credentials
3. Browse products, add to cart, and place orders
4. Verify access is restricted to user-only features

### 3. Test API Integration
1. Use Postman or similar tool to test backend APIs
2. Verify JWT authentication works correctly
3. Test role-based access control

### 4. Test Frontend-Backend Integration
1. Ensure all data is fetched from backend APIs
2. Verify no dummy/static data remains
3. Test error handling and loading states

## Project Structure

```
project/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/     # Java source code
│   ├── src/main/resources/ # Configuration files
│   ├── Dockerfile         # Docker configuration
│   └── init-mongo.js      # Database initialization
├── src/                    # React frontend
│   ├── components/        # Reusable UI components
│   ├── pages/            # Page components
│   ├── context/          # React context providers
│   ├── services/         # API services
│   └── types/            # TypeScript type definitions
├── package.json           # Frontend dependencies
└── README.md             # This file
```

## Development

### Backend Development
- The backend uses Spring Boot with MongoDB
- JWT tokens are used for authentication
- Role-based access control is implemented using `@PreAuthorize`
- CORS is configured to allow frontend requests

### Frontend Development
- React components are built with TypeScript
- Tailwind CSS provides consistent styling
- Context API manages global state
- Protected routes ensure role-based access

### Database Schema
- **Users:** email, password, fullName, role, enabled
- **Products:** title, description, price, category, stock, rating, imageUrl
- **Carts:** userId, items[], total
- **Orders:** userId, items[], total, status, shippingAddress, paymentInfo

## Deployment

### Backend Deployment
1. Build the JAR file: `./mvnw clean package`
2. Run: `java -jar target/backend-0.0.1-SNAPSHOT.jar`
3. Configure environment variables for production

### Frontend Deployment
1. Build: `npm run build`
2. Deploy the `dist` folder to your hosting service
3. Update API base URL for production

## Troubleshooting

### Common Issues

1. **MongoDB Connection Error**
   - Ensure MongoDB is running
   - Check connection string in `application.properties`

2. **CORS Issues**
   - Verify CORS configuration in `SecurityConfig.java`
   - Check allowed origins in `application.properties`

3. **JWT Token Issues**
   - Ensure JWT secret is properly configured
   - Check token expiration settings

4. **Frontend API Errors**
   - Verify backend is running on correct port
   - Check API base URL configuration
   - Ensure JWT token is properly stored

### Logs
- Backend logs are available in the console
- Frontend errors are logged to browser console
- Check network tab for API request/response details

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please open an issue in the repository.
