import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { Cart, CartItem, Product } from '../types';
import { cartAPI } from '../services/api';
import { useAuth } from './AuthContext';
import { toast } from 'sonner';

interface CartContextType {
  items: CartItem[];
  cart: Cart | null;
  loading: boolean;
  addToCart: (productId: string, quantity?: number) => Promise<void>;
  removeFromCart: (productId: string) => Promise<void>;
  updateQuantity: (productId: string, quantity: number) => Promise<void>;
  clearCart: () => Promise<void>;
  getTotalPrice: () => number;
  getTotalItems: () => number;
  refreshCart: () => Promise<void>;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

interface CartProviderProps {
  children: ReactNode;
}

export const CartProvider: React.FC<CartProviderProps> = ({ children }) => {
  const [items, setItems] = useState<CartItem[]>([]);
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(false);
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      refreshCart();
    } else {
      // Clear cart when user logs out
      setCart(null);
      setItems([]);
    }
  }, [user]);

  const refreshCart = async () => {
    if (!user) return;
    
    try {
      setLoading(true);
      const cartData = await cartAPI.get();
      setCart(cartData);
      setItems(cartData.items || []);
    } catch (error) {
      console.error('Error fetching cart:', error);
      setCart(null);
      setItems([]);
    } finally {
      setLoading(false);
    }
  };

  const addToCart = async (productId: string, quantity: number = 1) => {
    if (!user) {
      toast.error('Please login to add items to cart');
      return;
    }

    try {
      setLoading(true);
      const updatedCart = await cartAPI.add(productId, quantity);
      setCart(updatedCart);
      setItems(updatedCart.items || []);
      
      const item = updatedCart.items.find((item: CartItem) => item.productId === productId);
      if (item) {
        toast.success(`Added ${item.productTitle} to cart`);
      }
    } catch (error) {
      console.error('Error adding to cart:', error);
      const errorMessage = error?.response?.data?.message || 'Failed to add item to cart';
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const removeFromCart = async (productId: string) => {
    if (!user) return;

    try {
      setLoading(true);
      const item = items.find(item => item.productId === productId);
      const updatedCart = await cartAPI.remove(productId);
      setCart(updatedCart);
      setItems(updatedCart.items || []);
      
      if (item) {
        toast.success(`Removed ${item.productTitle} from cart`);
      }
    } catch (error) {
      console.error('Error removing from cart:', error);
      toast.error('Failed to remove item from cart');
    } finally {
      setLoading(false);
    }
  };

  const updateQuantity = async (productId: string, quantity: number) => {
    if (!user) return;

    if (quantity <= 0) {
      await removeFromCart(productId);
      return;
    }

    try {
      setLoading(true);
      const updatedCart = await cartAPI.update(productId, quantity);
      setCart(updatedCart);
      setItems(updatedCart.items || []);
    } catch (error) {
      console.error('Error updating cart:', error);
      toast.error('Failed to update item quantity');
    } finally {
      setLoading(false);
    }
  };

  const clearCart = async () => {
    if (!user) return;

    try {
      setLoading(true);
      await cartAPI.clear();
      setCart(null);
      setItems([]);
      toast.success('Cart cleared');
    } catch (error) {
      console.error('Error clearing cart:', error);
      toast.error('Failed to clear cart');
    } finally {
      setLoading(false);
    }
  };

  const getTotalPrice = () => {
    return items.reduce((total, item) => total + item.productPrice * item.quantity, 0);
  };

  const getTotalItems = () => {
    return items.reduce((total, item) => total + item.quantity, 0);
  };

  const value: CartContextType = {
    items,
    cart,
    loading,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    getTotalPrice,
    getTotalItems,
    refreshCart,
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};