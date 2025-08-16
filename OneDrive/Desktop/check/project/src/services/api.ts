import axios from 'axios';
import { Product, Order, User, CartItem, AdminStats } from '../types';

// API Configuration
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      // Only redirect if not already on login page
      if (!window.location.pathname.includes('/login')) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: async (email: string, password: string) => {
    const response = await api.post('/auth/login', { email, password });
    return response.data;
  },

  register: async (email: string, password: string, fullName: string) => {
    const response = await api.post('/auth/register', { email, password, fullName });
    return response.data;
  },

  refreshToken: async (refreshToken: string) => {
    const response = await api.post('/auth/refresh', {}, {
      headers: { Authorization: `Bearer ${refreshToken}` }
    });
    return response.data;
  },

  validateToken: async () => {
    const response = await api.get('/auth/validate');
    return response.data;
  }
};

// Products API
export const productsAPI = {
  getAll: async (page = 0, size = 20) => {
    const response = await api.get(`/products?page=${page}&size=${size}`);
    return response.data;
  },

  getById: async (id: string) => {
    const response = await api.get(`/products/${id}`);
    return response.data;
  },

  getByCategory: async (category: string) => {
    const response = await api.get(`/products/category/${category}`);
    return response.data;
  },

  search: async (query: string) => {
    const response = await api.get(`/products/search?q=${query}`);
    return response.data;
  },

  getByPriceRange: async (minPrice: number, maxPrice: number) => {
    const response = await api.get(`/products/price-range?minPrice=${minPrice}&maxPrice=${maxPrice}`);
    return response.data;
  },

  getAvailable: async () => {
    const response = await api.get('/products/available');
    return response.data;
  },

  getByRating: async (minRating: number) => {
    const response = await api.get(`/products/rating/${minRating}`);
    return response.data;
  },

  // Admin only
  create: async (productData: Partial<Product>) => {
    const response = await api.post('/products/admin', productData);
    return response.data;
  },

  update: async (id: string, productData: Partial<Product>) => {
    const response = await api.put(`/products/admin/${id}`, productData);
    return response.data;
  },

  delete: async (id: string) => {
    const response = await api.delete(`/products/admin/${id}`);
    return response.data;
  },

  updateStock: async (id: string, stock: number) => {
    const response = await api.put(`/products/admin/${id}/stock?stock=${stock}`);
    return response.data;
  },

  updateRating: async (id: string, rating: number, reviewCount: number) => {
    const response = await api.put(`/products/admin/${id}/rating?rating=${rating}&reviewCount=${reviewCount}`);
    return response.data;
  },

  getStats: async () => {
    const response = await api.get('/products/admin/stats');
    return response.data;
  }
};

// Cart API
export const cartAPI = {
  get: async () => {
    const response = await api.get('/cart');
    return response.data;
  },

  add: async (productId: string, quantity = 1) => {
    const response = await api.post(`/cart/add?productId=${productId}&quantity=${quantity}`);
    return response.data;
  },

  update: async (productId: string, quantity: number) => {
    const response = await api.put(`/cart/update?productId=${productId}&quantity=${quantity}`);
    return response.data;
  },

  remove: async (productId: string) => {
    const response = await api.delete(`/cart/remove/${productId}`);
    return response.data;
  },

  clear: async () => {
    const response = await api.delete('/cart/clear');
    return response.data;
  },

  getCount: async () => {
    const response = await api.get('/cart/count');
    return response.data;
  }
};

// Orders API
export const ordersAPI = {
  place: async (shippingAddress: string, paymentInfo?: string) => {
    const response = await api.post(`/order/place?shippingAddress=${encodeURIComponent(shippingAddress)}&paymentInfo=${paymentInfo || ''}`);
    return response.data;
  },

  getHistory: async (page = 0, size = 10) => {
    const response = await api.get(`/order/history?page=${page}&size=${size}`);
    return response.data;
  },

  getById: async (orderId: string) => {
    const response = await api.get(`/order/${orderId}`);
    return response.data;
  },

  getByStatus: async (status: string) => {
    const response = await api.get(`/order/status/${status}`);
    return response.data;
  },

  getStats: async () => {
    const response = await api.get('/order/stats');
    return response.data;
  },

  // Admin only
  getAll: async (page = 0, size = 10) => {
    const response = await api.get(`/order/admin/all?page=${page}&size=${size}`);
    return response.data;
  },

  getAllByStatus: async (status: string) => {
    const response = await api.get(`/order/admin/status/${status}`);
    return response.data;
  },

  updateStatus: async (orderId: string, status: string) => {
    const response = await api.put(`/order/admin/${orderId}/status?status=${status}`);
    return response.data;
  },

  updateTracking: async (orderId: string, trackingNumber: string) => {
    const response = await api.put(`/order/admin/${orderId}/tracking?trackingNumber=${trackingNumber}`);
    return response.data;
  },

  getPendingAndConfirmed: async () => {
    const response = await api.get('/order/admin/pending');
    return response.data;
  },

  getByDateRange: async (startDate: string, endDate: string) => {
    const response = await api.get(`/order/admin/date-range?startDate=${startDate}&endDate=${endDate}`);
    return response.data;
  },

  getAdminStats: async () => {
    const response = await api.get('/order/admin/stats');
    return response.data;
  }
};

// Admin API
export const adminAPI = {
  getStats: async (): Promise<AdminStats> => {
    try {
      const [productStats, orderStats] = await Promise.all([
        productsAPI.getStats(),
        ordersAPI.getAdminStats()
      ]);
      
      return {
        totalProducts: productStats.totalProducts,
        totalOrders: orderStats.totalOrders,
        totalSales: orderStats.totalSales,
        pendingOrders: orderStats.pendingOrders,
        lowStockProducts: productStats.lowStockProducts,
      };
    } catch (error) {
      console.error('Error fetching admin stats:', error);
      throw error;
    }
  }
};

export default api;

// Legacy API functions for backward compatibility
export const sendChatMessage = async (message: string) => {
  // Mock chatbot response - replace with actual AI service integration
  return new Promise((resolve) => {
    setTimeout(() => {
      const responses = [
        "Hello! How can I help you today?",
        "I'm here to assist you with your shopping needs.",
        "Feel free to ask me about our products or services.",
        "Is there anything specific you're looking for?",
        "I can help you find the perfect product for your needs."
      ];
      const randomResponse = responses[Math.floor(Math.random() * responses.length)];
      resolve({ message: randomResponse });
    }, 1000);
  });
};

export const fetchProductsByCategory = async (category: string) => {
  return productsAPI.getByCategory(category);
};

export const fetchOrders = async () => {
  return ordersAPI.getHistory();
};