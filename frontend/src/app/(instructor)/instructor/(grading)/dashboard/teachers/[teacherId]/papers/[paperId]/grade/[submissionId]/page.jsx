'use client';

import { useMemo, useState } from 'react';
import Link from 'next/link';
import { useParams, useRouter } from 'next/navigation';
import { ChevronLeft, FileText, PenLine } from 'lucide-react';
import { toast } from 'sonner';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip';
import { cn } from '@/lib/utils';
import { MOCK_INSTRUCTOR_TEACHERS, MOCK_LOGGED_IN_INSTRUCTOR } from '@/lib/mock-data';

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

function applySubmissionGrade(submission, patch) {
  Object.assign(submission, patch);
}

function getGrade(score, totalMarks) {
  const percentage = (score / totalMarks) * 100;
  if (percentage >= 75) return { grade: 'A', color: 'green' };
  if (percentage >= 65) return { grade: 'B', color: 'blue' };
  if (percentage >= 50) return { grade: 'C', color: 'amber' };
  if (percentage >= 35) return { grade: 'S', color: 'orange' };
  return { grade: 'F', color: 'red' };
}

function QuestionRow({
  question,
  value,
  comment,
  commentOpen,
  exceeded,
  showEmptyError,
  onMarksChange,
  onCommentChange,
  onToggleComment,
}) {
  const invalid = exceeded || showEmptyError;

  return (
    <div className="space-y-1.5">
      <div className="flex items-center justify-between gap-2">
        <span className="text-sm font-semibold text-gray-900">{question.question_label}</span>
        <div className="flex items-center gap-1.5">
          <span className="text-xs text-gray-400">/ {question.max_marks}</span>
          <Tooltip open={exceeded}>
            <TooltipTrigger
              render={
                <Input
                  type="number"
                  min={0}
                  max={question.max_marks}
                  placeholder="0"
                  value={value}
                  onChange={(e) => onMarksChange(question, e.target.value)}
                  className={cn(
                    'w-20 text-right',
                    invalid && 'border-red-500 focus-visible:border-red-500 focus-visible:ring-red-500/30'
                  )}
                />
              }
            />
            <TooltipContent>Maximum {question.max_marks} marks</TooltipContent>
          </Tooltip>
        </div>
      </div>
      {commentOpen ? (
        <Textarea
          rows={2}
          placeholder="Add comment (optional)"
          value={comment}
          onChange={(e) => onCommentChange(question, e.target.value)}
          className="text-xs"
        />
      ) : (
        <button
          type="button"
          onClick={() => onToggleComment(question)}
          className="text-xs text-indigo-600 hover:underline"
        >
          Add comment
        </button>
      )}
    </div>
  );
}

