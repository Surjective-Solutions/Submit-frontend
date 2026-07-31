'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { isTokenExpired, markSessionExpired } from '@/lib/auth';

const SESSION_CHECK_INTERVAL_MS = 5000;

export default function AuthGuard({ children, loginPath }) {
  const router = useRouter();
  const [isChecking, setIsChecking] = useState(true);

  useEffect(() => {
    const token = sessionStorage.getItem('token');
    if (!token || isTokenExpired()) {
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('role');
      router.replace(loginPath);
    } else {
      setIsChecking(false);
    }
  }, [router, loginPath]);

  // Catches tokens that expire while the user stays on the page without navigating.
  useEffect(() => {
    if (isChecking) return;

    const intervalId = setInterval(() => {
      if (isTokenExpired()) {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('role');
        markSessionExpired();
        router.replace(loginPath);
      }
    }, SESSION_CHECK_INTERVAL_MS);

    return () => clearInterval(intervalId);
  }, [isChecking, router, loginPath]);

  if (isChecking) return null;

  return <>{children}</>;
}