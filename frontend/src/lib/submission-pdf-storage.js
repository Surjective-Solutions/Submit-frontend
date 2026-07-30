// TEMPORARY FRONTEND-ONLY WORKAROUND
// There is no backend endpoint yet to persist or retrieve a student's
// uploaded exam answer PDF, so we stash the actual file (as a Blob) in
// IndexedDB, keyed by paperId. IndexedDB is used instead of
// localStorage/sessionStorage because PDFs are binary and can be too large
// for those APIs. This is local-only: it does not survive a cleared browser,
// a different browser, or a different device.
// Remove this module and its call sites once a real backend endpoint for
// storing/retrieving submitted exam PDFs exists.

const DB_NAME = 'examflow_submissions';
const DB_VERSION = 1;
const STORE_NAME = 'submission_pdfs';

function openDb() {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open(DB_NAME, DB_VERSION);
    request.onupgradeneeded = () => {
      const db = request.result;
      if (!db.objectStoreNames.contains(STORE_NAME)) {
        db.createObjectStore(STORE_NAME);
      }
    };
    request.onsuccess = () => resolve(request.result);
    request.onerror = () => reject(request.error);
  });
}

export async function saveSubmissionPdf(paperId, file) {
  try {
    const db = await openDb();
    await new Promise((resolve, reject) => {
      const tx = db.transaction(STORE_NAME, 'readwrite');
      tx.objectStore(STORE_NAME).put(file, String(paperId));
      tx.oncomplete = resolve;
      tx.onerror = () => reject(tx.error);
    });
    db.close();
  } catch {
    // IndexedDB unavailable — the submission just won't be previewable locally.
  }
}

export async function getSubmissionPdf(paperId) {
  try {
    const db = await openDb();
    const blob = await new Promise((resolve, reject) => {
      const tx = db.transaction(STORE_NAME, 'readonly');
      const req = tx.objectStore(STORE_NAME).get(String(paperId));
      req.onsuccess = () => resolve(req.result ?? null);
      req.onerror = () => reject(req.error);
    });
    db.close();
    return blob;
  } catch {
    return null;
  }
}
