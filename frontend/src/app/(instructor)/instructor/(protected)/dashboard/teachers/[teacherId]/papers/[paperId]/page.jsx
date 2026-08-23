'use client';

import { useState ,useEffect } from 'react';
import Link from 'next/link';
import { useParams, useRouter } from 'next/navigation';
import { ChevronLeft, RotateCcw, UserCheck } from 'lucide-react';
import { toast } from 'sonner';

import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { getInstructorTeachers, getInstructorPendingRegradeRequests } from '@/lib/api-client';

const MONTH_NAMES = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
const MOCK_NOW = new Date('2026-06-19T18:30:00.000Z');
const SRI_LANKA_OFFSET_MS = 5.5 * 60 * 60 * 1000;

function initials(name) {
  const parts = (name ?? '').trim().split(/\s+/);
  if (parts.length >= 2) return `${parts[0][0]}${parts[parts.length - 1][0]}`.toUpperCase();
  return (parts[0]?.slice(0, 2) || '?').toUpperCase();
}

function formatRelativeDate(value) {
  const date = new Date(value);
  const diffMs = MOCK_NOW.getTime() - date.getTime();
  const diffDays = Math.floor(diffMs / 86400000);
  const diffHours = Math.floor(diffMs / 3600000);

  if (diffDays > 0) return `${diffDays} day${diffDays === 1 ? '' : 's'} ago`;
  if (diffHours > 0) return `${diffHours} hour${diffHours === 1 ? '' : 's'} ago`;
  return 'Today';
}

function formatFullDate(value) {
  const date = new Date(new Date(value).getTime() + SRI_LANKA_OFFSET_MS);
  const month = MONTH_NAMES[date.getUTCMonth()];
  const day = date.getUTCDate();
  const year = date.getUTCFullYear();
  const hour24 = date.getUTCHours();
  const minute = String(date.getUTCMinutes()).padStart(2, '0');
  const period = hour24 >= 12 ? 'PM' : 'AM';
  const hour12 = hour24 % 12 || 12;

  return `${month} ${day}, ${year}, ${hour12}:${minute} ${period}`;
}

function formatGrade(grade) {
  return grade?.replace('/', ' / ') ?? '-';
}

function StudentCell({ submission }) {
  return (
    <div className="flex min-w-48 items-center gap-2.5">
      <Avatar className="h-8 w-8">
        <AvatarFallback className="bg-indigo-50 text-xs font-semibold text-indigo-700">
          {initials(submission.student_name)}
        </AvatarFallback>
      </Avatar>
      <span className="font-medium text-gray-900">{submission.student_name}</span>
    </div>
  );
}

