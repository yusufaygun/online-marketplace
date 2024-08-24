// app/layout.jsx

import './globals.css';
import NavBar from './components/NavBar';
import { CartProvider } from '@/app/context/CartContext';  

export const metadata = {
  title: 'Online Marketplace',
  description: 'An example online marketplace application',
};

export default function Layout({ children }) {
  return (
    <html lang="en">
      <body>
        <CartProvider>  
          <NavBar />
          <main>{children}</main>
        </CartProvider>  
      </body>
    </html>
  );
}
