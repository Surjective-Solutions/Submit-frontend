'use client';

import { useRef, useState } from 'react';

export const QUESTION_LETTERS = 'abcdefghijklmnopqrstuvwxyz';

function isValidMarks(value) {
  return value !== '' && Number(value) > 0;
}

function questionsToBuilderState(questions) {
  if (!questions || questions.length === 0) return [];
  const sorted = [...questions].sort((a, b) => a.display_order - b.display_order);
  const order = [];
  const map = new Map();
  let localKey = 0;
  const nextLocalKey = () => `init-${localKey++}`;
  for (const q of sorted) {
    const label = q.parent_label ?? q.question_label;
    if (!map.has(label)) {
      map.set(label, { key: nextLocalKey(), marks: '', subparts: [] });
      order.push(label);
    }
    const entry = map.get(label);
    if (q.parent_label) {
      entry.subparts.push({ key: nextLocalKey(), marks: String(q.max_marks) });
    } else {
      entry.marks = String(q.max_marks);
    }
  }
  return order.map((label) => map.get(label));
}

export function useQuestionBuilder(initialQuestions) {
  const keyRef = useRef(0);

  function makeKey() {
    keyRef.current += 1;
    return keyRef.current;
  }

  const [questions, setQuestions] = useState(() => questionsToBuilderState(initialQuestions));
  const [showErrors, setShowErrors] = useState(false);

  function reset(nextInitial = []) {
    setQuestions(questionsToBuilderState(nextInitial));
    setShowErrors(false);
  }

  function addQuestion() {
    setQuestions((prev) => [...prev, { key: makeKey(), marks: '', subparts: [] }]);
  }

  function removeQuestion(qi) {
    setQuestions((prev) => prev.filter((_, i) => i !== qi));
  }

  function changeQuestionMarks(qi, value) {
    if (value !== '' && !/^\d*$/.test(value)) return;
    setQuestions((prev) => prev.map((q, i) => (i === qi ? { ...q, marks: value } : q)));
  }

  function addSubpart(qi) {
    setQuestions((prev) =>
      prev.map((q, i) =>
        i === qi ? { ...q, marks: '', subparts: [...q.subparts, { key: makeKey(), marks: '' }] } : q
      )
    );
  }

  function removeSubpart(qi, si) {
    setQuestions((prev) =>
      prev.map((q, i) => (i === qi ? { ...q, subparts: q.subparts.filter((_, j) => j !== si) } : q))
    );
  }

  function changeSubpartMarks(qi, si, value) {
    if (value !== '' && !/^\d*$/.test(value)) return;
    setQuestions((prev) =>
      prev.map((q, i) =>
        i === qi
          ? { ...q, subparts: q.subparts.map((sp, j) => (j === si ? { ...sp, marks: value } : sp)) }
          : q
      )
    );
  }

  const totalMarks = questions.reduce((sum, q) => {
    if (q.subparts.length > 0) return sum + q.subparts.reduce((s, sp) => s + (Number(sp.marks) || 0), 0);
    return sum + (Number(q.marks) || 0);
  }, 0);

  function validate() {
    if (questions.length === 0) return 'Add at least one question.';
    for (const q of questions) {
      if (q.subparts.length > 0) {
        if (q.subparts.some((sp) => !isValidMarks(sp.marks))) return 'Enter valid marks for every part.';
      } else if (!isValidMarks(q.marks)) {
        return 'Enter valid marks for every question.';
      }
    }
    return null;
  }

  function buildPayload() {
    const result = [];
    let order = 1;
    questions.forEach((q, qi) => {
      const qNum = qi + 1;
      if (q.subparts.length > 0) {
        q.subparts.forEach((sp, si) => {
          result.push({
            question_label: `Q${qNum}(${QUESTION_LETTERS[si]})`,
            parent_label: `Q${qNum}`,
            max_marks: Number(sp.marks),
            display_order: order++,
          });
        });
      } else {
        result.push({
          question_label: `Q${qNum}`,
          parent_label: null,
          max_marks: Number(q.marks),
          display_order: order++,
        });
      }
    });
    return result;
  }

  function submit() {
    const error = validate();
    if (error) {
      setShowErrors(true);
      return { error };
    }
    return { error: null, payload: buildPayload(), count: questions.length };
  }

  return {
    questions,
    showErrors,
    totalMarks,
    error: showErrors ? validate() : null,
    addQuestion,
    removeQuestion,
    changeQuestionMarks,
    addSubpart,
    removeSubpart,
    changeSubpartMarks,
    submit,
    reset,
  };
}
