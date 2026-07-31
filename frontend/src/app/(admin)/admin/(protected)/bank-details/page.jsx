'use client';

import { useState, useEffect } from 'react';
import { Plus, Pencil, Trash2, Landmark } from 'lucide-react';
import { toast } from 'sonner';

import { Button } from '@/components/ui/button';
import {
  Tooltip,
  TooltipTrigger,
  TooltipContent,
} from '@/components/ui/tooltip';
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from '@/components/ui/table';
import DeleteConfirmDialog from '@/components/admin/DeleteConfirmDialog';
import AddBankAccountDialog from '@/components/admin/AddBankAccountDialog';
import EditBankAccountDialog from '@/components/admin/EditBankAccountDialog';
import { getBankAccounts, createBankAccount, updateBankAccount, deleteBankAccount } from '@/lib/api-client';

export default function BankDetailsPage() {
  const [accounts, setAccounts] = useState([]);
  const [addDialogOpen, setAddDialogOpen] = useState(false);
  const [editAccount, setEditAccount] = useState(null);
  const [deleteAccount, setDeleteAccount] = useState(null);

  useEffect(() => {
    loadAccounts();
  }, []);

  async function loadAccounts() {
    try {
      const data = await getBankAccounts();
      setAccounts(data);
    } catch {
      toast.error('Failed to load bank accounts');
    }
  }

  async function handleAdd(data) {
    try {
      await createBankAccount({
        displayName: data.bankName,
        accountName: data.accountName,
        accountNumber: data.accountNumber,
        additionalDetails: data.additionalDetails || '',
      });
      toast.success('Bank account added successfully');
      setAddDialogOpen(false);
      loadAccounts();
    } catch {
      toast.error('Failed to add bank account');
    }
  }

  async function handleEditSave(data) {
    try {
      await updateBankAccount(editAccount.id, {
        displayName: data.bankName,
        accountName: data.accountName,
        accountNumber: data.accountNumber,
        additionalDetails: data.additionalDetails || '',
      });
      toast.success('Bank account updated successfully');
      setEditAccount(null);
      loadAccounts();
    } catch {
      toast.error('Failed to update bank account');
    }
  }

  async function handleDelete() {
    try {
      await deleteBankAccount(deleteAccount.id);
      toast.success('Bank account deleted');
      setDeleteAccount(null);
      loadAccounts();
    } catch {
      toast.error('Failed to delete bank account');
    }
  }

  return (
    <div className="space-y-5">
      {/* Page header */}
      <div className="flex items-center justify-between gap-4 flex-wrap">
        <div className="flex items-center gap-2.5">
          <h2 className="text-xl font-semibold text-gray-900">Bank Details</h2>
          <span className="text-sm text-gray-400">•&nbsp;{accounts.length} total</span>
        </div>
        <Button
          className="gap-2 text-white shrink-0"
          style={{ backgroundColor: '#3940A0' }}
          onClick={() => setAddDialogOpen(true)}
        >
          <Plus className="h-4 w-4" />
          Add Bank Account
        </Button>
      </div>

      {/* Table */}
      <div className="rounded-xl border border-border bg-white shadow-sm overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="odd:bg-gray-50 even:bg-gray-50 hover:bg-gray-50">
              <TableHead className="w-10 pl-4" />
              <TableHead>Account Name</TableHead>
              <TableHead>Account Number</TableHead>
              <TableHead>Bank Name</TableHead>
              <TableHead>Additional Details</TableHead>
              <TableHead className="pr-4 text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {accounts.length === 0 ? (
              <TableRow className="odd:bg-white even:bg-white hover:bg-white">
                <TableCell colSpan={6} className="py-14 text-center text-gray-400 text-sm">
                  No bank accounts yet. Add your first bank account to get started.
                </TableCell>
              </TableRow>
            ) : (
              accounts.map((account) => (
                <TableRow key={account.id}>
                  <TableCell className="pl-4">
                    <div
                      className="w-8 h-8 rounded-full flex items-center justify-center shrink-0"
                      style={{ background: 'linear-gradient(135deg, #053A34 0%, #0d6b5e 100%)' }}
                      aria-hidden="true"
                    >
                      <Landmark className="h-3.5 w-3.5 text-white" />
                    </div>
                  </TableCell>
                  <TableCell>
                    <span className="font-medium text-gray-900 whitespace-nowrap">
                      {account.accountName}
                    </span>
                  </TableCell>
                  <TableCell>
                    <span className="font-mono text-[11px] px-1.5 py-0.5 rounded bg-gray-100 text-gray-500 whitespace-nowrap">
                      {account.accountNumber}
                    </span>
                  </TableCell>
                  <TableCell>
                    <span className="text-gray-700 text-sm whitespace-nowrap">{account.bankName}</span>
                  </TableCell>
                  <TableCell>
                    <span className="text-gray-500 text-sm max-w-[240px] truncate block">
                      {account.additionalDetails ?? '—'}
                    </span>
                  </TableCell>
                  <TableCell className="pr-4 text-right">
                    <div className="flex items-center justify-end gap-0.5">
                      <Tooltip>
                        <TooltipTrigger
                          onClick={() => setEditAccount(account)}
                          className="inline-flex h-7 w-7 items-center justify-center rounded-md text-gray-400 hover:text-gray-700 hover:bg-gray-100 transition-colors"
                        >
                          <Pencil className="h-3.5 w-3.5" />
                          <span className="sr-only">Edit</span>
                        </TooltipTrigger>
                        <TooltipContent>Edit</TooltipContent>
                      </Tooltip>
                      <Tooltip>
                        <TooltipTrigger
                          onClick={() => setDeleteAccount(account)}
                          className="inline-flex h-7 w-7 items-center justify-center rounded-md text-gray-400 hover:text-red-600 hover:bg-red-50 transition-colors"
                        >
                          <Trash2 className="h-3.5 w-3.5" />
                          <span className="sr-only">Delete</span>
                        </TooltipTrigger>
                        <TooltipContent>Delete</TooltipContent>
                      </Tooltip>
                    </div>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      {/* Dialogs */}
      <AddBankAccountDialog
        open={addDialogOpen}
        onOpenChange={setAddDialogOpen}
        onSuccess={handleAdd}
      />
      <EditBankAccountDialog
        open={!!editAccount}
        onOpenChange={(o) => !o && setEditAccount(null)}
        account={editAccount}
        onSave={handleEditSave}
      />
      <DeleteConfirmDialog
        open={!!deleteAccount}
        onOpenChange={(o) => !o && setDeleteAccount(null)}
        name={deleteAccount?.accountName}
        onConfirm={handleDelete}
      />
    </div>
  );
}