export default function InstructorPaperSubmissionsPage() {
  const { teacherId, paperId } = useParams();
  const router = useRouter();
   const [instructorTeachers, setInstructorTeachers] = useState([]);
   const [regradeRequests, setRegradeRequests] = useState([]);
  const teacher = instructorTeachers.find((item) => item.id === teacherId);
  const paper = teacher?.papers?.find((item) => item.id === paperId);


    useEffect(() => {
      loadInstructorTeachers();
      loadRegradeRequests();
    }, []);



  async function loadInstructorTeachers() {
    try {
      const data = await getInstructorTeachers();
      setInstructorTeachers(data);
    } catch (error) {
      toast.error("Failed to load instructor teachers");
    }
  }

  async function loadRegradeRequests() {
    try {
      const data = await getInstructorPendingRegradeRequests();
      setRegradeRequests(data ?? []);
    } catch (error) {
      console.error("Failed to load regrade requests:", error);
    }
  }


  if (!teacher || !paper) {
    return (
      <div className="mx-auto flex min-h-[50vh] max-w-xl flex-col items-center justify-center gap-4 text-center">
        <h2 className="text-xl font-semibold text-gray-900">Paper not found</h2>
        <Button
          variant="outline"
          nativeButton={false}
          className="border-indigo-200 text-indigo-700 hover:bg-indigo-50"
          render={<Link href={teacher ? `/instructor/dashboard/teachers/${teacher.id}` : '/instructor/dashboard'} />}
        >
          <ChevronLeft className="h-4 w-4" />
          Back
        </Button>
      </div>
    );
  }

  const paperRegradeRequests = regradeRequests.filter((r) => String(r.paper_id) === String(paperId));
  const regradeBySubmissionId = new Map(paperRegradeRequests.map((r) => [String(r.submission_id), r]));

  const pendingSubmissions = paper.submissions.filter((submission) => !submission.graded);
  const regradeRequestedSubmissions = paper.submissions.filter(
    (submission) => submission.graded && regradeBySubmissionId.has(String(submission.id))
  );
  const gradedSubmissions = paper.submissions.filter(
    (submission) => submission.graded && !regradeBySubmissionId.has(String(submission.id))
  );

  return (
    <div className="space-y-5">
      <div className="rounded-2xl border border-border bg-white p-4 shadow-sm">
        <Button
          variant="ghost"
          nativeButton={false}
          className="mb-3 h-8 px-2 text-gray-500 hover:text-gray-900"
          render={<Link href={`/instructor/dashboard/teachers/${teacher.id}`} />}
        >
          <ChevronLeft className="h-4 w-4" />
          Back
        </Button>
        <h2 className="text-xl font-semibold text-gray-900">{paper.paper_name}</h2>
        <p className="mt-1 text-sm text-gray-500">
          {paper.class_name} <span className="text-gray-300">·</span> {paper.month_label}
        </p>
      </div>

      <section className="rounded-2xl border border-border bg-white shadow-sm">
        <div className="border-l-4 border-amber-400 px-4 py-3">
          <h3 className="text-base font-semibold text-gray-900">
            Pending Grading <span className="font-normal text-gray-400">•&nbsp;{pendingSubmissions.length}</span>
          </h3>
        </div>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Student</TableHead>
              <TableHead>Student No.</TableHead>
              <TableHead>Submitted</TableHead>
              <TableHead className="text-right">Action</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {pendingSubmissions.length === 0 ? (
              <TableRow>
                <TableCell colSpan={4} className="py-10 text-center text-sm text-gray-400">
                  All submissions have been graded.
                </TableCell>
              </TableRow>
            ) : (
              pendingSubmissions.map((submission) => (
                <TableRow key={submission.id}>
                  <TableCell>
                    <StudentCell submission={submission} />
                  </TableCell>
                  <TableCell>
                    <Badge variant="outline" className="font-mono text-[11px] text-gray-500">
                      {submission.student_number}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-gray-500">
                    <span title={formatFullDate(submission.submitted_at)}>
                      {formatRelativeDate(submission.submitted_at)}
                    </span>
                  </TableCell>
                  <TableCell className="text-right">
                    <Button
                      size="sm"
                      className="bg-indigo-600 text-white hover:bg-indigo-700"
                      onClick={() => router.push(`/instructor/dashboard/teachers/${teacherId}/papers/${paperId}/grade/${submission.id}`)}
                    >
                      Grade
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </section>

      {regradeRequestedSubmissions.length > 0 && (
        <section className="rounded-2xl border border-border bg-white shadow-sm">
          <div className="border-l-4 border-purple-400 px-4 py-3">
            <h3 className="text-base font-semibold text-gray-900">
              Regrade Requested{' '}
              <span className="font-normal text-gray-400">•&nbsp;{regradeRequestedSubmissions.length}</span>
            </h3>
          </div>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Student</TableHead>
                <TableHead>Student No.</TableHead>
                <TableHead>Previous Grade</TableHead>
                <TableHead>Regrade Remark</TableHead>
                <TableHead>Requested</TableHead>
                <TableHead className="text-right">Action</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {regradeRequestedSubmissions.map((submission) => {
                const regradeRequest = regradeBySubmissionId.get(String(submission.id));
                return (
                  <TableRow key={submission.id}>
                    <TableCell>
                      <StudentCell submission={submission} />
                    </TableCell>
                    <TableCell>
                      <Badge variant="outline" className="font-mono text-[11px] text-gray-500">
                        {submission.student_number}
                      </Badge>
                    </TableCell>
                    <TableCell className="font-semibold text-amber-600">{formatGrade(submission.grade)}</TableCell>
                    <TableCell className="max-w-56 truncate text-gray-600" title={regradeRequest?.reason}>
                      {regradeRequest?.reason || '—'}
                    </TableCell>
                    <TableCell className="text-gray-500">
                      {regradeRequest?.requested_at ? (
                        <span title={formatFullDate(regradeRequest.requested_at)}>
                          {formatRelativeDate(regradeRequest.requested_at)}
                        </span>
                      ) : (
                        '—'
                      )}
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        size="sm"
                        className="gap-1.5 bg-purple-600 text-white hover:bg-purple-700"
                        onClick={() => router.push(`/instructor/dashboard/teachers/${teacherId}/papers/${paperId}/grade/${submission.id}`)}
                      >
                        <RotateCcw className="h-3.5 w-3.5" />
                        Regrade
                      </Button>
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </section>
      )}

      <section className="rounded-2xl border border-border bg-white shadow-sm">
        <div className="border-l-4 border-green-500 px-4 py-3">
          <h3 className="text-base font-semibold text-gray-900">
            Graded <span className="font-normal text-gray-400">•&nbsp;{gradedSubmissions.length}</span>
          </h3>
        </div>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Student</TableHead>
              <TableHead>Student No.</TableHead>
              <TableHead>Grade</TableHead>
              <TableHead>Graded By</TableHead>
              <TableHead>Graded At</TableHead>
              <TableHead className="text-right">Action</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {gradedSubmissions.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} className="py-10 text-center text-sm text-gray-400">
                  No submissions have been graded yet.
                </TableCell>
              </TableRow>
            ) : (
              gradedSubmissions.map((submission) => (
                <TableRow key={submission.id}>
                  <TableCell>
                    <StudentCell submission={submission} />
                  </TableCell>
                  <TableCell>
                    <Badge variant="outline" className="font-mono text-[11px] text-gray-500">
                      {submission.student_number}
                    </Badge>
                  </TableCell>
                  <TableCell className="font-semibold text-amber-600">{formatGrade(submission.grade)}</TableCell>
                  <TableCell className="min-w-48 text-gray-500">
                    <span className="inline-flex items-center gap-1.5">
                      <UserCheck className="h-3.5 w-3.5 text-green-600" />
                      {submission.graded_by}
                    </span>
                  </TableCell>
                  <TableCell className="text-gray-500">
                    <span title={formatFullDate(submission.graded_at)}>
                      {formatRelativeDate(submission.graded_at)}
                    </span>
                  </TableCell>
                  <TableCell className="text-right">
                    <Button
                      variant="outline"
                      size="sm"
                      className="border-indigo-200 text-indigo-700 hover:bg-indigo-50 hover:text-indigo-800"
                      onClick={() => router.push(`/instructor/dashboard/teachers/${teacherId}/papers/${paperId}/grade/${submission.id}`)}
                    >
                      View
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </section>
    </div>
  );
}
