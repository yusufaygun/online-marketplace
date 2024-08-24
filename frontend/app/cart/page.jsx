'use client';

import React, { useContext } from 'react';
import { useRouter } from 'next/navigation';

import { CartContext } from '@/app/context/CartContext';

const CartPage = () => {
  const { cartItems, removeFromCart, updateCartItem } = useContext(CartContext);
  const router = useRouter();

  const handleQuantityChange = (itemId, newQuantity) => {
    if (newQuantity > 0) {
      updateCartItem(itemId, newQuantity);
    } else {
      removeFromCart(itemId); // Eğer quantity 0 veya daha azsa ürünü sepetten kaldır
    }
  };

  const totalPrice = cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);

  return (
    <div className="flex justify-center items-center min-h-screen bg-gradient-to-r from-teal-400 to-green-500">
      <div className="w-full max-w-3xl p-8 space-y-6 bg-white rounded-lg shadow-lg">
        <h1 className="text-3xl font-bold mb-6 text-center">Your Cart</h1>
        {cartItems.length === 0 ? (
          <p className="text-center text-lg">Your cart is empty</p>
        ) : (
          <div className="space-y-4">
            {cartItems.map(item => (
              <div key={item.id} className="flex justify-between items-center border p-4 rounded-lg shadow-sm bg-gray-50">
                <div>
                  <h2 className="text-xl font-semibold">{item.name}</h2>
                  <p className="text-gray-600">Quantity: {item.quantity}</p>
                  <p className="text-lg font-medium text-green-600">Price: ${item.price.toFixed(2)}</p>
                </div>
                <div className="flex items-center">
                  <button
                    onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                    className="bg-green-500 text-white px-3 py-1 rounded mr-2 hover:bg-green-600 transition"
                  >
                    +
                  </button>
                  <button
                    onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                    className="bg-yellow-500 text-white px-3 py-1 rounded mr-2 hover:bg-yellow-600 transition"
                  >
                    -
                  </button>
                  <button
                    onClick={() => removeFromCart(item.id)}
                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition"
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
        <div className="text-right font-bold text-2xl mt-6">
          Total Price: ${totalPrice.toFixed(2)}
        </div>
      </div>
    </div>
  );
};

export default CartPage;
