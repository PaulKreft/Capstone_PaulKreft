import React, { useEffect, useState } from "react";
import { cn } from "../lib/utils.ts";
import { StopWatch } from "./StopWatch.tsx";
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
  timeToBeat?: number;
  gameStartTime: number;
  onLose: () => void;
  onSuccess: (time: number) => void;
};

export const MultiPlay: React.FC<MultiPlayProps> = ({
  playerId,
  difficulty,
  timeToBeat,
  gameStartTime,
  onLose,
  onSuccess,
}) => {
  const [colorConfig, setColorConfig] = useState<string[]>([]);
  const [streak, setStreak] = useState<number>(0);

  const [puzzleStartTime, setPuzzleStartTime] = useState<number>();
  const [endTime, setEndTime] = useState<number>();

  const [isOver, setIsOver] = useState<boolean>(false);

  useEffect(() => {
    setColorConfig(
      Array(4 + difficulty * 2)
        .fill("")
        .map((_value, index) => (index % 2 === 0 ? BLACK : WHITE)),
    );
  }, [difficulty]);

  useEffect(() => {
    if(isOver) {
      return;
    }

    const currentTime: number = Date.now() - gameStartTime;

    if (!timeToBeat || timeToBeat > currentTime) {
      return;
    }

    onLose();
  }, [gameStartTime, timeToBeat]);

  useEffect(() => {
    if (streak === 1) {
      onSuccess(Date.now() - gameStartTime);
      setIsOver(true);
    }
  }, [gameStartTime, streak]);

  const handleOverEvent = (result: "lost" | "won"): void => {
    const now: number = Date.now();
    const hasWon: boolean = result === "won";

    if (hasWon) {
      setEndTime(now);
      setStreak((s) => s + 1);
    } else {
      setStreak(0);
    }

    axios
      .post("/api/games", {
        playerId,
        type: "",
        difficulty,
        isSuccess: hasWon,
        duration: hasWon && puzzleStartTime ? now - puzzleStartTime : null,
        configuration: colorConfig,
      })
      .then(() => {});
  };

  const resetClock = (): void => {
    setPuzzleStartTime(undefined);
    setEndTime(undefined);
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

  if(isOver) {
    return <div className="flex flex-1 items-center justify-center text-3xl font-extrabold">Determining Winner...</div>
  }

  return (
    <div className="flex flex-1 flex-col items-center justify-center px-5 pb-32 pt-20 xs:pb-20 sm:px-10">
      <TileConfiguration baseConfig={colorConfig} overEvent={handleOverEvent}>
        <div className={cn("absolute -top-10 left-4 text-xl text-black", streak ? "block" : "hidden")}>
          Streak: {streak}
        </div>
        <StopWatch
          className={cn("absolute -top-10 right-3 text-xl text-black", puzzleStartTime && endTime ? "block" : "hidden")}
          value={puzzleStartTime && endTime ? endTime - puzzleStartTime : null}
        />
      </TileConfiguration>

      <button className="mt-10 rounded-[20px] border-2 border-black px-12 py-4 text-2xl" onClick={shuffleColours}>
        New Puzzle
      </button>
    </div>
  );
};
