"use client";

import React from "react";
import { useRouter } from "next/navigation";

const HomePage = () => {
  const router = useRouter();

  return (
    <div className="flex justify-center items-center min-h-screen bg-gradient-to-r from-orange-500 to-purple-600">
      <div className="w-full max-w-lg p-8 space-y-6 bg-white rounded-lg shadow-lg text-center">
        <h1 className="text-5xl font-bold text-gray-800 mb-4">Welcome!</h1>
        <p className="text-lg text-gray-600 mb-6">
          Your one-stop shop for all your needs. Explore our wide range of products and enjoy a seamless shopping experience.
        </p>
        <button
          onClick={() => router.push("/products")}
          className="w-full py-3 text-white bg-indigo-600 rounded-md shadow-lg hover:bg-indigo-700 transition duration-300 transform hover:scale-105"
        >
          Browse Products
        </button>
      </div>
    </div>
  );
};

export default HomePage;
