'use client';

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Loader2, CreditCard, Landmark, FileText } from 'lucide-react';
import { toast } from 'sonner';

import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog';
import { Tabs, TabsList, TabsTrigger, TabsContent } from '@/components/ui/tabs';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { cardPaymentSchema, bankTransferPaymentSchema } from '@/lib/validations/student';
import { MOCK_BANK_ACCOUNTS, MOCK_PAYMENTS, MOCK_LOGGED_IN_STUDENT } from '@/lib/mock-data';

function formatLKR(amount) {
  return `LKR ${Number(amount).toLocaleString('en-LK')}`;
}

function Field({ label, required, error, id, children }) {
  return (
    <div className="space-y-1.5">
      <Label htmlFor={id} className={error ? 'text-destructive' : 'text-gray-700'}>
        {label}
        {required && <span className="text-destructive ml-0.5">*</span>}
      </Label>
      {children}
      {error && (
        <p className="text-xs text-destructive" role="alert">
          {error}
        </p>
      )}
    </div>
  );
}

function AmountBanner({ amount, tone }) {
  const toneClass = tone === 'amber' ? 'border-amber-100 bg-amber-50/50' : 'border-indigo-100 bg-indigo-50/50';
  return (
    <div className={`rounded-xl border px-4 py-3 flex items-center justify-between ${toneClass}`}>
      <span className="text-sm text-gray-600">Amount due</span>
      <span className="text-lg font-bold text-gray-900">{formatLKR(amount)}</span>
    </div>
  );
}

function CardPaymentForm({ amount, onSuccess }) {
  const [isLoading, setIsLoading] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(cardPaymentSchema),
    defaultValues: { card_number: '', card_name: '', expiry: '', cvv: '' },
  });

  async function onSubmit() {
    setIsLoading(true);
    try {
      await new Promise((resolve) => setTimeout(resolve, 900));
      onSuccess();
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate className="space-y-4">
      <AmountBanner amount={amount} tone="indigo" />

      <Field label="Card Number" required id="card_number" error={errors.card_number?.message}>
        <Input id="card_number" placeholder="1234 5678 9012 3456" {...register('card_number')} />
      </Field>
      <Field label="Name on Card" required id="card_name" error={errors.card_name?.message}>
        <Input id="card_name" placeholder="AMAL PERERA" {...register('card_name')} />
      </Field>
      <div className="grid grid-cols-2 gap-4">
        <Field label="Expiry (MM/YY)" required id="expiry" error={errors.expiry?.message}>
          <Input id="expiry" placeholder="MM/YY" {...register('expiry')} />
        </Field>
        <Field label="CVV" required id="cvv" error={errors.cvv?.message}>
          <Input id="cvv" type="password" placeholder="123" {...register('cvv')} />
        </Field>
      </div>

      <Button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 text-white" disabled={isLoading}>
        {isLoading && <Loader2 className="h-4 w-4 animate-spin mr-2" />}
        Pay {formatLKR(amount)}
      </Button>
      <p className="text-[11px] text-gray-400 text-center">
        This is a demo payment form — no real card details are processed.
      </p>
    </form>
  );
}

function BankTransferForm({ amount, onSubmitTransfer }) {
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFileName, setSelectedFileName] = useState(null);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(bankTransferPaymentSchema),
    defaultValues: { bank_account_id: MOCK_BANK_ACCOUNTS[0]?.id ?? '' },
  });

  async function onSubmit(data) {
    setIsLoading(true);
    try {
      onSubmitTransfer(data);
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate className="space-y-4">
      <AmountBanner amount={amount} tone="amber" />

      <div className="space-y-2">
        <Label className="text-gray-700">Transfer To</Label>
        <div className="space-y-2">
          {MOCK_BANK_ACCOUNTS.map((acc) => (
            <label
              key={acc.id}
              className="flex items-start gap-2.5 cursor-pointer rounded-lg border border-input px-3 py-2.5 has-[:checked]:border-indigo-400 has-[:checked]:bg-indigo-50 transition-colors"
            >
              <input type="radio" value={acc.id} className="accent-indigo-600 mt-0.5" {...register('bank_account_id')} />
              <div className="min-w-0 flex-1">
                <p className="text-sm font-semibold text-gray-900">{acc.bankName}</p>
                <p className="text-xs text-gray-600">{acc.accountName}</p>
                <p className="font-mono text-xs text-gray-500 mt-0.5">{acc.accountNumber}</p>
                {acc.additionalDetails && (
                  <p className="text-[11px] text-gray-400 mt-0.5">{acc.additionalDetails}</p>
                )}
              </div>
            </label>
          ))}
        </div>
        {errors.bank_account_id && (
          <p className="text-xs text-destructive" role="alert">{errors.bank_account_id.message}</p>
        )}
      </div>

      <Field label="Upload Bank Slip" required id="receipt_file_input" error={errors.receipt_file?.message}>
        <div className="flex items-center gap-3 rounded-lg border border-input px-3 py-2">
          <FileText className="h-4 w-4 text-gray-400 shrink-0" />
          <span className="text-sm text-gray-500 flex-1 truncate min-w-0">
            {selectedFileName ?? 'No file selected'}
          </span>
          <label
            htmlFor="receipt_file_input"
            className="cursor-pointer text-xs font-medium text-indigo-600 hover:text-indigo-700 shrink-0"
          >
            Choose file
          </label>
        </div>
        <input
          id="receipt_file_input"
          type="file"
          accept="image/*,.pdf"
          className="hidden"
          {...register('receipt_file', {
            onChange: (e) => setSelectedFileName(e.target.files?.[0]?.name ?? null),
          })}
        />
      </Field>

      <Button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 text-white" disabled={isLoading}>
        {isLoading && <Loader2 className="h-4 w-4 animate-spin mr-2" />}
        Submit for Review
      </Button>
    </form>
  );
}

