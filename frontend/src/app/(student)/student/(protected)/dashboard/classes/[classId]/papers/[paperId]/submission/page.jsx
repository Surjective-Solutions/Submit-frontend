'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { toast } from 'sonner';
import {
  ArrowLeft,
  Award,
  CheckCircle,
  ClipboardEdit,
  Eye,
  FileText,
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogTitle,
} from '@/components/ui/dialog';
import { Textarea } from '@/components/ui/textarea';
import { useEnrolledClasses } from '@/context/EnrolledClassesContext';
import { findPaperInClass } from '@/lib/exam-utils';
import { toMonthYearSlug } from '@/lib/billing-utils';

function formatDate(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleDateString('en-GB', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  });
}

// ── Request Recorrection Dialog ──────────────────────────────────────────────

// TEMPORARY FRONTEND-ONLY WORKAROUND: there is no backend endpoint yet to
// submit recorrection/regrading requests, so this just notes the request
// locally via a toast. Wire this up to a real endpoint once one exists.
function RequestRecorrectionDialog({ open, onOpenChange, paperName }) {
  const [reason, setReason] = useState('');

  function handleClose(next) {
    if (!next) setReason('');
    onOpenChange(next);
  }

  function handleSubmit() {
    if (!reason.trim()) {
      toast.error('Please enter a reason for your recorrection request.');
      return;
    }
    toast.success(
      "Your recorrection request has been noted. Backend processing for this isn't implemented yet.",
    );
    handleClose(false);
  }

  return (
    <Dialog open={open} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-md p-0 gap-0 overflow-hidden">
        <div
          className="px-6 py-5"
          style={{ background: 'linear-gradient(135deg, #3940A0 0%, #5a62ff 100%)' }}
        >
          <DialogTitle className="text-white text-sm font-semibold leading-tight m-0">
            Request Recorrection
          </DialogTitle>
          <p className="text-white/60 text-xs mt-0.5 truncate">{paperName}</p>
        </div>

        <div className="px-6 py-5 space-y-2">
          <label className="text-xs font-semibold text-gray-500 uppercase tracking-wider">
            Reason
          </label>
          <Textarea
            rows={4}
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            placeholder="Explain why you'd like this paper re-checked..."
          />
        </div>

        <div className="flex items-center justify-end gap-2 px-6 py-4 border-t bg-gray-50/60">
          <Button variant="outline" size="sm" onClick={() => handleClose(false)}>
            Cancel
          </Button>
          <Button
            size="sm"
            className="bg-indigo-600 hover:bg-indigo-700 text-white"
            onClick={handleSubmit}
          >
            Submit Request
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
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

// ── Page ──────────────────────────────────────────────────────────────────────

export default function PaperSubmissionPage() {
  const { classId, paperId } = useParams();
  const { classes } = useEnrolledClasses();
  const [recorrectionOpen, setRecorrectionOpen] = useState(false);

  const cls = classes.find((c) => c.id === Number(classId));

  if (!cls) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
        <p className="text-gray-500 text-sm">Class not found.</p>
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

  const paper = findPaperInClass(cls, paperId);

  if (!paper) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
        <p className="text-gray-500 text-sm">Paper not found.</p>
        <Button
          variant="outline"
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

      {/* Recorrection */}
      <div className="rounded-xl border border-border bg-white p-5 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <p className="text-sm font-semibold text-gray-900">Not happy with your grade?</p>
          <p className="text-xs text-gray-500 mt-0.5">
            Request a recorrection if you think this paper needs to be re-checked.
          </p>
        </div>
        <Button
          variant="outline"
          className="gap-2 border-gray-300 text-gray-700 hover:bg-gray-50 shrink-0"
          onClick={() => setRecorrectionOpen(true)}
        >
          <ClipboardEdit className="h-4 w-4" />
          Request Recorrection
        </Button>
      </div>

      <RequestRecorrectionDialog
        open={recorrectionOpen}
        onOpenChange={setRecorrectionOpen}
        paperName={paper.paper_name}
      />
    </div>
  );
}
