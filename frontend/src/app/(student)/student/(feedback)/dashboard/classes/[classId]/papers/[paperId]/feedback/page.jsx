'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { toast } from 'sonner';
import { ChevronLeft, ClipboardEdit, FileText, MessageSquareText } from 'lucide-react';

import { Button } from '@/components/ui/button';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Dialog, DialogContent, DialogTitle } from '@/components/ui/dialog';
import { Textarea } from '@/components/ui/textarea';
import { cn } from '@/lib/utils';
import { useEnrolledClasses } from '@/context/EnrolledClassesContext';
import { findPaperInClass } from '@/lib/exam-utils';
import { getGradeDetailsForPaper } from '@/lib/api-client';

const GRADE_TEXT_COLOR = {
  green: 'text-green-600',
  blue: 'text-blue-600',
  amber: 'text-amber-600',
  orange: 'text-orange-500',
  red: 'text-red-600',
};

const GRADE_BADGE_COLOR = {
  green: 'bg-green-100 text-green-700',
  blue: 'bg-blue-100 text-blue-700',
  amber: 'bg-amber-100 text-amber-700',
  orange: 'bg-orange-100 text-orange-700',
  red: 'bg-red-100 text-red-700',
};

function getGrade(score, totalMarks) {
  const percentage = (score / totalMarks) * 100;
  if (percentage >= 75) return { grade: 'A', color: 'green' };
  if (percentage >= 65) return { grade: 'B', color: 'blue' };
  if (percentage >= 50) return { grade: 'C', color: 'amber' };
  if (percentage >= 35) return { grade: 'S', color: 'orange' };
  return { grade: 'F', color: 'red' };
}

