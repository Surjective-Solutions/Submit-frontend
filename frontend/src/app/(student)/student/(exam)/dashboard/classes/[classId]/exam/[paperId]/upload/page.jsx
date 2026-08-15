'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useParams, useRouter } from 'next/navigation';
import { toast } from 'sonner';
import {
  ArrowLeft,
  Clock,
  FileText,
  Loader2,
  UploadCloud,
  AlertTriangle,
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useEnrolledClasses } from '@/context/EnrolledClassesContext';
import { useExamLock } from '@/context/ExamLockContext';
import { useCountdownTimer } from '@/hooks/use-countdown-timer';
import { UPLOAD_DURATION_SECONDS, findPaperInClass } from '@/lib/exam-utils';
import { markPaperSubmitted } from '@/lib/submitted-papers';
import { saveSubmissionPdf } from '@/lib/submission-pdf-storage';
import { uploadAnswerSheet } from "@/lib/api-client";

export default function AnswerUploadPage() {
  const { classId, paperId } = useParams();
  const router = useRouter();
  const { classes } = useEnrolledClasses();
  const { unlockExam } = useExamLock();
  const [file, setFile] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [timeUp, setTimeUp] = useState(false);

  const cls = classes.find((c) => c.id === Number(classId));
  const paper = findPaperInClass(cls, paperId);

  async function handleSubmit() {
    if (!file || isSubmitting) return;
    setIsSubmitting(true);
    timer.pause();

    try {
      await uploadAnswerSheet(classId, paperId, file);

      toast.success("Answer sheet submitted successfully!");

      unlockExam();
      router.replace(`/student/dashboard/classes/${classId}`);
    } catch (error) {
      console.error(error);
      toast.error("Upload failed.");
    } finally {
      setIsSubmitting(false);
    }
  }

  const timer = useCountdownTimer(UPLOAD_DURATION_SECONDS, {
    onExpire: () => {
      setTimeUp(true);
      if (file) {
        toast.warning("Time's up! Auto-submitting your answer sheet.");
        handleSubmit();
      } else {
        toast.error("Time's up! Please upload your answer sheet immediately.");
      }
    },
  });

  if (!cls || !paper) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
        <p className="text-gray-500 text-sm">Exam paper not found.</p>
        <Button
          variant="outline"
          render={
            <Link href="/student/dashboard">
              <ArrowLeft className="h-4 w-4 mr-1.5" />
              Back to My Classes
            </Link>
          }
        />
      </div>
    );
  }

  function handleFileChange(e) {
    setFile(e.target.files?.[0] ?? null);
  }

  return (
    <div className="max-w-xl mx-auto space-y-5">
      {/* Top bar */}
      <div className="bg-white rounded-xl border border-border px-5 py-4 flex items-center gap-4">
        <div className="min-w-0 flex-1">
          <h1 className="text-sm font-bold text-gray-900 leading-tight truncate">
            Upload Answer Sheet
          </h1>
          <p className="text-xs text-gray-400 mt-0.5 truncate">
            {paper.paper_name}
          </p>
        </div>
        <span
          className={`inline-flex items-center gap-1.5 text-sm font-semibold px-3 py-1.5 rounded-lg tabular-nums shrink-0 ${
            timeUp
              ? "bg-red-50 text-red-600 border border-red-200"
              : timer.secondsLeft <= 120
                ? "bg-amber-50 text-amber-700 border border-amber-200"
                : "bg-indigo-50 text-indigo-700 border border-indigo-200"
          }`}
        >
          <Clock className="h-3.5 w-3.5" />
          {timer.formatted}
        </span>
      </div>

      {timeUp && !isSubmitting && (
        <div className="flex items-start gap-2.5 rounded-xl border border-red-200 bg-red-50 px-4 py-3">
          <AlertTriangle className="h-4 w-4 text-red-500 mt-0.5 shrink-0" />
          <p className="text-sm text-red-700">
            Time is up. Please upload and submit your answer sheet right away.
          </p>
        </div>
      )}

      {/* Upload card */}
      <div className="bg-white rounded-2xl border border-border shadow-sm p-6 space-y-5">
        <div
          className="w-11 h-11 rounded-xl flex items-center justify-center"
          style={{
            background: "linear-gradient(135deg, #3940A0 0%, #5a62ff 100%)",
          }}
        >
          <UploadCloud className="h-5 w-5 text-white" />
        </div>

        <div>
          <h2 className="text-sm font-semibold text-gray-900">
            Scan and upload your answer sheet
          </h2>
          <p className="text-xs text-gray-500 mt-1">
            You have {UPLOAD_DURATION_SECONDS / 60} minutes to scan or
            photograph your answers and upload them as a single PDF file.
          </p>
        </div>

        <div className="space-y-1.5">
          <div className="flex items-center gap-3 rounded-lg border border-input px-3 py-2">
            <FileText className="h-4 w-4 text-gray-400 shrink-0" />
            <span className="text-sm text-gray-500 flex-1 truncate min-w-0">
              {file?.name ?? "No file selected"}
            </span>
            <label
              htmlFor="answer_pdf_input"
              className="cursor-pointer text-xs font-medium text-indigo-600 hover:text-indigo-700 shrink-0"
            >
              Choose file
            </label>
          </div>
          <input
            id="answer_pdf_input"
            type="file"
            accept=".pdf"
            className="hidden"
            onChange={handleFileChange}
            disabled={isSubmitting}
          />
        </div>

        <Button
          className="w-full text-white"
          style={{
            background: "linear-gradient(135deg, #3940A0 0%, #5a62ff 100%)",
          }}
          disabled={!file || isSubmitting}
          onClick={handleSubmit}
        >
          {isSubmitting && <Loader2 className="h-4 w-4 animate-spin mr-2" />}
          Submit Answer Sheet
        </Button>
      </div>
    </div>
  );
}
