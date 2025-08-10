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

// Insert sample admin user
db.users.insertOne({
    fullName: "Admin User",
    email: "admin@smartcart.com",
    password: "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi", // password
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
        imageUrl: "https://example.com/iphone15pro.jpg",
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
        imageUrl: "https://example.com/galaxys24.jpg",
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
        imageUrl: "https://example.com/macbookpro.jpg",
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
        imageUrl: "https://example.com/nikeairmax.jpg",
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
        imageUrl: "https://example.com/adidasultraboost.jpg",
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
        imageUrl: "https://example.com/sonywh1000xm5.jpg",
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
        imageUrl: "https://example.com/applewatch.jpg",
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
        imageUrl: "https://example.com/dellxps13.jpg",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    }
]);

print("SmartCart database initialized successfully!");
print("Admin user created: admin@smartcart.com / password");
print("Sample products added to database"); 