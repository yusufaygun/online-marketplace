'use client';

import React, { useEffect, useState, useContext } from 'react';
import { useParams, useRouter } from 'next/navigation';
import axios from 'axios';
import { CartContext } from '@/app/context/CartContext';

const BACKEND_URL = 'http://localhost:8080';

const ProductDetails = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const router = useRouter();
  const { addToCart } = useContext(CartContext);
  const [showNotification, setShowNotification] = useState(false);

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/products/${id}`);
        setProduct(response.data.data);
      } catch (error) {
        console.error("An error occurred while fetching the product:", error);
      }
    };

    fetchProduct();
  }, [id]);

  const handleAddToCart = () => {
    addToCart(product);
    setShowNotification(true);
    setTimeout(() => setShowNotification(false), 3000);
  };

  const handleAddToFavorites = async () => {
    console.log("token: " + localStorage.getItem('token'))
    try {
      await axios.post(BACKEND_URL + "/profile/favorites", id,
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
          },
        }
      );
      alert(`${product.name} added to favorites`);
    } catch (error) {
      console.error('Error adding product to favorites:', error);
    }
  };

  if (!product) {
    return <p className="text-center text-gray-600">Loading...</p>;
  }

  return (
    <div className="flex justify-center items-center min-h-screen bg-gradient-to-r from-teal-400 to-cyan-500">
      <div className="w-full max-w-4xl p-8 bg-white rounded-lg shadow-lg space-y-8">
        <div className="flex flex-col md:flex-row items-start gap-8">
          <div className="w-full md:w-1/2">
            <h1 className="text-4xl font-bold mb-4 text-gray-800">{product.name}</h1>
            <p className="mb-4 text-gray-600">{product.description}</p>
            <p className="mb-4 text-2xl font-bold text-green-600">${product.price}</p>
            <button
              onClick={() => router.push(`/sellers/${product.sellerId}`)}
              className="bg-gray-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-gray-600 transition duration-300"
            >
              {product.sellerName}
            </button>
          </div>
          <div className="w-full md:w-1/2 flex flex-col gap-4">
            <button
              onClick={handleAddToCart}
              className="bg-green-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-green-600 transition duration-300"
            >
              Add to Cart
            </button>
            <button
              onClick={handleAddToFavorites}
              className="bg-blue-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-blue-600 transition duration-300"
            >
              Add to Favorites
            </button>
          </div>
        </div>

        {/* Notification */}
        {showNotification && (
          <div className="fixed bottom-4 right-4 bg-blue-500 text-white px-4 py-2 rounded-lg shadow-lg flex flex-col items-center">
            <p className="text-lg mb-2">{product.name} added to cart!</p>
            <button
              onClick={() => router.push('/cart')}
              className="bg-white text-blue-500 px-4 py-2 rounded-lg shadow-md hover:bg-gray-100 transition duration-300"
            >
              Go to Cart
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProductDetails;
