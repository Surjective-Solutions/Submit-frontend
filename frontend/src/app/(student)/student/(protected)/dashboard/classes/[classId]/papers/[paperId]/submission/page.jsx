'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { toast } from 'sonner';
import {
  ArrowLeft,
  Award,
  CheckCircle,
  Eye,
  FileText,
  ListChecks,
  MessageSquareText,
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useEnrolledClasses } from '@/context/EnrolledClassesContext';
import { findPaperInClass } from '@/lib/exam-utils';
import { toMonthYearSlug } from '@/lib/billing-utils';
import { getGradeDetailsForPaper } from '@/lib/api-client';

function formatDate(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleDateString('en-GB', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  });
}

function formatQuestionLabel(questionId) {
  const [parentId, subId] = String(questionId).split('-');
  if (!subId) return `Q${parentId}`;
  const subLetter = /^\d+$/.test(subId)
    ? String.fromCharCode(96 + Number(subId))
    : subId;
  return `Q${parentId} (${subLetter})`;
}

// ── Document Card ─────────────────────────────────────────────────────────────

const ACCENT_CLASSES = {
  indigo: 'border-indigo-200 text-indigo-700 hover:bg-indigo-50',
  green: 'border-green-200 text-green-700 hover:bg-green-50',
};

function DocumentCard({ icon: Icon, title, url, actionLabel, unavailableText, accent = 'indigo' }) {
  return (
    <div className="rounded-xl border border-gray-200 bg-white p-5 space-y-3">
      <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider">{title}</p>
      {url ? (
        <Button
          variant="outline"
          size="sm"
          className={`w-full gap-2 ${ACCENT_CLASSES[accent]}`}
          onClick={() => window.open(url, '_blank')}
        >
          <Icon className="h-3.5 w-3.5" />
          {actionLabel}
        </Button>
      ) : (
        <p className="text-sm text-gray-400">{unavailableText}</p>
      )}
    </div>
  );
}

// ── Grade Breakdown ──────────────────────────────────────────────────────────

