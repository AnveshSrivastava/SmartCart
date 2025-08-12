import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { Package, Truck, CheckCircle, Clock, MapPin } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Order } from '../types';
import { ordersAPI } from '../utils/api';

const Orders: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadOrders = async () => {
      try {
        const data = await ordersAPI.getHistory();
        // Mock orders for demonstration
        const mockOrders: Order[] = [
          {
            id: 'ORD-001',
            items: [
              {
                id: '1',
                product: {
                  id: '1',
                  name: 'Wireless Bluetooth Headphones',
                  price: 79.99,
                  image: 'https://images.pexels.com/photos/3394650/pexels-photo-3394650.jpeg?auto=compress&cs=tinysrgb&w=300',
                  category: 'Electronics',
                  description: 'High-quality wireless headphones with noise cancellation',
                  rating: 4.5,
                  reviews: 128,
                  inStock: true,
                },
                quantity: 1,
              },
              {
                id: '2',
                product: {
                  id: '2',
                  name: 'Smart Watch Series 8',
                  price: 399.99,
                  image: 'https://images.pexels.com/photos/267394/pexels-photo-267394.jpeg?auto=compress&cs=tinysrgb&w=300',
                  category: 'Electronics',
                  description: 'Advanced smart watch with health monitoring features',
                  rating: 4.8,
                  reviews: 256,
                  inStock: true,
                },
                quantity: 1,
              },
            ],
            total: 479.98,
            status: 'delivered',
            date: '2024-01-15',
            shippingAddress: {
              name: 'John Doe',
              street: '123 Main St',
              city: 'San Francisco',
              zip: '94102',
              country: 'USA',
            },
          },
          {
            id: 'ORD-002',
            items: [
              {
                id: '3',
                product: {
                  id: '3',
                  name: 'Organic Cotton T-Shirt',
                  price: 29.99,
                  image: 'https://images.pexels.com/photos/1018911/pexels-photo-1018911.jpeg?auto=compress&cs=tinysrgb&w=300',
                  category: 'Clothing',
                  description: 'Comfortable organic cotton t-shirt in various colors',
                  rating: 4.3,
                  reviews: 89,
                  inStock: true,
                },
                quantity: 2,
              },
            ],
            total: 59.98,
            status: 'shipped',
            date: '2024-01-20',
            shippingAddress: {
              name: 'John Doe',
              street: '123 Main St',
              city: 'San Francisco',
              zip: '94102',
              country: 'USA',
            },
          },
        ];
        setOrders(mockOrders);
      } catch (error) {
        console.error('Error loading orders:', error);
      } finally {
        setLoading(false);
      }
    };

    loadOrders();
  }, []);

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'pending':
        return <Clock className="h-4 w-4" />;
      case 'processing':
        return <Package className="h-4 w-4" />;
      case 'shipped':
        return <Truck className="h-4 w-4" />;
      case 'delivered':
        return <CheckCircle className="h-4 w-4" />;
      default:
        return <Clock className="h-4 w-4" />;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'pending':
        return 'bg-yellow-100 text-yellow-800';
      case 'processing':
        return 'bg-blue-100 text-blue-800';
      case 'shipped':
        return 'bg-purple-100 text-purple-800';
      case 'delivered':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-purple-50 pt-20">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="animate-pulse space-y-6">
            <div className="h-8 bg-gray-200 rounded w-1/4"></div>
            {[...Array(3)].map((_, i) => (
              <div key={i} className="h-32 bg-gray-200 rounded-lg"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-purple-50 pt-20">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8"
        >
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Your Orders</h1>
          <p className="text-gray-600">
            Track and manage your orders
          </p>
        </motion.div>

        {orders.length === 0 ? (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="text-center"
          >
            <Package className="h-24 w-24 text-gray-400 mx-auto mb-4" />
            <h2 className="text-xl font-semibold text-gray-900 mb-2">No orders yet</h2>
            <p className="text-gray-600 mb-6">
              When you place your first order, it will appear here.
            </p>
            <Button onClick={() => window.location.href = '/'}>
              Start Shopping
            </Button>
          </motion.div>
        ) : (
          <div className="space-y-6">
            {orders.map((order, index) => (
              <motion.div
                key={order.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.1 }}
              >
                <Card className="border-0 shadow-lg bg-white/90 backdrop-blur-sm">
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <CardTitle className="text-lg">Order #{order.id}</CardTitle>
                      <Badge className={`${getStatusColor(order.status)} flex items-center space-x-1`}>
                        {getStatusIcon(order.status)}
                        <span className="capitalize">{order.status}</span>
                      </Badge>
                    </div>
                    <div className="flex items-center space-x-4 text-sm text-gray-600">
                      <span>Placed on {new Date(order.date).toLocaleDateString()}</span>
                      <span>•</span>
                      <span>Total: ${order.total.toFixed(2)}</span>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      {/* Order Items */}
                      <div className="space-y-3">
                        {order.items.map((item) => (
                          <div key={item.id} className="flex items-center space-x-4">
                            <img
                              src={item.product.image}
                              alt={item.product.name}
                              className="w-16 h-16 object-cover rounded-lg"
                            />
                            <div className="flex-1">
                              <h4 className="font-medium">{item.product.name}</h4>
                              <p className="text-sm text-gray-600">
                                Quantity: {item.quantity} • ${item.product.price}
                              </p>
                            </div>
                            <div className="font-semibold">
                              ${(item.product.price * item.quantity).toFixed(2)}
                            </div>
                          </div>
                        ))}
                      </div>

                      {/* Shipping Address */}
                      <div className="border-t pt-4">
                        <div className="flex items-start space-x-2">
                          <MapPin className="h-4 w-4 text-gray-500 mt-1" />
                          <div className="text-sm">
                            <p className="font-medium">Shipping Address:</p>
                            <p className="text-gray-600">
                              {order.shippingAddress.name}<br />
                              {order.shippingAddress.street}<br />
                              {order.shippingAddress.city}, {order.shippingAddress.zip}<br />
                              {order.shippingAddress.country}
                            </p>
                          </div>
                        </div>
                      </div>

                      {/* Order Actions */}
                      <div className="flex space-x-2 pt-4 border-t">
                        {order.status === 'delivered' && (
                          <Button variant="outline" size="sm">
                            Reorder
                          </Button>
                        )}
                        <Button variant="outline" size="sm">
                          View Details
                        </Button>
                        {order.status === 'shipped' && (
                          <Button variant="outline" size="sm">
                            Track Package
                          </Button>
                        )}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </motion.div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Orders;