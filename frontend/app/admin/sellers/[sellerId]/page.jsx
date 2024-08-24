'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useRouter } from 'next/navigation';
import useAdminRoute from '@/app/hooks/useAdminRoute';

const BACKEND_URL = 'http://localhost:8080';

const ProductManagement = () => {
  useAdminRoute(); // Admin kontrolü burada 
  const router = useRouter();
  const { sellerId } = useParams();
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [query, setQuery] = useState('');
  const [newProduct, setNewProduct] = useState({ name: '', price: '', description: '' });
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [seller, setSeller] = useState([]);
  const [updatedSellerName, setUpdatedSellerName] = useState('');
  const [notification, setNotification] = useState('');

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/sellers/${sellerId}`);
        setSeller(response.data.data);
        setUpdatedSellerName(response.data.data.name);
        setProducts(response.data.data.products);
        setFilteredProducts(response.data.data.products);
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    fetchProducts();
  }, [sellerId]);

  const handleSearch = () => {
    const lowercasedQuery = query.toLowerCase();
    const filtered = products.filter(product =>
      product.name.toLowerCase().includes(lowercasedQuery) ||
      product.description.toLowerCase().includes(lowercasedQuery)
    );
    setFilteredProducts(filtered);
  };

  const addProduct = async () => {
    try {
      const productData = { ...newProduct, sellerId };
      const response = await axios.post(`${BACKEND_URL}/admin/sellers/${sellerId}/products`, productData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      const updatedProducts = [...products, response.data];
      setProducts(updatedProducts);
      setFilteredProducts(updatedProducts);
      setNewProduct({ name: '', price: '', description: '' });
      setNotification('Product added successfully!');
    } catch (error) {
      console.error('Error adding product:', error);
    }
  };

  const editProduct = async (productId) => {
    try {
      const response = await axios.put(`${BACKEND_URL}/admin/sellers/${sellerId}/products/${productId}`, selectedProduct, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      const updatedProducts = products.map(product => product.id === productId ? response.data : product);
      setProducts(updatedProducts);
      setFilteredProducts(updatedProducts);
      setSelectedProduct(null);
      setNotification('Product updated successfully!');
    } catch (error) {
      console.error('Error editing product:', error);
    }
  };

  const deleteProduct = async (productId) => {
    try {
      await axios.delete(`${BACKEND_URL}/admin/sellers/${sellerId}/products/${productId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      const updatedProducts = products.filter(product => product.id !== productId);
      setProducts(updatedProducts);
      setFilteredProducts(updatedProducts);
      setNotification('Product deleted successfully!');
    } catch (error) {
      console.error('Error deleting product:', error);
    }
  };

  const editSeller = async () => {
    try {
      const response = await axios.put(`${BACKEND_URL}/admin/sellers/${sellerId}`, { name: updatedSellerName }, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      setSeller(response.data.data);
      setNotification('Seller updated successfully!');
    } catch (error) {
      console.error('Error editing seller:', error);
    }
  };

  const deleteSeller = async () => {
    try {
      await axios.delete(`${BACKEND_URL}/admin/sellers/${sellerId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      router.push('/admin/sellers'); // Redirect back to manage sellers page
    } catch (error) {
      console.error('Error deleting seller:', error);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-r from-green-600 via-blue-400 to-red-600 flex items-center justify-center">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-4xl">
        <button onClick={() => router.back()} className="bg-gray-500 text-white px-4 py-2 rounded mb-4">
          Back to Sellers
        </button>
        <h1 className="text-3xl font-bold mb-8">Manage Products for Seller {seller.name}</h1>
  
        {/* Edit Seller */}
        <div className="mb-8 p-4 border rounded-lg shadow-sm bg-gray-50">
          <h3 className="text-lg font-semibold mb-4">Edit Seller</h3>
          <input
            type="text"
            placeholder="Seller Name"
            value={updatedSellerName}
            onChange={(e) => setUpdatedSellerName(e.target.value)}
            className="border p-2 mb-2 rounded-lg w-full"
          />
          <button
            onClick={editSeller}
            className="bg-green-500 text-white px-4 py-2 rounded"
          >
            Save Seller Changes
          </button>
          <button
            onClick={deleteSeller}
            className="bg-red-500 text-white px-4 py-2 rounded ml-2"
          >
            Delete Seller
          </button>
        </div>
  
        {/* Search Products */}
        <div className="mb-8 p-4 border rounded-lg shadow-sm bg-gray-50">
          <input
            type="text"
            placeholder="Search Products..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="border p-2 mb-2 rounded-lg w-full"
          />
          <button
            onClick={handleSearch}
            className="bg-blue-500 text-white px-4 py-2 rounded"
          >
            Search
          </button>
        </div>
  
        {/* Add New Product */}
        <div className="mb-8 p-4 border rounded-lg shadow-sm bg-gray-50">
          <h3 className="text-lg font-semibold mb-4">Add New Product</h3>
          <input
            type="text"
            placeholder="Product Name"
            value={newProduct.name}
            onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
            className="border p-2 mb-2 rounded-lg w-full"
          />
          <input
            type="text"
            placeholder="Price"
            value={newProduct.price}
            onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })}
            className="border p-2 mb-2 rounded-lg w-full"
          />
          <input
            type="text"
            placeholder="Description"
            value={newProduct.description}
            onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
            className="border p-2 mb-2 rounded-lg w-full"
          />
          <button
            onClick={addProduct}
            className="bg-blue-500 text-white px-4 py-2 rounded"
          >
            Add Product
          </button>
        </div>
  
        {/* Products List */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {filteredProducts.map(product => (
            <div key={product.id} className="border p-4 rounded-lg shadow-sm bg-white">
              <h2 className="text-xl font-semibold mb-2">{product.name}</h2>
              <p className="mb-2">{product.description}</p>
              <p className="font-bold mb-4">${product.price}</p>
              <button
                onClick={() => setSelectedProduct(product)}
                className="bg-green-500 text-white px-4 py-2 rounded"
              >
                Edit Product
              </button>
              <button
                onClick={() => deleteProduct(product.id)}
                className="bg-red-500 text-white px-4 py-2 rounded ml-2"
              >
                Delete Product
              </button>
  
              {selectedProduct && selectedProduct.id === product.id && (
                <div className="mt-4 p-4 border rounded-lg bg-gray-50">
                  <h4 className="text-md font-semibold mb-2">Edit Product</h4>
                  <input
                    type="text"
                    placeholder="Product Name"
                    value={selectedProduct.name}
                    onChange={(e) => setSelectedProduct({ ...selectedProduct, name: e.target.value })}
                    className="border p-2 mb-2 rounded-lg w-full"
                  />
                  <input
                    type="text"
                    placeholder="Price"
                    value={selectedProduct.price}
                    onChange={(e) => setSelectedProduct({ ...selectedProduct, price: e.target.value })}
                    className="border p-2 mb-2 rounded-lg w-full"
                  />
                  <input
                    type="text"
                    placeholder="Description"
                    value={selectedProduct.description}
                    onChange={(e) => setSelectedProduct({ ...selectedProduct, description: e.target.value })}
                    className="border p-2 mb-2 rounded-lg w-full"
                  />
                  <button
                    onClick={() => editProduct(product.id)}
                    className="bg-green-500 text-white px-4 py-2 rounded"
                  >
                    Save Product Changes
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
  
        {/* Notification */}
        {notification && (
          <div className="fixed bottom-4 right-4 bg-green-500 text-white px-4 py-2 rounded">
            {notification}
          </div>
        )}
      </div>
    </div>
  );
};

export default ProductManagement;
