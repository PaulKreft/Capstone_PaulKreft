import { Player } from "./Player.ts";
import { Difficulty } from "./Difficulty.ts";
import { Chat } from "./Chat.ts";

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
  chat: Chat;
};
