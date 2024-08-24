"use client";

import React from "react";
import { useRouter } from "next/navigation";

import useAdminRoute from "../hooks/useAdminRoute";

const AdminPage = () => {
  useAdminRoute(); // Admin kontrolü burada 
  const router = useRouter();

  return (
    <div className="flex justify-center items-center min-h-screen bg-gradient-to-r from-purple-400 via-pink-500 to-red-500">
      <div className="w-full max-w-lg p-8 space-y-6 bg-white rounded-lg shadow-lg text-center">
        <h1 className="text-4xl font-bold text-gray-800 mb-4">Admin Dashboard</h1>
        <p className="text-lg text-gray-600 mb-6">Manage your marketplace with ease</p>
        <div className="space-y-4">
          <button
            onClick={() => router.push("/admin/users")}
            className="w-full py-3 text-white bg-indigo-600 rounded-lg shadow-md hover:bg-indigo-700 transition duration-300"
          >
            Manage Users
          </button>
          <button
            onClick={() => router.push("/admin/sellers")}
            className="w-full py-3 text-white bg-green-600 rounded-lg shadow-md hover:bg-green-700 transition duration-300"
          >
            Manage Sellers & Products
          </button>
        </div>
      </div>
    </div>
  );
};

export default AdminPage;
