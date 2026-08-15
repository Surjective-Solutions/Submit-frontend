'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { ArrowLeft, Eye, Lock } from 'lucide-react';
import { Button } from '@/components/ui/button';
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from '@/components/ui/table';
import PayNowDialog from '@/components/student/PayNowDialog';
import ViewPaymentSubmissionDialog from '@/components/student/ViewPaymentSubmissionDialog';
import { useEnrolledClasses } from '@/context/EnrolledClassesContext';
import { MOCK_PAYMENTS } from '@/lib/mock-data';
import {
  isPaidMonth,
  getMonthStatus,
  formatMonthYear,
  parseMonthYearSlug,
} from '@/lib/billing-utils';

function findLatestPaymentSubmission(classId, month, year) {
  return (
    MOCK_PAYMENTS
      .filter((p) => p.class_id === classId && p.month === month && p.year === year)
      .sort((a, b) => new Date(b.submitted_at) - new Date(a.submitted_at))[0] ?? null
  );
}

// ── Helpers ───────────────────────────────────────────────────────────────────

function formatDate(iso) {
  return new Date(iso).toLocaleDateString('en-GB', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  });
}

// ── Badges ────────────────────────────────────────────────────────────────────

function SubmissionStatusBadge({ status }) {
  if (status === 'GRADED') {
    return (
      <span className="inline-flex items-center text-[11px] font-semibold px-2.5 py-0.5 rounded-full bg-green-100 text-green-700 border border-green-200">
        Graded
      </span>
    );
  }
  if (status === 'SUBMITTED') {
    return (
      <span className="inline-flex items-center text-[11px] font-semibold px-2.5 py-0.5 rounded-full bg-amber-100 text-amber-700 border border-amber-200">
        Submitted
      </span>
    );
  }
  return (
    <span className="inline-flex items-center text-[11px] font-semibold px-2.5 py-0.5 rounded-full bg-red-100 text-red-700 border border-red-200">
      Not Submitted
    </span>
  );
}

// ── Papers Table ──────────────────────────────────────────────────────────────

