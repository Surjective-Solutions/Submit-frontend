export function getTokenPayload() {
  try {
    const token = localStorage.getItem('token');
    if (!token) return null;
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload;
  } catch {
    return null;
  }
}

export function getUserRole() {
  const payload = getTokenPayload();
  return payload?.role ?? null;
}

export function getUserSeq() {
  const payload = getTokenPayload();
  return payload?.userSeq ?? null;
}

export function getUserIdentifier() {
  const payload = getTokenPayload();
  return payload?.sub ?? null;
}