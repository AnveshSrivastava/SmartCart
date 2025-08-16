export interface Product {
  id: string;
  title: string;
  price: number;
  imageUrl: string;
  category: string;
  description: string;
  rating: number;
  reviewCount: number;
  stock: number;
  active: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface CartItem {
  id: string;
  productId: string;
  quantity: number;
  productTitle: string;
  productImageUrl: string;
  productPrice: number;
}

export interface Cart {
  id: string;
  userId: string;
  items: CartItem[];
  total: number;
  createdAt: string;
  updatedAt: string;
}

export interface User {
  id: string;
  email: string;
  fullName: string;
  role: 'USER' | 'ADMIN';
  avatar?: string;
}

export interface OrderItem {
  id: string;
  productId: string;
  productTitle: string;
  productImageUrl: string;
  quantity: number;
  price: number;
  total: number;
}

export interface Order {
  id: string;
  userId: string;
  items: OrderItem[];
  total: number;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  shippingAddress: string;
  paymentInfo?: string;
  trackingNumber?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ChatMessage {
  id: string;
  type: 'user' | 'assistant';
  content: string;
  timestamp: Date;
}

export interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string, name: string) => Promise<void>;
  logout: () => void;
  isLoading: boolean;
}

export interface AdminStats {
  totalProducts: number;
  totalOrders: number;
  totalSales: number;
  pendingOrders: number;
  lowStockProducts: number;
}

export interface ProductFormData {
  title: string;
  description: string;
  price: number;
  category: string;
  stock: number;
  imageUrl: string;
  rating?: number;
  reviewCount?: number;
  active: boolean;
}