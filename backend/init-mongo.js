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
    },
    {
        title: "Redmi Note 13",
        description: "Budget smartphone with great camera",
        price: NumberDecimal("14999"),
        category: "Electronics",
        rating: 4.3,
        reviewCount: 1200,
        stock: 100,
        imageUrl: "https://images.pexels.com/photos/1647976/pexels-photo-1647976.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Realme Narzo 60x",
        description: "Affordable gaming smartphone",
        price: NumberDecimal("16999"),
        category: "Electronics",
        rating: 4.2,
        reviewCount: 800,
        stock: 80,
        imageUrl: "https://images.pexels.com/photos/1647976/pexels-photo-1647976.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Cotton T-Shirt",
        description: "Comfortable cotton t-shirt for everyday wear",
        price: NumberDecimal("599"),
        category: "Clothing",
        rating: 4.1,
        reviewCount: 500,
        stock: 200,
        imageUrl: "https://images.pexels.com/photos/1043474/pexels-photo-1043474.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Denim Jeans",
        description: "Classic blue denim jeans",
        price: NumberDecimal("1299"),
        category: "Clothing",
        rating: 4.4,
        reviewCount: 750,
        stock: 150,
        imageUrl: "https://images.pexels.com/photos/1043474/pexels-photo-1043474.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Programming Book",
        description: "Learn React and TypeScript development",
        price: NumberDecimal("899"),
        category: "Books",
        rating: 4.7,
        reviewCount: 300,
        stock: 50,
        imageUrl: "https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Cotton T-Shirt",
        description: "Comfortable cotton t-shirt for everyday wear",
        price: NumberDecimal("45"),
        category: "Clothing",
        rating: 4.1,
        reviewCount: 500,
        stock: 200,
        imageUrl: "https://images.pexels.com/photos/1043474/pexels-photo-1043474.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Casual Shorts",
        description: "Comfortable casual shorts for summer",
        price: NumberDecimal("55"),
        category: "Clothing",
        rating: 4.2,
        reviewCount: 300,
        stock: 150,
        imageUrl: "https://images.pexels.com/photos/1043474/pexels-photo-1043474.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Energy Bar",
        description: "High protein energy bar for fitness",
        price: NumberDecimal("25"),
        category: "Food",
        rating: 4.3,
        reviewCount: 200,
        stock: 100,
        imageUrl: "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Protein Shake",
        description: "Chocolate protein shake powder",
        price: NumberDecimal("40"),
        category: "Food",
        rating: 4.4,
        reviewCount: 150,
        stock: 80,
        imageUrl: "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        title: "Study Chair",
        description: "Ergonomic study chair for students",
        price: NumberDecimal("35"),
        category: "Furniture",
        rating: 4.0,
        reviewCount: 100,
        stock: 50,
        imageUrl: "https://images.pexels.com/photos/271816/pexels-photo-271816.jpeg?auto=compress&cs=tinysrgb&w=300",
        active: true,
        createdAt: new Date(),
        updatedAt: new Date()
    }
]);

print("SmartCart database initialized successfully!");
print("Admin user created: admin@smartcart.com / admin123");
print("Sample products added to database"); 