export default function PayNowDialog({ open, onOpenChange, cls, month, year, monthLabel, onPaidByCard, onSubmittedForReview }) {
  if (!cls) return null;

  function handleCardSuccess() {
    onPaidByCard();
    toast.success('Payment successful');
    onOpenChange(false);
  }

  function handleBankSubmit(data) {
    const file = data.receipt_file?.[0];
    const receiptUrl = file ? URL.createObjectURL(file) : null;
    const bankAccount = MOCK_BANK_ACCOUNTS.find((a) => a.id === data.bank_account_id);

    const newPayment = {
      id: `pay-new-${Date.now()}`,
      student_id: MOCK_LOGGED_IN_STUDENT.id,
      student_name: `${MOCK_LOGGED_IN_STUDENT.first_name} ${MOCK_LOGGED_IN_STUDENT.last_name}`,
      student_number: MOCK_LOGGED_IN_STUDENT.student_number,
      teacher_name: cls.teacher_name,
      class_name: cls.class_name,
      class_id: cls.id,
      month,
      year,
      amount: cls.monthly_fee,
      receipt_url: receiptUrl,
      bank_account: bankAccount ? `${bankAccount.bankName} — ${bankAccount.accountNumber}` : null,
      status: 'PENDING',
      reference_number: null,
      rejection_reason: null,
      submitted_at: new Date().toISOString(),
      reviewed_at: null,
      reviewed_by: null,
    };
    MOCK_PAYMENTS.unshift(newPayment);

    onSubmittedForReview();
    toast.success('Payment submitted for review');
    onOpenChange(false);
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        className="sm:max-w-lg p-0 gap-0 overflow-hidden max-h-[90vh]"
        style={{ display: 'flex', flexDirection: 'column' }}
      >
        <div
          className="px-6 py-5 flex items-center gap-3 shrink-0"
          style={{ background: 'linear-gradient(135deg, #3940A0 0%, #5a62ff 100%)' }}
        >
          <div className="w-9 h-9 rounded-xl bg-white/15 flex items-center justify-center shrink-0">
            <CreditCard className="h-5 w-5 text-white" />
          </div>
          <div className="min-w-0 flex-1">
            <DialogTitle className="text-white text-base font-semibold leading-tight m-0">Pay Now</DialogTitle>
            <DialogDescription className="text-white/60 text-xs mt-0.5 m-0 truncate">
              {cls.class_name} · {monthLabel}
            </DialogDescription>
          </div>
        </div>

        <div className="flex-1 min-h-0 overflow-y-auto px-6 py-5">
          <Tabs defaultValue="card">
            <TabsList className="h-10 w-full mb-4">
              <TabsTrigger value="card" className="flex-1 gap-1.5">
                <CreditCard className="h-3.5 w-3.5" />
                Pay by Card
              </TabsTrigger>
              <TabsTrigger value="bank" className="flex-1 gap-1.5">
                <Landmark className="h-3.5 w-3.5" />
                Bank Transfer
              </TabsTrigger>
            </TabsList>

            <TabsContent value="card">
              <CardPaymentForm amount={cls.monthly_fee} onSuccess={handleCardSuccess} />
            </TabsContent>
            <TabsContent value="bank">
              <BankTransferForm amount={cls.monthly_fee} onSubmitTransfer={handleBankSubmit} />
            </TabsContent>
          </Tabs>
        </div>
      </DialogContent>
    </Dialog>
  );
}
