export type Game = {
  id?: string;
  userId: string;
  type: string;
  difficulty: number;
  isSuccess: boolean;
  duration: number;
  configuration: string[];
  createdAt?: string;
};
