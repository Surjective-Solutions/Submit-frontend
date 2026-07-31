'use client';

import { createContext, useCallback, useContext, useEffect, useRef, useState } from 'react';
import { toast } from 'sonner';

const ExamLockContext = createContext(null);

// Keeps the student trapped on the exam flow: blocks the back/forward button
// via popstate and warns on tab close/refresh via beforeunload. Scoped to the
// exam/[paperId] layout so it stays mounted across the exam -> upload
// transition and unmounts (cleaning up both listeners) once the student is
// routed away after a successful submission.
export function ExamLockProvider({ children }) {
  const [locked, setLocked] = useState(true);
  const lockedRef = useRef(true);

  useEffect(() => {
    lockedRef.current = locked;
  }, [locked]);

  useEffect(() => {
    window.history.pushState(null, '', window.location.href);

    function handlePopState() {
      if (!lockedRef.current) return;
      window.history.pushState(null, '', window.location.href);
      toast.warning('You cannot leave the exam until you end it and submit your answers.');
    }

    window.addEventListener('popstate', handlePopState);
    return () => window.removeEventListener('popstate', handlePopState);
  }, []);

  useEffect(() => {
    function handleBeforeUnload(e) {
      if (!lockedRef.current) return;
      e.preventDefault();
      e.returnValue = '';
      return '';
    }

    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => window.removeEventListener('beforeunload', handleBeforeUnload);
  }, []);

  const unlockExam = useCallback(() => setLocked(false), []);

  return (
    <ExamLockContext.Provider value={{ locked, unlockExam }}>
      {children}
    </ExamLockContext.Provider>
  );
}

export function useExamLock() {
  const ctx = useContext(ExamLockContext);
  if (!ctx) throw new Error('useExamLock must be used within an ExamLockProvider');
  return ctx;
}
