import PaymentsSection from '@/components/admin/PaymentsSection';

export default function AdminPaymentsPage() {
  return (
    <div className="space-y-5">
      <div>
        <h2 className="text-xl font-semibold text-gray-900">Payments</h2>
        <p className="text-sm text-gray-500 mt-0.5">Review and manage student payment submissions</p>
      </div>
      <PaymentsSection />
    </div>
  );
}
