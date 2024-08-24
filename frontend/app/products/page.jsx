'use client';

import React, { useEffect, useState, useContext } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';
import { CartContext } from '@/app/context/CartContext';

const BACKEND_URL = 'http://localhost:8080';

export default function ProductsPage() {
    const [products, setProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [showNotification, setShowNotification] = useState(false);

    const router = useRouter();
    const { addToCart } = useContext(CartContext);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axios.get(`${BACKEND_URL}/products?page=${page}&size=2`);
                let allProducts = response.data.data.content;
                const totalPages = response.data.data.totalPages;

                const userIsLoggedIn = Boolean(localStorage.getItem('token'));

                if (userIsLoggedIn) {
                    const blacklistResponse = await axios.get(`${BACKEND_URL}/profile/blacklistids`, {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        },
                    });
                    const blacklist = blacklistResponse.data.data;

                    allProducts = allProducts.filter(product => !blacklist.includes(product.sellerId));
                }

                setProducts(allProducts);
                setFilteredProducts(allProducts);
                setTotalPages(totalPages);
            } catch (error) {
                console.error("An error occurred while fetching products:", error);
            }
        };

        fetchProducts();
    }, [page]);

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

    const handleAddToCart = (product) => {
        addToCart(product);
        setShowNotification(true);
        setTimeout(() => setShowNotification(false), 3000);
    };

    const handleSearch = () => {
        const lowercasedQuery = searchQuery.toLowerCase();
        const filtered = products.filter(product =>
            product.name.toLowerCase().includes(lowercasedQuery)
        );
        setFilteredProducts(filtered);
    };

    return (
        <div className="flex justify-center items-center min-h-screen bg-gradient-to-r from-teal-400 to-blue-500">
          <div className="w-full max-w-4xl p-8 space-y-6 bg-white rounded-lg shadow-lg">
            <h1 className="text-3xl font-bold mb-6 text-gray-800 text-center">Products</h1>
            
            <div className="mb-6 flex flex-col items-center">
              <input
                type="text"
                placeholder="Search Products"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="border rounded-lg p-2 mb-2 w-full md:w-1/2 lg:w-1/3"
              />
              <button
                onClick={handleSearch}
                className="bg-blue-500 text-white px-6 py-2 rounded-lg shadow-md hover:bg-blue-600 transition duration-300"
              >
                Search
              </button>
            </div>
    
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {filteredProducts.map((product) => (
                <div key={product.id} className="border rounded-lg shadow-md p-4 bg-white hover:bg-gray-100 transition duration-300">
                  <h2 
                    onClick={() => router.push(`/products/${product.id}`)}
                    className="text-xl font-semibold cursor-pointer mb-2 text-gray-800"
                  >
                    {product.name}
                  </h2>
                  <p className="text-lg font-bold text-green-600 mb-4">${product.price}</p>
                  <button
                    onClick={() => router.push(`/sellers/${product.sellerId}`)}
                    className="bg-violet-300 text-white px-4 py-2 rounded-lg shadow-sm hover:bg-violet-400 transition duration-300 mb-2 w-full"
                  >
                    {product.sellerName}
                  </button>
                  <button
                    onClick={() => handleAddToCart(product)}
                    className="bg-green-500 text-white px-4 py-2 rounded-lg shadow-sm hover:bg-green-600 transition duration-300 w-full"
                  >
                    Add to Cart
                  </button>
                </div>
              ))}
            </div>
    
            <div className="flex justify-between items-center mt-8">
              <button 
                onClick={handlePreviousPage} 
                disabled={page === 0} 
                className={`px-6 py-2 rounded-lg shadow-md ${page === 0 ? 'bg-gray-300' : 'bg-blue-500 text-white'} transition duration-300`}
              >
                Previous
              </button>
              <span className="text-lg">Page {page + 1} of {totalPages}</span>
              <button 
                onClick={handleNextPage} 
                disabled={page === totalPages - 1} 
                className={`px-6 py-2 rounded-lg shadow-md ${page === totalPages - 1 ? 'bg-gray-300' : 'bg-blue-500 text-white'} transition duration-300`}
              >
                Next
              </button>
            </div>
    
            {showNotification && (
              <div className="fixed bottom-4 right-4 bg-green-500 text-white px-4 py-2 rounded-lg shadow-lg">
                Product added to cart!
              </div>
            )}
          </div>
        </div>
      );
}
