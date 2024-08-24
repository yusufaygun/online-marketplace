"use client";

import React, { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import axios from 'axios';

const BACKEND_URL = 'http://localhost:8080';

const SellerPage = () => {
  const { id } = useParams();
  const [seller, setSeller] = useState(null);
  const router = useRouter();

  useEffect(() => {
    const fetchSeller = async () => {
      try {
        const response = await fetch(`${BACKEND_URL}/sellers/${id}`);
        const data = await response.json();
        setSeller(data.data);
      } catch (error) {
        console.error("Error fetching seller data:", error);
      }
    };

    fetchSeller();
  }, [id]);

  const handleAddToBlacklist = async () => {
    try {
      await axios.post(
        `${BACKEND_URL}/profile/blacklist`, id,  // Seller ID
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
          },
        }
      );
      alert(`${seller.name} added to blacklist`);
    } catch (error) {
      console.error('Error adding seller to blacklist:', error);
    }
  };

  if (!seller) {
    return <p className="text-center text-gray-600">Loading...</p>;
  }

  return (
    <div className="flex justify-center items-center min-h-screen bg-gradient-to-r from-purple-400 to-pink-500">
      <div className="w-full max-w-5xl p-8 space-y-6 bg-white rounded-lg shadow-lg">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-800">{seller.name}</h1>
          <h2 className="text-2xl font-semibold text-gray-600">Products by {seller.name}</h2>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {seller.products && seller.products.length > 0 ? (
            seller.products.map(product => (
              <div key={product.id} className="border rounded-lg p-4 shadow-sm bg-white">
                <h3 className="text-xl font-semibold mb-2">{product.name}</h3>
                <p className="text-lg font-bold text-green-600 mb-2">${product.price}</p>
                <p className="text-gray-600 mb-4">{product.description}</p>
                <button 
                  onClick={() => router.push(`/products/${product.id}`)} 
                  className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition duration-300"
                >
                  View Details
                </button> 
              </div>
            ))
          ) : (
            <p className="text-center text-gray-600">No products available.</p>
          )}
        </div>

        <div className="text-center mt-8">
          <button 
            onClick={handleAddToBlacklist}
            className="bg-blue-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-blue-600 transition duration-300"
          >
            Add Seller to Blacklist
          </button>
        </div>
      </div>
    </div>
  );
};

export default SellerPage;