function formatQuestionLabel(questionId) {
  const [parentId, subId] = String(questionId).split('-');
  if (!subId) return `Q${parentId}`;
  const subLetter = /^\d+$/.test(subId)
    ? String.fromCharCode(96 + Number(subId))
    : subId;
  return `Q${parentId} (${subLetter})`;
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

function FeedbackRow({ detail }) {
  return (
    <div className="space-y-1.5">
      <div className="flex items-center justify-between gap-2">
        <span className="text-sm font-semibold text-gray-900">
          {formatQuestionLabel(detail.question_id)}
        </span>
        <span className="text-sm font-semibold text-gray-900">
          {detail.marks_awarded}
          <span className="text-xs font-normal text-gray-400"> / {detail.max_marks}</span>
        </span>
      </div>
      <div className="rounded-md bg-gray-50 px-3 py-2 text-xs text-gray-600">
        {detail.comment ? detail.comment : <span className="text-gray-400">No comment</span>}
      </div>
    </div>
  );
}

export default function StudentFeedbackPage() {
  const { classId, paperId } = useParams();
  const { classes } = useEnrolledClasses();
  const [gradeDetails, setGradeDetails] = useState([]);
  const [loading, setLoading] = useState(true);
  const [recorrectionOpen, setRecorrectionOpen] = useState(false);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        setLoading(true);
        const data = await getGradeDetailsForPaper(paperId);
        if (!cancelled) setGradeDetails(data ?? []);
      } catch {
        if (!cancelled) toast.error('Failed to load feedback.');
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [paperId]);

  const cls = classes.find((c) => c.id === Number(classId));
  const paper = findPaperInClass(cls, paperId);
  console.log('feedback page paper.submission_url:', paper?.submission_url);

  const totalMax = gradeDetails.reduce((sum, d) => sum + (d.max_marks ?? 0), 0);
  const totalAwarded = gradeDetails.reduce((sum, d) => sum + (d.marks_awarded ?? 0), 0);
  const gradeInfo = getGrade(totalAwarded, totalMax || 1);

  if (!cls || !paper) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center gap-4 text-center">
        <h2 className="text-xl font-semibold text-gray-900">Feedback not found</h2>
        <Button
          variant="outline"
          nativeButton={false}
          className="border-indigo-200 text-indigo-700 hover:bg-indigo-50"
          render={<Link href={cls ? `/student/dashboard/classes/${classId}` : '/student/dashboard'} />}
        >
          <ChevronLeft className="h-4 w-4" />
          Back
        </Button>
      </div>
    );
  }

  return (
    <div className="flex h-screen flex-col overflow-hidden bg-gray-50">
      {/* Minimal top bar */}
      <div className="flex h-12 shrink-0 items-center justify-between gap-3 border-b border-border bg-white px-4">
        <Button
          variant="ghost"
          size="sm"
          nativeButton={false}
          className="shrink-0 gap-1 text-gray-500 hover:text-gray-900"
          render={<Link href={`/student/dashboard/classes/${classId}/papers/${paperId}/submission`} />}
        >
          <ChevronLeft className="h-4 w-4" />
          <span className="hidden sm:inline">Back to Submission</span>
        </Button>

        <div className="hidden min-w-0 flex-1 items-baseline justify-center gap-2 overflow-hidden text-center sm:flex">
          <span className="truncate font-semibold text-gray-900">{paper.paper_name}</span>
          <span className="text-gray-300">·</span>
          <span className="truncate text-sm text-gray-500">{cls.class_name}</span>
        </div>

        <div className="flex shrink-0 items-center gap-3">
          {!loading && gradeDetails.length > 0 && (
            <span
              className={cn(
                'hidden text-sm font-semibold whitespace-nowrap sm:inline',
                GRADE_TEXT_COLOR[gradeInfo.color]
              )}
            >
              Total: {totalAwarded} / {totalMax} ({gradeInfo.grade})
            </span>
          )}
        </div>
      </div>

      {/* Two panel split */}
      <div className="flex flex-1 flex-col overflow-hidden md:flex-row">
        {/* PDF viewer */}
        <div className="h-[45vh] shrink-0 overflow-hidden border-b border-border bg-gray-100 md:h-[calc(100vh-3rem)] md:flex-1 md:border-b-0">
          {paper.submission_url ? (
            <iframe src={paper.submission_url} title="Your submission" className="h-full w-full border-0" />
          ) : (
            <div className="flex h-full flex-col items-center justify-center gap-2 px-6 text-center">
              <FileText className="h-16 w-16 text-gray-300" />
              <p className="text-sm font-medium text-gray-500">Your submission would appear here</p>
              <p className="text-xs text-gray-400">PDF viewer renders the uploaded answer sheet</p>
            </div>
          )}
        </div>

        {/* Feedback panel */}
        <div className="flex h-[calc(55vh-3rem)] flex-col bg-white md:h-[calc(100vh-3rem)] md:w-[360px] md:shrink-0 md:border-l md:border-border">
          <div className="shrink-0 border-b border-border px-4 py-3">
            <div className="flex items-center gap-1.5">
              <MessageSquareText className="h-4 w-4 text-indigo-600" />
              <h3 className="text-sm font-semibold text-gray-900">Feedback</h3>
            </div>
            <p className="mt-0.5 text-xs text-gray-500">{cls.class_name}</p>
          </div>

          <ScrollArea className="min-h-0 flex-1">
            <div className="space-y-4 p-4">
              {loading ? (
                <p className="text-sm text-gray-400">Loading feedback...</p>
              ) : gradeDetails.length === 0 ? (
                <p className="text-sm text-gray-400">No feedback available yet.</p>
              ) : (
                gradeDetails.map((detail) => <FeedbackRow key={detail.question_id} detail={detail} />)
              )}

              <div className="rounded-lg border border-gray-200 bg-white p-3 space-y-2">
                <div>
                  <p className="text-xs font-semibold text-gray-900">Not happy with your grade?</p>
                  <p className="mt-0.5 text-[11px] text-gray-500">
                    Request a recorrection if you think this paper needs to be re-checked.
                  </p>
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  className="w-full gap-2 border-gray-300 text-gray-700 hover:bg-gray-50"
                  onClick={() => setRecorrectionOpen(true)}
                >
                  <ClipboardEdit className="h-3.5 w-3.5" />
                  Request Recorrection
                </Button>
              </div>
            </div>
          </ScrollArea>

          <RequestRecorrectionDialog
            open={recorrectionOpen}
            onOpenChange={setRecorrectionOpen}
            paperName={paper.paper_name}
          />

          {!loading && gradeDetails.length > 0 && (
            <div className="shrink-0 space-y-2 border-t border-border p-4">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-500">
                  Total: <span className="font-semibold text-gray-900">{totalAwarded} / {totalMax}</span>
                </span>
                <span className={cn('rounded-full px-2 py-0.5 text-xs font-bold', GRADE_BADGE_COLOR[gradeInfo.color])}>
                  {gradeInfo.grade}
                </span>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
