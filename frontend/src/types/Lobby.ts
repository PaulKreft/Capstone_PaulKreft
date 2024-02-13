import { Player } from "./Player.ts";

export type Lobby = {
  id: string;
  players: Player[];
  isGameInProgress: boolean;
  isGameOver: boolean;
  difficulty: number;
  winner?: Player;
};
