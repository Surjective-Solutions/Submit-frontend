'use client';

import { Receipt, ExternalLink, Clock, XCircle } from 'lucide-react';

import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';

function formatFullDate(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleDateString('en-GB', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

function formatLKR(amount) {
  return `LKR ${Number(amount).toLocaleString('en-LK')}`;
}

export default function ViewPaymentSubmissionDialog({ open, onOpenChange, payment }) {
  if (!payment) return null;

  const isPending = payment.status === 'PENDING';
  const headerBg = isPending
    ? 'linear-gradient(135deg, #92400e 0%, #d97706 100%)'
    : 'linear-gradient(135deg, #7f1d1d 0%, #991b1b 100%)';

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        className="sm:max-w-lg p-0 gap-0 overflow-hidden max-h-[90vh]"
        style={{ display: 'flex', flexDirection: 'column' }}
      >
        <div className="px-6 py-5 flex items-center gap-3 shrink-0" style={{ background: headerBg }}>
          <div className="w-9 h-9 rounded-xl bg-white/15 flex items-center justify-center shrink-0">
            <Receipt className="h-5 w-5 text-white" />
          </div>
          <div>
            <DialogTitle className="text-white text-base font-semibold leading-tight m-0">
              {isPending ? 'Payment Under Review' : 'Payment Rejected'}
            </DialogTitle>
            <DialogDescription className="text-white/60 text-xs mt-0.5 m-0">
              Submitted {formatFullDate(payment.submitted_at)}
            </DialogDescription>
          </div>
        </div>

        <div className="flex-1 min-h-0 overflow-y-auto px-6 py-5 space-y-4">
          <div className="rounded-xl border border-gray-200 bg-gray-50 p-4 space-y-1.5 text-sm">
            <div className="flex justify-between">
              <span className="text-gray-500">Amount</span>
              <span className="font-semibold text-gray-900">{formatLKR(payment.amount)}</span>
            </div>
            {payment.bank_account && (
              <div className="flex justify-between gap-4">
                <span className="text-gray-500 shrink-0">Bank Account</span>
                <span className="text-gray-800 text-right">{payment.bank_account}</span>
              </div>
            )}
          </div>

          {payment.receipt_url && (
            <div className="space-y-2">
              <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider">Uploaded Bank Slip</p>
              <div className="rounded-xl border border-gray-200 overflow-hidden bg-gray-50">
                <img
                  src={payment.receipt_url}
                  alt="Bank slip"
                  className="w-full object-contain"
                  style={{ maxHeight: '320px' }}
                />
              </div>
              <a
                href={payment.receipt_url}
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center gap-1 text-xs text-indigo-600 hover:text-indigo-800 hover:underline"
              >
                <ExternalLink className="h-3 w-3" />
                Open Full Size
              </a>
            </div>
          )}

          {isPending ? (
            <div className="rounded-xl border border-amber-200 bg-amber-50 p-4 flex items-center gap-2.5">
              <Clock className="h-5 w-5 text-amber-600 shrink-0" />
              <p className="text-sm text-amber-800">
                Your payment is being reviewed. You&apos;ll get access once it&apos;s approved.
              </p>
            </div>
          ) : (
            <div className="rounded-xl border border-red-200 bg-red-50 p-4 space-y-1.5">
              <div className="flex items-center gap-2">
                <XCircle className="h-5 w-5 text-red-600 shrink-0" />
                <span className="text-sm font-semibold text-red-800">Rejected</span>
              </div>
              {payment.rejection_reason && (
                <p className="text-sm text-red-700 leading-relaxed">{payment.rejection_reason}</p>
              )}
            </div>
          )}
        </div>

        <div className="shrink-0 flex items-center justify-end px-6 py-4 border-t bg-gray-50/60">
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Close
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
