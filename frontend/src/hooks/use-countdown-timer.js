'use client';

import { useState, useEffect, useRef, useCallback } from 'react';

export function formatCountdown(totalSeconds) {
  const seconds = Math.max(0, totalSeconds);
  const h = Math.floor(seconds / 3600);
  const m = Math.floor((seconds % 3600) / 60);
  const s = seconds % 60;
  const pad = (n) => String(n).padStart(2, '0');
  return h > 0 ? `${pad(h)}:${pad(m)}:${pad(s)}` : `${pad(m)}:${pad(s)}`;
}

// Reusable second-resolution countdown. Stays paused until `autoStart`/`resume()`,
// counts down to zero, then fires `onExpire` exactly once.
export function useCountdownTimer(durationSeconds, { onExpire, autoStart = true } = {}) {
  const [secondsLeft, setSecondsLeft] = useState(durationSeconds);
  const [isRunning, setIsRunning] = useState(autoStart);
  const expiredRef = useRef(false);
  const onExpireRef = useRef(onExpire);

  useEffect(() => {
    onExpireRef.current = onExpire;
  });

  useEffect(() => {
    if (!isRunning) return;
    const id = setInterval(() => {
      setSecondsLeft((prev) => {
        if (prev <= 1) {
          clearInterval(id);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(id);
  }, [isRunning]);

  useEffect(() => {
    if (secondsLeft === 0 && !expiredRef.current) {
      expiredRef.current = true;
      setIsRunning(false);
      onExpireRef.current?.();
    }
  }, [secondsLeft]);

  const pause = useCallback(() => setIsRunning(false), []);
  const resume = useCallback(() => setIsRunning(true), []);
  const reset = useCallback((next = durationSeconds) => {
    expiredRef.current = false;
    setSecondsLeft(next);
  }, [durationSeconds]);

  return {
    secondsLeft,
    isRunning,
    isExpired: secondsLeft === 0,
    formatted: formatCountdown(secondsLeft),
    pause,
    resume,
    reset,
  };
}
