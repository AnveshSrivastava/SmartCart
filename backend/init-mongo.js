// Initialize SmartCart database
db = db.getSiblingDB('smartcart');

// Create collections
db.createCollection('users');
db.createCollection('products');
db.createCollection('carts');
db.createCollection('orders');

// Create indexes
db.users.createIndex({ "email": 1 }, { unique: true });
db.products.createIndex({ "title": "text", "description": "text" });
db.products.createIndex({ "category": 1 });
db.products.createIndex({ "active": 1 });
db.carts.createIndex({ "userId": 1 });
db.orders.createIndex({ "userId": 1 });
db.orders.createIndex({ "status": 1 });
db.orders.createIndex({ "createdAt": -1 });

// Insert sample admin user (password: admin123)
db.users.insertOne({
    fullName: "Admin User",
    email: "admin@smartcart.com",
    password: "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", // admin123
    role: "ADMIN",
    enabled: true,
    accountNonExpired: true,
    accountNonLocked: true,
    credentialsNonExpired: true,
    createdAt: new Date(),
    updatedAt: new Date()
});

// Insert sample products
db.products.insertMany([
    {
        title: "iPhone 15 Pro",
        description: "Latest iPhone with advanced camera system and A17 Pro chip",
        price: NumberDecimal("999.99"),
        category: "Electronics",
        rating: 4.8,
        reviewCount: 1250,
        stock: 50,
        imageUrl: "https://images.pexels.com/photos/1647976/pexels-photo-1647976.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Samsung Galaxy S24",
        description: "Premium Android smartphone with AI features",
        price: NumberDecimal("899.99"),
        category: "Electronics",
        rating: 4.7,
        reviewCount: 890,
        stock: 75,
        imageUrl: "https://images.pexels.com/photos/1647976/pexels-photo-1647976.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "MacBook Pro 16-inch",
        description: "Professional laptop with M3 Pro chip",
        price: NumberDecimal("2499.99"),
        category: "Electronics",
        rating: 4.9,
        reviewCount: 567,
        stock: 25,
        imageUrl: "https://images.pexels.com/photos/18105/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Nike Air Max 270",
        description: "Comfortable running shoes with Air Max technology",
        price: NumberDecimal("129.99"),
        category: "Sports",
        rating: 4.6,
        reviewCount: 2340,
        stock: 100,
        imageUrl: "https://images.pexels.com/photos/2526878/pexels-photo-2526878.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Adidas Ultraboost 22",
        description: "Premium running shoes with Boost midsole",
        price: NumberDecimal("179.99"),
        category: "Sports",
        rating: 4.7,
        reviewCount: 1890,
        stock: 80,
        imageUrl: "https://images.pexels.com/photos/2526878/pexels-photo-2526878.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Sony WH-1000XM5",
        description: "Wireless noise-canceling headphones",
        price: NumberDecimal("399.99"),
        category: "Electronics",
        rating: 4.8,
        reviewCount: 2100,
        stock: 60,
        imageUrl: "https://images.pexels.com/photos/3394650/pexels-photo-3394650.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Apple Watch Series 9",
        description: "Smartwatch with health monitoring features",
        price: NumberDecimal("399.99"),
        category: "Electronics",
        rating: 4.7,
        reviewCount: 1560,
        stock: 45,
        imageUrl: "https://images.pexels.com/photos/437037/pexels-photo-437037.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Dell XPS 13",
        description: "Ultrabook with InfinityEdge display",
        price: NumberDecimal("1199.99"),
        category: "Electronics",
        rating: 4.6,
        reviewCount: 890,
        stock: 30,
        imageUrl: "https://images.pexels.com/photos/18105/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    }
]);

print("SmartCart database initialized successfully!");
print("Admin user created: admin@smartcart.com / admin123");
print("Sample products added to database"); 