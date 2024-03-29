import { Player } from "./Player.ts";
import { Difficulty } from "./Difficulty.ts";

export type Lobby = {
  id: string;
  host: Player;
  players: Player[];
  isGameInProgress: boolean;
  isGameOver: boolean;
  difficulty: Difficulty;
  winner?: Player;
  losers: Player[];
  streakToWin: number;
  timeToBeat?: number;
  lastGameStarted?: Date;
  capacity?: number;
};
