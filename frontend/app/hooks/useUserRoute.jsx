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
          // belki daha farklı bir sayfa açılabilir burada
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
          router.push('/forbidden'); // user değilse Forbidden sayfasına yönlendir
        }
      } catch (error) {
        console.error('Error checking admin role:', error);
        router.push('/forbidden'); // Hata durumunda forbidden sayfasına yönlendir
      }
    };

    checkUserRole();
  }, [router]);
};

export default useUserRoute;
