'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useRouter } from "next/navigation";
import useUserRoute from '../hooks/useUserRoute';

const BACKEND_URL = 'http://localhost:8080';

const ProfilePage = () => {
  useUserRoute(); // User kontrolü burada
  const [user, setUser] = useState(null);
  const [favoriteProducts, setFavoriteProducts] = useState([]);
  const [blacklist, setBlacklist] = useState([]);
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem('token');

    const fetchUser = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/profile`, {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        setUser(response.data.data);
      } catch (error) {
        console.error("An error occurred while fetching the user profile:", error);
      }
    };

    const fetchFavoriteProducts = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/profile/favorites`, {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        setFavoriteProducts(response.data.data);
      } catch (error) {
        console.error('Error fetching favorite products:', error);
      }
    };

    const fetchBlacklist = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/profile/blacklist`, {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        setBlacklist(response.data.data);
      } catch (error) {
        console.error('Error fetching blacklist:', error);
      }
    };

    fetchUser();
    fetchFavoriteProducts();
    fetchBlacklist();
  }, []);

  const removeFromFavorites = async (productId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`${BACKEND_URL}/profile/favorites`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        data: { productId },  // Bu şekilde productId'yi data olarak gönderiyoruz
      });
      setFavoriteProducts(favoriteProducts.filter(product => product.id !== productId));
    } catch (error) {
      console.error('Error removing product from favorites:', error);
    }
  };

  const removeFromBlacklist = async (sellerId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`${BACKEND_URL}/profile/blacklist`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        data: { sellerId },  // Bu şekilde sellerId'yi data olarak gönderiyoruz
      });
      setBlacklist(blacklist.filter(seller => seller.id !== sellerId));
    } catch (error) {
      console.error('Error removing seller from blacklist:', error);
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gradient-to-r from-purple-500 to-pink-500">
      <div className="w-full max-w-4xl p-8 space-y-6 bg-white rounded-lg shadow-lg">
        <h1 className="text-3xl font-bold mb-6 text-center">Your Profile</h1>

        {user && (
          <div className="mb-8 p-4 bg-white shadow-md rounded-lg">
            <h2 className="text-2xl font-semibold mb-2">Welcome, {user.name}!</h2>
            <p className="text-gray-700">Username: {user.username}</p>
          </div>
        )}

        <h2 className="text-2xl font-semibold mb-4">Your Favorite Products</h2>
        {favoriteProducts.length === 0 ? (
          <p className="text-gray-600">You have no favorite products.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {favoriteProducts.map(product => (
              <div key={product.id} className="border p-4 rounded-lg shadow-sm bg-white">
                <h3 className="text-xl font-semibold mb-2">{product.name}</h3>
                <p className="text-gray-700 mb-2">{product.description}</p>
                <p className="text-green-600 font-bold mb-4">${product.price}</p>
                <button
                  onClick={() => router.push(`/products/${product.id}`)}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition duration-300"
                >
                  View Details
                </button>
                <button
                  onClick={() => removeFromFavorites(product.id)}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition duration-300 mt-2"
                >
                  Remove from Favorites
                </button>
              </div>
            ))}
          </div>
        )}

        <h2 className="text-2xl font-semibold mb-4 mt-8">Your Blacklist</h2>
        {blacklist.length === 0 ? (
          <p className="text-gray-600">You have no blocked sellers.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {blacklist.map(seller => (
              <div key={seller.id} className="border p-4 rounded-lg shadow-sm bg-white">
                <h3 className="text-xl font-semibold mb-2">{seller.name}</h3>
                <button
                  onClick={() => router.push(`/sellers/${seller.id}`)}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition duration-300"
                >
                  Go to Seller Page
                </button>
                <button
                  onClick={() => removeFromBlacklist(seller.id)}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition duration-300 mt-2"
                >
                  Remove from Blacklist
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default ProfilePage;