function PapersTable({ classId, papers }) {
  return (
    <div className="rounded-xl border border-border overflow-hidden">
      <div className="overflow-x-auto">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Paper Name</TableHead>
              <TableHead>Due Date</TableHead>
              <TableHead>Grade</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>View</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {papers.map((paper) => (
              <TableRow key={paper.id}>
                <TableCell className="font-semibold text-gray-900">{paper.paper_name}</TableCell>
                <TableCell className="text-sm text-gray-500">{formatDate(paper.due_date)}</TableCell>
                <TableCell>
                  {paper.submission_status === 'GRADED' && paper.grade ? (
                    <span className="font-semibold text-amber-600">{paper.grade}</span>
                  ) : paper.submission_status === 'SUBMITTED' ? (
                    <span className="text-sm text-gray-400">Pending</span>
                  ) : (
                    <span className="text-sm text-gray-300">—</span>
                  )}
                </TableCell>
                <TableCell>
                  <SubmissionStatusBadge status={paper.submission_status} />
                </TableCell>
                <TableCell>
                  <Link
                    href={`/student/dashboard/classes/${classId}/papers/${paper.id}/submission`}
                    className="w-8 h-8 flex items-center justify-center rounded-lg border border-gray-200 text-gray-500 hover:bg-indigo-50 hover:border-indigo-200 hover:text-indigo-600 transition-colors"
                    title="View paper"
                  >
                    <Eye className="h-4 w-4" />
                  </Link>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  );
}

// ── Locked State ──────────────────────────────────────────────────────────────

function LockedState({ classId, monthLabel, cls, month, year, status }) {
  const { setMonthlyPaymentStatus } = useEnrolledClasses();
  const [payOpen, setPayOpen] = useState(false);
  const [viewSubmissionOpen, setViewSubmissionOpen] = useState(false);

  const latestSubmission = findLatestPaymentSubmission(classId, month, year);

  function handlePaidByCard() {
    setMonthlyPaymentStatus(classId, month, year, {
      status: 'PAID',
      reference_number: `CARD-${Date.now()}`,
      paid_at: new Date().toISOString(),
    });
  }

  function handleSubmittedForReview() {
    setMonthlyPaymentStatus(classId, month, year, {
      status: 'PENDING',
      reference_number: null,
      paid_at: null,
    });
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-[50vh] gap-5 text-center px-4">
      <div className="w-16 h-16 rounded-full bg-gray-100 flex items-center justify-center">
        <Lock className="h-8 w-8 text-gray-400" />
      </div>
      <div className="space-y-2">
        <h2 className="text-base font-semibold text-gray-900">No Content Available</h2>
        <p className="text-sm text-gray-500 max-w-sm leading-relaxed">
          {status === 'PENDING'
            ? `Your ${monthLabel} payment is under review. You will get access once approved.`
            : status === 'REJECTED'
            ? `Your ${monthLabel} payment was rejected. Please resubmit your payment.`
            : `You have not paid for ${monthLabel}. Purchase this month to view these papers.`}
        </p>
      </div>
      <div className="flex items-center gap-3">
        {(status === 'PENDING' || status === 'REJECTED') && latestSubmission && (
          <Button
            variant="outline"
            className="font-semibold h-9 px-5"
            onClick={() => setViewSubmissionOpen(true)}
          >
            View Submission
          </Button>
        )}
        {status !== 'PENDING' && (
          <Button
            className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold h-9 px-5"
            onClick={() => setPayOpen(true)}
          >
            Pay Now
          </Button>
        )}
      </div>
      <Link
        href={`/student/dashboard/classes/${classId}`}
        className="text-sm text-gray-400 hover:text-indigo-600 transition-colors"
      >
        ← Back to Class
      </Link>

      <PayNowDialog
        open={payOpen}
        onOpenChange={setPayOpen}
        cls={cls}
        month={month}
        year={year}
        monthLabel={monthLabel}
        onPaidByCard={handlePaidByCard}
        onSubmittedForReview={handleSubmittedForReview}
      />
      <ViewPaymentSubmissionDialog
        open={viewSubmissionOpen}
        onOpenChange={setViewSubmissionOpen}
        payment={latestSubmission}
      />
    </div>
  );
}

// ── Page ──────────────────────────────────────────────────────────────────────

export default function MonthDetailPage() {
  const { classId, monthYear } = useParams();
  const { classes } = useEnrolledClasses();

  // const cls = classes.find((c) => c.id === classId);
  const cls = classes.find((c) => c.id === Number(classId));
  const { month, year } = parseMonthYearSlug(monthYear ?? "");
  const monthLabel = month > 0 ? formatMonthYear(month, year) : null;

  const parsedMonth = Number(month);
  const parsedYear = Number(year);

  if (!cls || !monthLabel) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
        <p className="text-gray-500 text-sm">Page not found.</p>
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

  const paid = isPaidMonth(cls.monthly_payments, month, year);
  const status = getMonthStatus(cls.monthly_payments, month, year);

  // const monthEntry = (cls.papers_by_month ?? []).find(
  //   (e) => e.month === month && e.year === year,
  // );

  const monthEntry = (cls.papers_by_month ?? []).find(
    (e) => e.month === parsedMonth && e.year === parsedYear,
  );
  const papers = monthEntry?.papers ?? [];

  return (
    <div className="space-y-6">
      {/* Top bar */}
      <div className="bg-white rounded-xl border border-border px-5 py-4 flex items-center gap-4">
        <Link
          href={`/student/dashboard/classes/${classId}`}
          className="inline-flex items-center gap-1.5 text-sm text-gray-500 hover:text-indigo-600 transition-colors shrink-0"
        >
          <ArrowLeft className="h-4 w-4" />
          Back
        </Link>
        <div className="w-px h-5 bg-gray-200 shrink-0" />
        <div className="min-w-0">
          <h1 className="text-base font-bold text-gray-900 leading-tight truncate">
            {cls.class_name} — {monthLabel}
          </h1>
          <p className="text-xs text-gray-400 mt-0.5">
            {cls.teacher_name} · {cls.subject} · {cls.class_year}
          </p>
        </div>
      </div>

      {/* Content */}
      {!paid ? (
        <LockedState classId={classId} monthLabel={monthLabel} cls={cls} month={month} year={year} status={status} />
      ) : papers.length === 0 ? (
        <div className="py-16 text-center text-sm text-gray-400 bg-white rounded-xl border border-border">
          No papers available for {monthLabel}.
        </div>
      ) : (
        <PapersTable classId={classId} papers={papers} />
      )}
    </div>
  );
}
