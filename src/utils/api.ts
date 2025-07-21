import axios from 'axios';
import { Product, Order, ChatMessage } from '../types';

const API_BASE_URL = 'https://api.smartcart.com'; // Mock API endpoint

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Mock data for development
const mockProducts: Product[] = [
  {
    id: '1',
    name: 'Wireless Bluetooth Headphones',
    price: 79.99,
    image: 'https://images.pexels.com/photos/3394650/pexels-photo-3394650.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Electronics',
    description: 'High-quality wireless headphones with noise cancellation',
    rating: 4.5,
    reviews: 128,
    inStock: true,
  },
  {
    id: '2',
    name: 'Smart Watch Series 8',
    price: 399.99,
    image: 'https://images.pexels.com/photos/267394/pexels-photo-267394.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Electronics',
    description: 'Advanced smart watch with health monitoring features',
    rating: 4.8,
    reviews: 256,
    inStock: true,
  },
  {
    id: '3',
    name: 'Organic Cotton T-Shirt',
    price: 29.99,
    image: 'https://images.pexels.com/photos/1018911/pexels-photo-1018911.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Clothing',
    description: 'Comfortable organic cotton t-shirt in various colors',
    rating: 4.3,
    reviews: 89,
    inStock: true,
  },
  {
    id: '4',
    name: 'Premium Coffee Beans',
    price: 24.99,
    image: 'https://images.pexels.com/photos/1695052/pexels-photo-1695052.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Food',
    description: 'Single-origin arabica coffee beans, medium roast',
    rating: 4.7,
    reviews: 167,
    inStock: true,
  },
  {
    id: '5',
    name: 'Yoga Mat Pro',
    price: 59.99,
    image: 'https://images.pexels.com/photos/4056723/pexels-photo-4056723.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Sports',
    description: 'Non-slip yoga mat with extra cushioning',
    rating: 4.6,
    reviews: 94,
    inStock: true,
  },
  {
    id: '6',
    name: 'Ergonomic Office Chair',
    price: 299.99,
    image: 'https://images.pexels.com/photos/586960/pexels-photo-586960.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Furniture',
    description: 'Comfortable ergonomic office chair with lumbar support',
    rating: 4.4,
    reviews: 203,
    inStock: true,
  },
  {
    id: '7',
    name: 'Wireless Gaming Mouse',
    price: 89.99,
    image: 'https://images.pexels.com/photos/2115257/pexels-photo-2115257.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Electronics',
    description: 'High-precision wireless gaming mouse with RGB lighting and customizable buttons',
    rating: 4.6,
    reviews: 342,
    inStock: true,
  },
  {
    id: '8',
    name: 'Leather Crossbody Bag',
    price: 149.99,
    image: 'https://images.pexels.com/photos/1152077/pexels-photo-1152077.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Clothing',
    description: 'Genuine leather crossbody bag with adjustable strap and multiple compartments',
    rating: 4.5,
    reviews: 178,
    inStock: true,
  },
  {
    id: '9',
    name: 'Protein Powder Vanilla',
    price: 39.99,
    image: 'https://images.pexels.com/photos/4397840/pexels-photo-4397840.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Food',
    description: 'Premium whey protein powder with natural vanilla flavor, 25g protein per serving',
    rating: 4.4,
    reviews: 289,
    inStock: true,
  },
  {
    id: '10',
    name: 'Basketball Official Size',
    price: 34.99,
    image: 'https://images.pexels.com/photos/1661950/pexels-photo-1661950.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Sports',
    description: 'Official size basketball with superior grip and durability for indoor and outdoor play',
    rating: 4.3,
    reviews: 156,
    inStock: true,
  },
  {
    id: '11',
    name: 'Modern Floor Lamp',
    price: 179.99,
    image: 'https://images.pexels.com/photos/1090638/pexels-photo-1090638.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Furniture',
    description: 'Contemporary floor lamp with adjustable brightness and sleek minimalist design',
    rating: 4.7,
    reviews: 124,
    inStock: true,
  },
  {
    id: '12',
    name: '4K Webcam Pro',
    price: 129.99,
    image: 'https://images.pexels.com/photos/4219654/pexels-photo-4219654.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Electronics',
    description: '4K Ultra HD webcam with auto-focus and noise-canceling microphone for streaming',
    rating: 4.5,
    reviews: 267,
    inStock: true,
  },
  {
    id: '13',
    name: 'The Great Gatsby',
    price: 12.99,
    image: 'https://images.pexels.com/photos/1029141/pexels-photo-1029141.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Books',
    description: 'Classic American novel by F. Scott Fitzgerald, a timeless tale of love and ambition',
    rating: 4.6,
    reviews: 1024,
    inStock: true,
  },
  {
    id: '14',
    name: 'Moisturizing Face Cream',
    price: 24.99,
    image: 'https://images.pexels.com/photos/3685530/pexels-photo-3685530.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Beauty',
    description: 'Hydrating face cream with natural ingredients for all skin types',
    rating: 4.4,
    reviews: 189,
    inStock: true,
  },
  {
    id: '15',
    name: 'Running Shoes Pro',
    price: 119.99,
    image: 'https://images.pexels.com/photos/2529148/pexels-photo-2529148.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Sports',
    description: 'Professional running shoes with advanced cushioning and breathable mesh',
    rating: 4.7,
    reviews: 456,
    inStock: true,
  },
  {
    id: '16',
    name: 'Wooden Dining Table',
    price: 599.99,
    image: 'https://images.pexels.com/photos/1080721/pexels-photo-1080721.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Furniture',
    description: 'Solid wood dining table for 6 people with elegant design',
    rating: 4.8,
    reviews: 78,
    inStock: true,
  },
  {
    id: '17',
    name: 'Denim Jacket',
    price: 79.99,
    image: 'https://images.pexels.com/photos/1040945/pexels-photo-1040945.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Clothing',
    description: 'Classic denim jacket with vintage wash and comfortable fit',
    rating: 4.3,
    reviews: 234,
    inStock: true,
  },
  {
    id: '18',
    name: 'Smartphone Pro Max',
    price: 999.99,
    image: 'https://images.pexels.com/photos/788946/pexels-photo-788946.jpeg?auto=compress&cs=tinysrgb&w=800',
    category: 'Electronics',
    description: 'Latest smartphone with advanced camera system and 5G connectivity',
    rating: 4.9,
    reviews: 2156,
    inStock: true,
  },
];

