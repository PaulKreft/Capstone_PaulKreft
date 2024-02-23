export type MultiplayerGame = {
  id?: string;
  playerIds: string[];
  difficulty: number;
  streakToWin: number;
  winnerId: string[];
  loserIds: string[];
  wonInMilliseconds: number;
  totalPlayers: number;
  createdAt?: string;
};
