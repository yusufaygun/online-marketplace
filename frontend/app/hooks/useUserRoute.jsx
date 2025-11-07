import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

const useUserRoute = () => {
  const router = useRouter();
  const BACKEND_URL = "http://localhost:8080";

  useEffect(() => {
    const checkUserRole = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          // Maybe a different page could be opened here
          router.push('/forbidden');
          return;
        }

        const response = await axios.get(BACKEND_URL + '/roles/check-role', {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        

        const role = response.data.data;
        if (!(role === 'user' || role === 'admin')) {
          router.push('/forbidden'); // Redirect to Forbidden page if not a user
        }
      } catch (error) {
        console.error('Error checking admin role:', error);
        router.push('/forbidden'); // Redirect to forbidden page on error
      }
    };

    checkUserRole();
  }, [router]);
};

export default useUserRoute;