export default function InstructorGradeSubmissionPage() {
  const { teacherId, paperId, submissionId } = useParams();
  const router = useRouter();

  const teacher = MOCK_INSTRUCTOR_TEACHERS.find((t) => t.id === teacherId);
  const paper = teacher?.papers?.find((p) => p.id === paperId);
  const submission = paper?.submissions?.find((s) => s.id === submissionId);

  const questions = useMemo(
    () => (paper?.questions ? [...paper.questions].sort((a, b) => a.display_order - b.display_order) : []),
    [paper]
  );

  const groups = useMemo(() => {
    const list = [];
    const map = new Map();
    for (const q of questions) {
      const key = q.parent_label ?? q.question_label;
      if (!map.has(key)) {
        const group = { key, questions: [] };
        map.set(key, group);
        list.push(group);
      }
      map.get(key).questions.push(q);
    }
    return list;
  }, [questions]);

  const [marks, setMarks] = useState(() => {
    const initial = {};
    (submission?.awarded_marks ?? []).forEach((a) => {
      initial[a.question_id] = a.marks_awarded != null ? String(a.marks_awarded) : '';
    });
    return initial;
  });
  const [comments, setComments] = useState(() => {
    const initial = {};
    (submission?.awarded_marks ?? []).forEach((a) => {
      if (a.comment) initial[a.question_id] = a.comment;
    });
    return initial;
  });
  const [commentOpen, setCommentOpen] = useState(() => {
    const initial = {};
    (submission?.awarded_marks ?? []).forEach((a) => {
      if (a.comment) initial[a.question_id] = true;
    });
    return initial;
  });
  const [attemptedSubmit, setAttemptedSubmit] = useState(false);

  const totalMax = questions.reduce((sum, q) => sum + q.max_marks, 0);
  const totalAwarded = questions.reduce((sum, q) => {
    const value = Number(marks[q.id]);
    return sum + (Number.isFinite(value) ? value : 0);
  }, 0);
  const gradeInfo = getGrade(totalAwarded, totalMax || 1);

  function isExceeded(q) {
    const value = marks[q.id];
    return value !== undefined && value !== '' && Number(value) > q.max_marks;
  }

  function isEmpty(q) {
    return marks[q.id] === undefined || marks[q.id] === '';
  }

  function handleMarksChange(q, rawValue) {
    if (rawValue !== '' && !/^\d*$/.test(rawValue)) return;
    setMarks((prev) => ({ ...prev, [q.id]: rawValue }));
  }

  function handleCommentChange(q, value) {
    setComments((prev) => ({ ...prev, [q.id]: value }));
  }

  function toggleComment(q) {
    setCommentOpen((prev) => ({ ...prev, [q.id]: !prev[q.id] }));
  }

  function handleSubmit() {
    const hasExceeded = questions.some((q) => isExceeded(q));
    const hasEmpty = questions.some((q) => isEmpty(q));
    if (hasExceeded || hasEmpty) {
      setAttemptedSubmit(true);
      toast.error('Please fix the errors before submitting');
      return;
    }

    const instructorName = MOCK_LOGGED_IN_INSTRUCTOR
      ? `Instructor ${MOCK_LOGGED_IN_INSTRUCTOR.first_name} ${MOCK_LOGGED_IN_INSTRUCTOR.last_name}`
      : 'Instructor';

    applySubmissionGrade(submission, {
      graded: true,
      graded_by: instructorName,
      graded_at: new Date().toISOString(),
      grade: `${totalAwarded}/${totalMax}`,
      awarded_marks: questions.map((q) => ({
        question_id: q.id,
        marks_awarded: Number(marks[q.id]),
        comment: comments[q.id] || null,
      })),
    });

    toast.success('Grades submitted successfully');
    router.push(`/instructor/dashboard/teachers/${teacherId}/papers/${paperId}`);
  }

  if (!teacher || !paper || !submission) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center gap-4 text-center">
        <h2 className="text-xl font-semibold text-gray-900">Submission not found</h2>
        <Button
          variant="outline"
          nativeButton={false}
          className="border-indigo-200 text-indigo-700 hover:bg-indigo-50"
          render={<Link href={teacher ? `/instructor/dashboard/teachers/${teacherId}` : '/instructor/dashboard'} />}
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
          render={<Link href={`/instructor/dashboard/teachers/${teacherId}/papers/${paperId}`} />}
        >
          <ChevronLeft className="h-4 w-4" />
          <span className="hidden sm:inline">Back to Submissions</span>
        </Button>

        <div className="hidden min-w-0 flex-1 items-baseline justify-center gap-2 overflow-hidden text-center sm:flex">
          <span className="truncate font-semibold text-gray-900">{paper.paper_name}</span>
          <span className="text-gray-300">·</span>
          <span className="truncate text-sm text-gray-500">{submission.student_name}</span>
        </div>

        <div className="flex shrink-0 items-center gap-3">
          <span
            className={cn(
              'hidden text-sm font-semibold whitespace-nowrap sm:inline',
              GRADE_TEXT_COLOR[gradeInfo.color]
            )}
          >
            Total: {totalAwarded} / {totalMax} ({gradeInfo.grade})
          </span>
          <Button size="sm" className="bg-indigo-600 text-white hover:bg-indigo-700" onClick={handleSubmit}>
            Submit Grades
          </Button>
        </div>
      </div>

      {/* Two panel split */}
      <div className="flex flex-1 flex-col overflow-hidden md:flex-row">
        {/* PDF viewer */}
        <div className="h-[45vh] shrink-0 overflow-hidden border-b border-border bg-gray-100 md:h-[calc(100vh-3rem)] md:flex-1 md:border-b-0">
          {submission.file_url ? (
            <iframe src={submission.file_url} title="Student submission" className="h-full w-full border-0" />
          ) : (
            <div className="flex h-full flex-col items-center justify-center gap-2 px-6 text-center">
              <FileText className="h-16 w-16 text-gray-300" />
              <p className="text-sm font-medium text-gray-500">Student submission would appear here</p>
              <p className="text-xs text-gray-400">PDF viewer renders the uploaded answer sheet</p>
            </div>
          )}
        </div>

        {/* Marks panel */}
        <div className="flex h-[calc(55vh-3rem)] flex-col bg-white md:h-[calc(100vh-3rem)] md:w-[360px] md:shrink-0 md:border-l md:border-border">
          <div className="shrink-0 border-b border-border px-4 py-3">
            <div className="flex items-center gap-1.5">
              <PenLine className="h-4 w-4 text-indigo-600" />
              <h3 className="text-sm font-semibold text-gray-900">Mark Entry</h3>
            </div>
            <p className="mt-0.5 text-xs text-gray-500">
              {submission.student_name} <span className="text-gray-300">·</span> {submission.student_number}
            </p>
          </div>

          <ScrollArea className="min-h-0 flex-1">
            <div className="space-y-4 p-4">
              {groups.map((group) => {
                if (group.questions.length > 1) {
                  const groupMax = group.questions.reduce((s, q) => s + q.max_marks, 0);
                  return (
                    <div key={group.key} className="space-y-3">
                      <div className="-mx-1 rounded-md bg-gray-50 px-3 py-1.5">
                        <span className="text-sm font-semibold text-gray-600">{group.key}</span>
                        <span className="ml-1.5 text-xs text-gray-400">/ {groupMax}</span>
                      </div>
                      <div className="ml-2 space-y-3 border-l border-border pl-3">
                        {group.questions.map((q) => (
                          <QuestionRow
                            key={q.id}
                            question={q}
                            value={marks[q.id] ?? ''}
                            comment={comments[q.id] ?? ''}
                            commentOpen={!!commentOpen[q.id]}
                            exceeded={isExceeded(q)}
                            showEmptyError={attemptedSubmit && isEmpty(q)}
                            onMarksChange={handleMarksChange}
                            onCommentChange={handleCommentChange}
                            onToggleComment={toggleComment}
                          />
                        ))}
                      </div>
                    </div>
                  );
                }
                const q = group.questions[0];
                return (
                  <QuestionRow
                    key={q.id}
                    question={q}
                    value={marks[q.id] ?? ''}
                    comment={comments[q.id] ?? ''}
                    commentOpen={!!commentOpen[q.id]}
                    exceeded={isExceeded(q)}
                    showEmptyError={attemptedSubmit && isEmpty(q)}
                    onMarksChange={handleMarksChange}
                    onCommentChange={handleCommentChange}
                    onToggleComment={toggleComment}
                  />
                );
              })}
            </div>
          </ScrollArea>

          <div className="shrink-0 space-y-2 border-t border-border p-4">
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">
                Total: <span className="font-semibold text-gray-900">{totalAwarded} / {totalMax}</span>
              </span>
              <span className={cn('rounded-full px-2 py-0.5 text-xs font-bold', GRADE_BADGE_COLOR[gradeInfo.color])}>
                {gradeInfo.grade}
              </span>
            </div>
            <Button className="w-full bg-indigo-600 text-white hover:bg-indigo-700" onClick={handleSubmit}>
              Submit Grades
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
