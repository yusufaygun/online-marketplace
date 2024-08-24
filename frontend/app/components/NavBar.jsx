
// app/components/NavBar.jsx

'use client';

import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

const NavBar = () => {
  const router = useRouter();
  const [isAdmin, setIsAdmin] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const checkUserRole = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        return;
      }

      try {
        const response = await axios.get('http://localhost:8080/profile', {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        setIsLoggedIn(true); // Kullanıcı oturum açmış

        // Kullanıcı rolünü kontrol et
        const roles = response.data.data.roles;
        if (roles.includes('admin')) {
          setIsAdmin(true);
        }
      } catch (error) {
        console.error("An error occurred while checking user role:", error);
      }
    };

    checkUserRole();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token'); // Oturum tokenını kaldır
    setIsLoggedIn(false);
    setIsAdmin(false);
    router.push('/auth/login');
  };

  return (
    <nav className="bg-blue-500 p-4">
      <div className="container mx-auto flex justify-between items-center">
        <h1 className="text-white font-bold text-xl">MyStore</h1>
        <div className="flex space-x-4">
          <button onClick={() => router.push('/')} className="text-white hover:underline">Home</button>
          <button onClick={() => router.push('/products')} className="text-white hover:underline">Products</button>
          <button onClick={() => router.push('/cart')} className="text-white hover:underline">Cart</button>
          {isLoggedIn && <button onClick={() => router.push('/profile')} className="text-white hover:underline">Profile</button>}
          {isAdmin && <button onClick={() => router.push('/admin')} className="text-white hover:underline">Admin Manager</button>}
          {isLoggedIn ? (
            <button onClick={handleLogout} className="text-white hover:underline">Logout</button>
          ) : (
            <button onClick={() => router.push('/auth/login')} className="text-white hover:underline">Login</button>
          )}
        </div>
      </div>
    </nav>
  );
};

export default NavBar;


