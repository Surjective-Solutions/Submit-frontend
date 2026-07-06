'use client';

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Loader2, Landmark } from 'lucide-react';

import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { createBankAccountSchema } from '@/lib/validations/admin';

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

export default function AddBankAccountDialog({ open, onOpenChange, onSuccess }) {
  const [isLoading, setIsLoading] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(createBankAccountSchema),
    defaultValues: {
      accountName: '',
      accountNumber: '',
      bankName: '',
      additionalDetails: '',
    },
  });

  function handleClose() {
    onOpenChange(false);
    reset();
  }

  async function onSubmit(data) {
    setIsLoading(true);
    try {
      onSuccess(data);
      reset();
      onOpenChange(false);
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <Dialog open={open} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-lg p-0 gap-0 overflow-hidden">
        {/* Coloured header strip */}
        <div
          className="px-6 py-5 flex items-center gap-3"
          style={{ background: 'linear-gradient(135deg, #053A34 0%, #0d6b5e 100%)' }}
        >
          <div className="w-9 h-9 rounded-xl bg-white/15 flex items-center justify-center shrink-0">
            <Landmark className="h-5 w-5 text-white" />
          </div>
          <div>
            <DialogTitle className="text-white text-base font-semibold leading-tight m-0">
              Add Bank Account
            </DialogTitle>
            <DialogDescription className="text-white/60 text-xs mt-0.5 m-0">
              Add a new bank account for receiving payments.
            </DialogDescription>
          </div>
        </div>

        {/* Form body */}
        <form
          onSubmit={handleSubmit(onSubmit)}
          noValidate
          className="px-6 py-5 space-y-4 max-h-[70vh] overflow-y-auto"
        >
          <Field label="Account Name" required id="accountName" error={errors.accountName?.message}>
            <Input
              id="accountName"
              placeholder="e.g. SubmitX Education (Pvt) Ltd"
              aria-invalid={!!errors.accountName}
              {...register('accountName')}
            />
          </Field>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <Field label="Account Number" required id="accountNumber" error={errors.accountNumber?.message}>
              <Input
                id="accountNumber"
                placeholder="e.g. 8001234567"
                aria-invalid={!!errors.accountNumber}
                {...register('accountNumber')}
              />
            </Field>
            <Field label="Bank Name" required id="bankName" error={errors.bankName?.message}>
              <Input
                id="bankName"
                placeholder="e.g. Bank of Ceylon"
                aria-invalid={!!errors.bankName}
                {...register('bankName')}
              />
            </Field>
          </div>

          <Field label="Additional Details" id="additionalDetails" error={errors.additionalDetails?.message}>
            <Textarea
              id="additionalDetails"
              rows={2}
              placeholder="e.g. Branch name, SWIFT code (optional)"
              {...register('additionalDetails')}
            />
          </Field>

          {/* Actions */}
          <div className="flex items-center justify-end gap-3 pt-1">
            <Button type="button" variant="outline" onClick={handleClose} disabled={isLoading}>
              Cancel
            </Button>
            <Button
              type="submit"
              className="min-w-[140px] text-white"
              style={{ background: 'linear-gradient(135deg, #053A34 0%, #0d6b5e 100%)' }}
              disabled={isLoading}
            >
              {isLoading && <Loader2 className="h-4 w-4 animate-spin mr-2" />}
              Add Account
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
}
