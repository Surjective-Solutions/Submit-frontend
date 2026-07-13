'use client';

import { Plus, Trash2, X } from 'lucide-react';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { cn } from '@/lib/utils';
import { QUESTION_LETTERS } from '@/hooks/use-question-builder';

function isValidMarks(value) {
  return value !== '' && Number(value) > 0;
}

export default function QuestionStructureBuilder({
  questions,
  showErrors,
  onAddQuestion,
  onRemoveQuestion,
  onQuestionMarksChange,
  onAddSubpart,
  onRemoveSubpart,
  onSubpartMarksChange,
}) {
  return (
    <div className="space-y-2">
      {questions.length === 0 && (
        <p className="text-xs text-gray-400 italic">No questions added yet.</p>
      )}
      {questions.map((q, qi) => {
        const qNum = qi + 1;
        const hasSubparts = q.subparts.length > 0;
        const sum = q.subparts.reduce((s, sp) => s + (Number(sp.marks) || 0), 0);
        const leafInvalid = showErrors && !hasSubparts && !isValidMarks(q.marks);

        return (
          <div key={q.key} className="rounded-lg border border-input p-3 space-y-2">
            <div className="flex items-center justify-between gap-2">
              <span className="text-sm font-semibold text-gray-900">Q{qNum}</span>
              <div className="flex items-center gap-2">
                {hasSubparts ? (
                  <span className="text-xs text-gray-500">{sum} marks</span>
                ) : (
                  <Input
                    type="number"
                    min="1"
                    placeholder="Marks"
                    value={q.marks}
                    onChange={(e) => onQuestionMarksChange(qi, e.target.value)}
                    className={cn('w-20 text-right', leafInvalid && 'border-destructive focus-visible:border-destructive focus-visible:ring-destructive/30')}
                  />
                )}
                <button
                  type="button"
                  onClick={() => onRemoveQuestion(qi)}
                  className="p-1.5 rounded-md hover:bg-red-50"
                >
                  <Trash2 className="h-3.5 w-3.5 text-red-500" />
                </button>
              </div>
            </div>

            {hasSubparts && (
              <div className="ml-2 space-y-2 border-l border-border pl-3">
                {q.subparts.map((sp, si) => {
                  const subInvalid = showErrors && !isValidMarks(sp.marks);
                  return (
                    <div key={sp.key} className="flex items-center justify-between gap-2">
                      <span className="text-xs font-medium text-gray-700">Q{qNum}({QUESTION_LETTERS[si]})</span>
                      <div className="flex items-center gap-2">
                        <Input
                          type="number"
                          min="1"
                          placeholder="Marks"
                          value={sp.marks}
                          onChange={(e) => onSubpartMarksChange(qi, si, e.target.value)}
                          className={cn('w-20 text-right', subInvalid && 'border-destructive focus-visible:border-destructive focus-visible:ring-destructive/30')}
                        />
                        <button
                          type="button"
                          onClick={() => onRemoveSubpart(qi, si)}
                          className="p-1.5 rounded-md hover:bg-gray-100"
                        >
                          <X className="h-3.5 w-3.5 text-gray-400" />
                        </button>
                      </div>
                    </div>
                  );
                })}
              </div>
            )}

            <button
              type="button"
              onClick={() => onAddSubpart(qi)}
              className="text-xs font-medium text-indigo-600 hover:text-indigo-700"
            >
              + Add Part
            </button>
          </div>
        );
      })}

      <Button type="button" variant="outline" size="sm" className="gap-1.5" onClick={onAddQuestion}>
        <Plus className="h-3.5 w-3.5" />
        Add Question
      </Button>
    </div>
  );
}
