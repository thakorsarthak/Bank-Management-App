export interface Transaction {
  type: string;
  amount: number;
  beforeBalance: number;
  afterBalance: number;
  direction: string;
  description: string;
  timestamp: string; // ISO date string
  status: string;
  counterPartyName: string;
}
