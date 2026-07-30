'use client';

import { ExamLockProvider } from '@/context/ExamLockContext';

// Persists across the exam-viewing -> answer-upload transition (both are
// children of this segment), so the navigation lock survives that in-flow
// navigation and is only released once the upload page calls unlockExam().
export default function ExamSessionLayout({ children }) {
  return <ExamLockProvider>{children}</ExamLockProvider>;
}
