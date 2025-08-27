export interface Claim {
  id?: number;
  userId?: number;

  lostItemId?:  number;
  foundItemId?: number;

  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
  claimDate?: string;               // ✅ now included

  reporterQuestion?: string;
  loserAnswer?:     string;
  verifiedByReporter?:         boolean;
  ownerGivesContactPermission?: boolean;

  /* hydrated objects when backend expands IDs */
  lostItem?:  { id: number; itemName: string };
  foundItem?: { id: number; itemName: string };
}
