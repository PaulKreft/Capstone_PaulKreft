import React, { useEffect, useState } from "react";
import { cn } from "../lib/utils.ts";
import { StopWatch } from "./StopWatch.tsx";
import { Button } from "./Button.tsx";
import { TileConfiguration } from "./TileConfiguration.tsx";
import { addHexColors, getRandomHexColor, subtractHexColors } from "../lib/hexUtils.ts";
import { shuffleArray } from "../lib/shuffleArray.tsx";
import axios from "axios";
import { Difficulty } from "../types/Difficulty.ts";

const WHITE = "#ffffff";
const BLACK = "#000000";

const EASY = 1;
const MEDIUM = 2;
const HARD = 4;

type PlayProps = {
  userId?: string;
};

export const Play: React.FC<PlayProps> = ({ userId }) => {
  const [colorConfig, setColorConfig] = useState<string[]>([]);

  const [difficulty, setDifficulty] = useState<Difficulty>(EASY);

  const [streak, setStreak] = useState<number>(0);

  const [startTime, setStartTime] = useState<number>();
  const [endTime, setEndTime] = useState<number>();

  useEffect(() => {
    setColorConfig(
      Array(4 + difficulty * 2)
        .fill("")
        .map((_value, index) => (index % 2 === 0 ? BLACK : WHITE)),
    );
  }, [difficulty]);

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
      .post("/api/game", {
        userId,
        type: "",
        difficulty,
        isSuccess: hasWon,
        duration: hasWon && startTime ? now - startTime : null,
        configuration: colorConfig,
      })
      .then(() => {});
  };

  const resetClock = (): void => {
    setStartTime(undefined);
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
    setStartTime(Date.now());
  };

  return (
    <div className="flex flex-1 flex-col items-center justify-center px-2 pb-32 xs:pb-20 sm:mx-10">
      <TileConfiguration className="mt-6" baseConfig={colorConfig} overEvent={handleOverEvent}>
        <div
          className={cn("text-xl text-black", streak ? "text-black" : "text-transparent", userId ? "block" : "hidden")}
        >
          Streak: {streak}
        </div>
        <StopWatch
          className={cn(
            "text-xl text-black",
            startTime && endTime ? "text-black" : "text-transparent",
            userId ? "block" : "hidden",
          )}
          value={startTime && endTime ? endTime - startTime : null}
        />
      </TileConfiguration>

      <button className="mt-10 rounded-[20px] border-2 border-black px-12 py-4 text-2xl" onClick={shuffleColours}>
        New Puzzle
      </button>
      <div className="mt-10 flex gap-3 xs:gap-5">
        <Button color="#73BA9B" onClick={() => setDifficulty(EASY)} isActive={difficulty === EASY}>
          Easy
        </Button>
        <Button color="#6D98BA" onClick={() => setDifficulty(MEDIUM)} isActive={difficulty === MEDIUM}>
          Medium
        </Button>
        <Button color="#BA2D0B" onClick={() => setDifficulty(HARD)} isActive={difficulty === HARD}>
          Hard
        </Button>
      </div>
    </div>
  );
};