function GradeBreakdownSection({ classId, paperId, isGraded }) {
  const [gradeDetails, setGradeDetails] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!isGraded) return;
    let cancelled = false;
    loadGradeDetails();
    return () => {
      cancelled = true;
    };

    async function loadGradeDetails() {
      try {
        setLoading(true);
        const data = await getGradeDetailsForPaper(paperId);
        if (!cancelled) setGradeDetails(data ?? []);
      } catch {
        if (!cancelled) toast.error('Failed to load grade breakdown.');
      } finally {
        if (!cancelled) setLoading(false);
      }
    }
  }, [paperId, isGraded]);

  if (!isGraded) return null;

  if (loading) {
    return (
      <div className="rounded-xl border border-gray-200 bg-white p-5 space-y-3">
        <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider">
          Grade Breakdown
        </p>
        <p className="text-sm text-gray-400">Loading grade breakdown...</p>
      </div>
    );
  }

  if (gradeDetails.length === 0) return null;

  return (
    <div className="rounded-xl border border-green-200 bg-white p-5 space-y-4">
      <div className="flex items-center gap-2">
        <div className="w-8 h-8 rounded-lg bg-green-100 flex items-center justify-center shrink-0">
          <ListChecks className="h-4 w-4 text-green-600" />
        </div>
        <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider">
          Grade Breakdown
        </p>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="text-left text-[11px] font-semibold text-gray-400 uppercase tracking-wider border-b border-gray-100">
              <th className="py-2 pr-4 font-semibold">Question</th>
              <th className="py-2 font-semibold">Marks</th>
            </tr>
          </thead>
          <tbody>
            {gradeDetails.map((detail) => (
              <tr key={detail.question_id} className="border-b border-gray-50 last:border-0">
                <td className="py-3 pr-4 font-medium text-gray-900 align-top whitespace-nowrap">
                  {formatQuestionLabel(detail.question_id)}
                </td>
                <td className="py-3 align-top whitespace-nowrap">
                  <span className="font-semibold text-green-700">{detail.marks_awarded}</span>
                  <span className="text-gray-400"> / {detail.max_marks}</span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Button
        variant="outline"
        size="sm"
        nativeButton={false}
        className="gap-2 border-green-200 text-green-700 hover:bg-green-50"
        render={<Link href={`/student/dashboard/classes/${classId}/papers/${paperId}/feedback`} />}
      >
        <MessageSquareText className="h-3.5 w-3.5" />
        View Detailed Feedback
      </Button>
    </div>
  );
}

// ── Page ──────────────────────────────────────────────────────────────────────

export default function PaperSubmissionPage() {
  const { classId, paperId } = useParams();
  const { classes } = useEnrolledClasses();

  const cls = classes.find((c) => c.id === Number(classId));

  if (!cls) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
        <p className="text-gray-500 text-sm">Class not found.</p>
        <Button
          variant="outline"
          nativeButton={false}
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

  const paper = findPaperInClass(cls, paperId);
  console.log('submission page paper.submission_url:', paper?.submission_url);

  if (!paper) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
        <p className="text-gray-500 text-sm">Paper not found.</p>
        <Button
          variant="outline"
          nativeButton={false}
          render={
            <Link href={`/student/dashboard/classes/${classId}`}>
              <ArrowLeft className="h-4 w-4 mr-1.5" />
              Back to Class
            </Link>
          }
        />
      </div>
    );
  }

  const monthEntry = (cls.papers_by_month ?? []).find((e) =>
    (e.papers ?? []).some((p) => String(p.id) === String(paperId)),
  );
  const now = new Date();
  const isCurrentMonth =
    monthEntry && monthEntry.month === now.getMonth() + 1 && monthEntry.year === now.getFullYear();
  const backHref = monthEntry && !isCurrentMonth
    ? `/student/dashboard/classes/${classId}/${toMonthYearSlug(monthEntry.month, monthEntry.year)}`
    : `/student/dashboard/classes/${classId}`;

  const isGraded = paper.submission_status === 'GRADED';

  return (
    <div className="space-y-6">
      {/* Top bar */}
      <div className="bg-white rounded-xl border border-border px-5 py-4 flex items-center gap-4">
        <Link
          href={backHref}
          className="inline-flex items-center gap-1.5 text-sm text-gray-500 hover:text-indigo-600 transition-colors shrink-0"
        >
          <ArrowLeft className="h-4 w-4" />
          Back
        </Link>
        <div className="w-px h-5 bg-gray-200 shrink-0" />
        <div className="min-w-0">
          <h1 className="text-base font-bold text-gray-900 leading-tight truncate">
            {paper.paper_name}
          </h1>
          <p className="text-xs text-gray-400 mt-0.5">
            {cls.class_name} · Due {formatDate(paper.due_date)}
          </p>
        </div>
      </div>

      {/* Grade banner */}
      {isGraded && paper.grade && (
        <div className="flex items-center gap-3 px-5 py-4 rounded-xl bg-green-50 border border-green-200">
          <div className="w-10 h-10 rounded-lg bg-green-100 flex items-center justify-center shrink-0">
            <Award className="h-5 w-5 text-green-600" />
          </div>
          <div>
            <p className="text-[11px] font-semibold text-green-600 uppercase tracking-wider">Your Grade</p>
            <p className="text-xl font-bold text-green-700 leading-tight">{paper.grade}</p>
          </div>
        </div>
      )}

      {/* Documents */}
      <div className="grid sm:grid-cols-2 gap-4">
        <DocumentCard
          icon={FileText}
          title="Original Paper"
          url={paper.exam_pdf_url}
          actionLabel="Open Exam Paper"
          unavailableText="Exam paper not available."
          accent="indigo"
        />
        <DocumentCard
          icon={Eye}
          title="Your Submission"
          url={paper.submission_url}
          actionLabel="Open My Submission"
          unavailableText="Submission file not available."
          accent="indigo"
        />
        <div className="sm:col-span-2">
          {isGraded ? (
            <DocumentCard
              icon={CheckCircle}
              title="Graded Submission"
              url={paper.graded_pdf_url}
              actionLabel="Open Graded Submission"
              unavailableText="Graded submission not available yet."
              accent="green"
            />
          ) : (
            <div className="rounded-xl border border-gray-200 bg-white p-5 space-y-1">
              <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider">Graded Submission</p>
              <p className="text-sm text-gray-400">Not graded yet.</p>
            </div>
          )}
        </div>
      </div>

      {/* Grade Breakdown */}
      <GradeBreakdownSection classId={classId} paperId={paperId} isGraded={isGraded} />
    </div>
  );
}
