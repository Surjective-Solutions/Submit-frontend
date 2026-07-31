'use client';

import { createContext, useContext, useState, useEffect } from "react";
import { toast } from 'sonner';
import { MOCK_STUDENT_ENROLLED_CLASSES } from "@/lib/mock-data";
import { getEnrolledClass, removeClassFromStudent, addClasstostudent } from "@/lib/api-client";

const EnrolledClassesContext = createContext(null);

export function EnrolledClassesProvider({ children }) {
  // const [classes, setClasses] = useState(() => [...MOCK_STUDENT_ENROLLED_CLASSES]);
  const [classes, setClasses] = useState([]);

  useEffect(() => {
    loadEnrolledClasses();
  }, []);

  async function loadEnrolledClasses() {
    try {
      const data = await getEnrolledClass();
      setClasses(data);
    } catch (error) {
      toast.error("Failed to load Classes");
    }
  }

  async function addClass(entry) {
    await addClasstostudent({ classSeq: entry.id });
    setClasses((prev) => [...prev, entry]);
  }

  async function removeClass(classId) {
    try {
      await removeClassFromStudent(classId);
      setClasses((prev) => prev.filter((c) => c.id !== classId));
    } catch {
      console.error('Failed to remove class');
    }
  }

  function setMonthlyPaymentStatus(classId, month, year, patch) {
    const cls = MOCK_STUDENT_ENROLLED_CLASSES.find((c) => c.id === classId);
    if (cls) {
      const entry = cls.monthly_payments.find((p) => p.month === month && p.year === year);
      if (entry) {
        Object.assign(entry, patch);
      } else {
        cls.monthly_payments.push({
          id: `mp-${classId}-${year}-${month}`,
          month,
          year,
          status: 'NOT_PAID',
          reference_number: null,
          paid_at: null,
          ...patch,
        });
      }
    }

    setClasses((prev) =>
      prev.map((c) => {
        if (c.id !== classId) return c;
        const idx = c.monthly_payments.findIndex((p) => p.month === month && p.year === year);
        const updated = [...c.monthly_payments];
        if (idx !== -1) {
          updated[idx] = { ...updated[idx], ...patch };
        } else {
          updated.push({
            id: `mp-${classId}-${year}-${month}`,
            month,
            year,
            status: 'NOT_PAID',
            reference_number: null,
            paid_at: null,
            ...patch,
          });
        }
        return { ...c, monthly_payments: updated };
      })
    );
  }

  return (
    <EnrolledClassesContext.Provider value={{ classes, addClass, removeClass, setMonthlyPaymentStatus }}>
      {children}
    </EnrolledClassesContext.Provider>
  );
}

export function useEnrolledClasses() {
  const ctx = useContext(EnrolledClassesContext);
  if (!ctx) throw new Error('useEnrolledClasses must be used within EnrolledClassesProvider');
  return ctx;
}
