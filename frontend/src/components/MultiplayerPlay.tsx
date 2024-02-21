import React, { useEffect, useState } from "react";
import { cn } from "../lib/utils.ts";
import { TileConfiguration } from "./TileConfiguration.tsx";
import { addHexColors, getRandomHexColor, subtractHexColors } from "../lib/hexUtils.ts";
import { shuffleArray } from "../lib/shuffleArray.tsx";
import axios from "axios";
import { Difficulty } from "../types/Difficulty.ts";

const WHITE = "#ffffff";
const BLACK = "#000000";

type MultiPlayProps = {
  playerId?: string;
  difficulty: Difficulty;
  gameStartTime: number;
  onSuccess: (time: number) => void;
  streakToWin: number;
};

export const MultiplayerPlay: React.FC<MultiPlayProps> = ({
  playerId,
  difficulty,
  gameStartTime,
  onSuccess,
  streakToWin,
}) => {
  const [colorConfig, setColorConfig] = useState<string[]>(
    Array(4 + difficulty * 2)
      .fill("")
      .map((_value, index) => (index % 2 === 0 ? BLACK : WHITE)),
  );

  const [streak, setStreak] = useState<number>(0);

  const [puzzleStartTime, setPuzzleStartTime] = useState<number>();

  const [isOver, setIsOver] = useState<boolean>(false);

  useEffect(() => {
    shuffleColours();
  }, []);

  useEffect(() => {
    if (streak === streakToWin) {
      onSuccess(Date.now() - gameStartTime);
      setIsOver(true);
    }
  }, [gameStartTime, streak]);

  const handleOverEvent = (result: "lost" | "won"): void => {
    const now: number = Date.now();
    const hasWon: boolean = result === "won";

    if (hasWon) {
      setStreak((s) => s + 1);
    } else {
      setStreak(0);
    }

    axios
      .post("/api/games", {
        userId: playerId,
        type: "",
        difficulty,
        isSuccess: hasWon,
        duration: hasWon && puzzleStartTime ? now - puzzleStartTime : null,
        configuration: colorConfig,
      })
      .then(() => shuffleColours());
  };

  const resetClock = (): void => {
    setPuzzleStartTime(undefined);
  };

  const resetConfig = (): string[] => {
    return colorConfig.map((_tile, index) => (index % 2 === 0 ? BLACK : WHITE));
  };

  const shuffleColours = (): void => {
    resetClock();
    const baseConfig: string[] = resetConfig();

    const newConfig: string[] = [];
    const whiteTiles = baseConfig.filter((color) => color === WHITE);
    const blackTiles = baseConfig.filter((color) => color === BLACK);

    whiteTiles.forEach((tileColor, index) => {
      const randomColor = getRandomHexColor(tileColor);
      newConfig.push(subtractHexColors(tileColor, randomColor));
      newConfig.push(addHexColors(blackTiles[index], randomColor));
    });

    setColorConfig(shuffleArray<string>(newConfig));
    setPuzzleStartTime(Date.now());
  };

  if (isOver) {
    return <div className="flex flex-1 items-center justify-center text-3xl font-extrabold">Determining Winner...</div>;
  }

  return (
    <div className="flex flex-1 flex-col items-center justify-center px-2 pb-32 xs:pb-20 sm:mx-10">
      <TileConfiguration className="mt-6" baseConfig={colorConfig} overEvent={handleOverEvent}>
        <div className={cn("text-xl text-black", streak ? "text-black" : "text-transparent")}>Streak: {streak}</div>
      </TileConfiguration>
    </div>
  );
};
