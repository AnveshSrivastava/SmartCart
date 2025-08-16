import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { 
  AlertTriangle, 
  Search, 
  Edit, 
  Package,
  TrendingDown,
  RefreshCw,
  Eye
} from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Skeleton } from '@/components/ui/skeleton';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import { productsAPI } from '../../services/api';
import { Product } from '../../types';
import { toast } from 'sonner';

const StockAlerts: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [lowStockProducts, setLowStockProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [showStockUpdate, setShowStockUpdate] = useState(false);
  const [newStock, setNewStock] = useState(0);

  useEffect(() => {
    loadProducts();
  }, []);

  useEffect(() => {
    filterProducts();
  }, [lowStockProducts, searchTerm]);

  const loadProducts = async () => {
    try {
      setLoading(true);
      const data = await productsAPI.getAll();
      setProducts(data);
      
      // Filter products with low stock (< 5)
      const lowStock = data.filter((product: Product) => product.stock < 5);
      setLowStockProducts(lowStock);
    } catch (error) {
      console.error('Error loading products:', error);
      toast.error('Failed to load products');
    } finally {
      setLoading(false);
    }
  };

  const filterProducts = () => {
    let filtered = lowStockProducts;

    if (searchTerm) {
      filtered = filtered.filter(product =>
        product.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        product.category.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    setFilteredProducts(filtered);
  };

  const handleUpdateStock = async () => {
    if (!selectedProduct) return;

    try {
      await productsAPI.updateStock(selectedProduct.id, newStock);
      toast.success('Stock updated successfully');
      setShowStockUpdate(false);
      setSelectedProduct(null);
      loadProducts();
    } catch (error) {
      console.error('Error updating stock:', error);
      toast.error('Failed to update stock');
    }
  };

  const openStockUpdate = (product: Product) => {
    setSelectedProduct(product);
    setNewStock(product.stock);
    setShowStockUpdate(true);
  };

  const getStockStatus = (stock: number) => {
    if (stock === 0) {
      return { 
        label: 'Out of Stock', 
        variant: 'destructive' as const,
        color: 'text-red-600',
        bgColor: 'bg-red-50'
      };
    } else if (stock < 3) {
      return { 
        label: 'Critical', 
        variant: 'destructive' as const,
        color: 'text-red-600',
        bgColor: 'bg-red-50'
      };
    } else if (stock < 5) {
      return { 
        label: 'Low Stock', 
        variant: 'secondary' as const,
        color: 'text-orange-600',
        bgColor: 'bg-orange-50'
      };
    } else {
      return { 
        label: 'In Stock', 
        variant: 'default' as const,
        color: 'text-green-600',
        bgColor: 'bg-green-50'
      };
    }
  };

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <Skeleton className="h-8 w-48" />
          <Skeleton className="h-10 w-32" />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[...Array(3)].map((_, i) => (
            <Card key={i} className="border-0 shadow-lg">
              <CardContent className="p-6">
                <Skeleton className="h-4 w-24 mb-2" />
                <Skeleton className="h-8 w-16 mb-2" />
                <Skeleton className="h-3 w-12" />
              </CardContent>
            </Card>
          ))}
        </div>
        <Card className="border-0 shadow-lg">
          <CardHeader>
            <Skeleton className="h-6 w-32" />
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {[...Array(5)].map((_, i) => (
                <Skeleton key={i} className="h-16 w-full" />
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  const criticalStock = lowStockProducts.filter(p => p.stock === 0);
  const lowStock = lowStockProducts.filter(p => p.stock > 0 && p.stock < 3);
  const warningStock = lowStockProducts.filter(p => p.stock >= 3 && p.stock < 5);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 flex items-center">
            <AlertTriangle className="h-7 w-7 text-orange-500 mr-3" />
            Stock Alerts
          </h1>
          <p className="text-gray-600">Monitor and manage low stock products</p>
        </div>
        <Button
          onClick={loadProducts}
          variant="outline"
          className="rounded-xl"
        >
          <RefreshCw className="h-4 w-4 mr-2" />
          Refresh
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
        >
          <Card className="border-0 shadow-lg bg-red-50 border-red-200">
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-red-600 mb-1">
                    Out of Stock
                  </p>
                  <p className="text-2xl font-bold text-red-700">
                    {criticalStock.length}
                  </p>
                  <p className="text-sm text-red-500">
                    Immediate attention required
                  </p>
                </div>
                <div className="p-3 rounded-xl bg-red-100">
                  <AlertTriangle className="h-6 w-6 text-red-600" />
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
        >
          <Card className="border-0 shadow-lg bg-orange-50 border-orange-200">
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-orange-600 mb-1">
                    Critical Stock
                  </p>
                  <p className="text-2xl font-bold text-orange-700">
                    {lowStock.length}
                  </p>
                  <p className="text-sm text-orange-500">
                    Less than 3 items
                  </p>
                </div>
                <div className="p-3 rounded-xl bg-orange-100">
                  <TrendingDown className="h-6 w-6 text-orange-600" />
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
        >
          <Card className="border-0 shadow-lg bg-yellow-50 border-yellow-200">
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-yellow-600 mb-1">
                    Low Stock
                  </p>
                  <p className="text-2xl font-bold text-yellow-700">
                    {warningStock.length}
                  </p>
                  <p className="text-sm text-yellow-500">
                    Less than 5 items
                  </p>
                </div>
                <div className="p-3 rounded-xl bg-yellow-100">
                  <Package className="h-6 w-6 text-yellow-600" />
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </div>

      {/* Low Stock Products Table */}
      <Card className="border-0 shadow-lg bg-white/80 backdrop-blur-sm">
        <CardHeader>
          <div className="flex justify-between items-center">
            <CardTitle className="flex items-center">
              <AlertTriangle className="h-5 w-5 mr-2 text-orange-500" />
              Low Stock Products ({filteredProducts.length})
            </CardTitle>
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
              <Input
                placeholder="Search products..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10 w-64 rounded-xl"
              />
            </div>
          </div>
        </CardHeader>
        <CardContent>
          {filteredProducts.length === 0 ? (
            <div className="text-center py-12">
              <Package className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                {lowStockProducts.length === 0 ? 'All products are well stocked!' : 'No products found'}
              </h3>
              <p className="text-gray-600">
                {lowStockProducts.length === 0 
                  ? 'Great job! All your products have sufficient stock levels.'
                  : 'Try adjusting your search criteria.'
                }
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Product</TableHead>
                    <TableHead>Category</TableHead>
                    <TableHead>Current Stock</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Price</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredProducts.map((product, index) => {
                    const stockStatus = getStockStatus(product.stock);
                    return (
                      <motion.tr
                        key={product.id}
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ delay: index * 0.05 }}
                        className={`hover:bg-gray-50 ${stockStatus.bgColor}`}
                      >
                        <TableCell>
                          <div className="flex items-center space-x-3">
                            <img
                              src={product.imageUrl}
                              alt={product.title}
                              className="w-12 h-12 object-cover rounded-lg"
                            />
                            <div>
                              <p className="font-medium text-gray-900">{product.title}</p>
                              <p className="text-sm text-gray-600 line-clamp-1">
                                {product.description}
                              </p>
                            </div>
                          </div>
                        </TableCell>
                        <TableCell>
                          <Badge variant="outline" className="rounded-full">
                            {product.category}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <span className={`font-bold text-lg ${stockStatus.color}`}>
                            {product.stock}
                          </span>
                        </TableCell>
                        <TableCell>
                          <Badge variant={stockStatus.variant} className="flex items-center space-x-1 w-fit">
                            <AlertTriangle className="h-3 w-3" />
                            <span>{stockStatus.label}</span>
                          </Badge>
                        </TableCell>
                        <TableCell className="font-medium">
                          ${product.price.toFixed(2)}
                        </TableCell>
                        <TableCell className="text-right">
                          <div className="flex justify-end space-x-2">
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => openStockUpdate(product)}
                              className="rounded-xl"
                            >
                              <Edit className="h-4 w-4 mr-1" />
                              Update Stock
                            </Button>
                          </div>
                        </TableCell>
                      </motion.tr>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Stock Update Dialog */}
      <Dialog open={showStockUpdate} onOpenChange={setShowStockUpdate}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle>Update Stock</DialogTitle>
          </DialogHeader>
          {selectedProduct && (
            <div className="space-y-4">
              <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
                <img
                  src={selectedProduct.imageUrl}
                  alt={selectedProduct.title}
                  className="w-12 h-12 object-cover rounded-lg"
                />
                <div>
                  <p className="font-medium">{selectedProduct.title}</p>
                  <p className="text-sm text-gray-600">Current stock: {selectedProduct.stock}</p>
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="newStock">New Stock Quantity</Label>
                <Input
                  id="newStock"
                  type="number"
                  min="0"
                  value={newStock}
                  onChange={(e) => setNewStock(parseInt(e.target.value) || 0)}
                  className="rounded-xl"
                />
              </div>

              <div className="flex justify-end space-x-2 pt-4 border-t">
                <Button
                  variant="outline"
                  onClick={() => setShowStockUpdate(false)}
                >
                  Cancel
                </Button>
                <Button
                  onClick={handleUpdateStock}
                  className="bg-blue-600 hover:bg-blue-700"
                >
                  Update Stock
                </Button>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default StockAlerts;