'use client'

// app/forbidden.jsx

import { useRouter } from 'next/navigation';

export default function ForbiddenPage() {
  const router = useRouter();

    return (
      <div className="container mx-auto mt-8">
        <h1 className="text-3xl font-bold">403 - Forbidden</h1>
        <p>You don't have permission to access this page.</p>
        <button onClick={() => router.push("/auth/login")}
            className="bg-violet-500 text-white px-4 py-2 rounded mt-4">
              Login
            </button>
      </div>
    );
  }
  