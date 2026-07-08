'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

export default function AuthGuard({ children, loginPath }) {
  const router = useRouter();
  const [isChecking, setIsChecking] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      router.replace(loginPath);
    } else {
      setIsChecking(false);
    }
  }, [router, loginPath]);

  if (isChecking) return null;

  return <>{children}</>;
}