// API functions
export const fetchProducts = async (): Promise<Product[]> => {
  // Mock API call
  return new Promise((resolve) => {
    setTimeout(() => resolve(mockProducts), 800);
  });
};

export const fetchProductsByCategory = async (category: string): Promise<Product[]> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const filteredProducts = mockProducts.filter(
        p => p.category.toLowerCase() === category.toLowerCase()
      );
      resolve(filteredProducts);
    }, 800);
  });
};

export const fetchProductById = async (id: string): Promise<Product | null> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const product = mockProducts.find(p => p.id === id);
      resolve(product || null);
    }, 300);
  });
};

export const fetchOrders = async (): Promise<Order[]> => {
  return new Promise((resolve) => {
    setTimeout(() => resolve([]), 500);
  });
};

export const sendChatMessage = async (message: string): Promise<ChatMessage> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const responses = [
        "I'd be happy to help you find the perfect product! What are you looking for?",
        "Based on your preferences, I recommend checking out our electronics section.",
        "That's a great choice! This product has excellent reviews from our customers.",
        "Would you like me to help you compare similar products?",
        "I can help you find products within your budget. What's your price range?",
      ];
      const randomResponse = responses[Math.floor(Math.random() * responses.length)];
      
      resolve({
        id: Math.random().toString(36).substr(2, 9),
        type: 'assistant',
        content: randomResponse,
        timestamp: new Date(),
      });
    }, 1000);
  });
};

export const login = async (email: string, password: string): Promise<{ token: string; user: any }> => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (email === 'demo@smartcart.com' && password === 'demo123') {
        resolve({
          token: 'mock-jwt-token-12345',
          user: {
            id: '1',
            email: 'demo@smartcart.com',
            name: 'Demo User',
            avatar: 'https://images.pexels.com/photos/1681010/pexels-photo-1681010.jpeg?auto=compress&cs=tinysrgb&w=100',
          },
        });
      } else {
        reject(new Error('Invalid credentials'));
      }
    }, 1000);
  });
};

export const register = async (email: string, password: string, name: string): Promise<{ token: string; user: any }> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        token: 'mock-jwt-token-12345',
        user: {
          id: '1',
          email,
          name,
          avatar: 'https://images.pexels.com/photos/1681010/pexels-photo-1681010.jpeg?auto=compress&cs=tinysrgb&w=100',
        },
      });
    }, 1000);
  });
};

export default api;