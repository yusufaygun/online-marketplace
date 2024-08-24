'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';
import useAdminRoute from '@/app/hooks/useAdminRoute';

const BACKEND_URL = 'http://localhost:8080';

const ManageSellers = () => {
  useAdminRoute(); // Admin kontrolü burada 
  const router = useRouter();
  const [sellers, setSellers] = useState([]);
  const [filteredSellers, setFilteredSellers] = useState([]);
  const [newSeller, setNewSeller] = useState({ name: '' });
  const [searchQuery, setSearchQuery] = useState('');

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const fetchSellers = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/sellers?page=${page}&size=10`);
        const allSellers = response.data.data.content;
        const totalPages = response.data.data.totalPages;
        setSellers(allSellers);
        setFilteredSellers(allSellers);
        setTotalPages(totalPages);
      } catch (error) {
        console.error('Error fetching sellers:', error);
      }
    };

    fetchSellers();
  }, [page]);

  const addSeller = async () => {
    try {
      const response = await axios.post(`${BACKEND_URL}/admin/sellers`, newSeller, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      setSellers([...sellers, response.data]);
      setFilteredSellers([...filteredSellers, response.data]);
      setNewSeller({ name: '' });
      // Sayfayı refreshle, bunu bir gözden geçir
      window.location.reload();
    } catch (error) {
      console.error('Error adding seller:', error);
    }
  };

  const handleSearch = () => {
    const lowercasedQuery = searchQuery.toLowerCase();
    const filtered = sellers.filter(seller =>
      seller.name.toLowerCase().includes(lowercasedQuery)
    );
    setFilteredSellers(filtered);
  };

  const handleSellerClick = (sellerId) => {
    router.push(`/admin/sellers/${sellerId}`);
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage(page + 1);
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      setPage(page - 1);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-r from-blue-500 to-purple-600 flex flex-col items-center">
      <div className="container mx-auto px-4 py-8 bg-white rounded-lg shadow-lg">
        <h1 className="text-3xl font-bold mb-8 text-gray-800">Manage Sellers & Products</h1>
  
        {/* { Search Section } */}
        <div className="mb-8 bg-gray-100 p-4 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-2">Search Sellers</h2>
          <input
            type="text"
            placeholder="Search Sellers"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="border border-gray-300 p-2 mb-2 rounded-lg w-full"
          />
          <button
            onClick={handleSearch}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition"
          >
            Search
          </button>
        </div>
  
        {/* { Add New Seller } */}
        <div className="mb-8 bg-gray-100 p-4 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-2">Add New Seller</h2>
          <input
            type="text"
            placeholder="Seller Name"
            value={newSeller.name}
            onChange={(e) => setNewSeller({ ...newSeller, name: e.target.value })}
            className="border border-gray-300 p-2 mb-2 rounded-lg w-full"
          />
          <button
            onClick={addSeller}
            className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
          >
            Add Seller
          </button>
        </div>
  
        {/* { Sellers List } */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredSellers.map(seller => (
            <div
              key={seller.id}
              className="border border-gray-300 p-4 rounded-lg shadow-md cursor-pointer hover:shadow-lg transition"
              onClick={() => handleSellerClick(seller.id)}
            >
              <h3 className="text-xl font-semibold">{seller.name}</h3>
            </div>
          ))}
        </div>
  
        {/* { Pagination } */}
        <div className="flex justify-between items-center mt-8">
          <button 
            onClick={handlePreviousPage} 
            disabled={page === 0} 
            className={`px-4 py-2 rounded-lg ${page === 0 ? 'bg-gray-300' : 'bg-blue-600 text-white hover:bg-blue-700 transition'}`}
          >
            Previous
          </button>
          <span className="text-lg">Page {page + 1} of {totalPages}</span>
          <button 
            onClick={handleNextPage} 
            disabled={page === totalPages - 1} 
            className={`px-4 py-2 rounded-lg ${page === totalPages - 1 ? 'bg-gray-300' : 'bg-blue-600 text-white hover:bg-blue-700 transition'}`}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
  
};

export default ManageSellers;
