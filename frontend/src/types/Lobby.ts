import { Player } from "./Player.ts";
import {Difficulty} from "./Difficulty.ts";

export type Lobby = {
  id: string;
  players: Player[];
  isGameInProgress: boolean;
  isGameOver: boolean;
  difficulty: Difficulty;
  winner?: Player;
  losers: Player[];
  timeToBeat?: number;
  lastGameStarted?: string;
};
