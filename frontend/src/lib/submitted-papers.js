// TEMPORARY FRONTEND-ONLY WORKAROUND
// There is no backend endpoint yet to mark a paper as submitted after the
// exam upload flow completes, so we track submitted paper IDs in
// sessionStorage (matching the storage type used by src/lib/auth.js) and use
// that to override the displayed submission status on the class detail page.
// Remove this module and its call sites once a real backend
// submission-tracking endpoint exists.

const SUBMITTED_PAPERS_KEY = 'submitted_papers';

function readSubmittedPaperIds() {
  try {
    const raw = sessionStorage.getItem(SUBMITTED_PAPERS_KEY);
    if (!raw) return [];
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

export function getSubmittedPaperIds() {
  return readSubmittedPaperIds();
}

export function isPaperSubmitted(paperId, submittedIds) {
  const ids = submittedIds ?? readSubmittedPaperIds();
  return ids.some((id) => String(id) === String(paperId));
}

export function markPaperSubmitted(paperId) {
  try {
    const ids = readSubmittedPaperIds();
    const idStr = String(paperId);
    if (ids.some((id) => String(id) === idStr)) return;
    sessionStorage.setItem(SUBMITTED_PAPERS_KEY, JSON.stringify([...ids, idStr]));
  } catch {
    // sessionStorage unavailable — nothing to persist.
  }
}
