import {ScoreMap} from "./ScoreMap.ts";

export type Statistics = {
  longestWinningStreak: ScoreMap;
  longestLosingStreak: ScoreMap;
  gamesPlayed: ScoreMap;
  gamesWon: ScoreMap;
  averageTime: ScoreMap;
  fastestSolve: ScoreMap;
};
