import { useEffect, useState } from "react";
import { addHexColors, getRandomHexColor, subtractHexColors } from "../lib/hexUtils.ts";
import { shuffleArray } from "../lib/shuffleArray.tsx";
import { cn } from "../lib/utils.ts";
import { StopWatch } from "./stop-watch.tsx";
import { Button } from "./Button.tsx";
import { Tile } from "./Tile.tsx";

const WHITE = "#ffffff";
const BLACK = "#000000";

const EASY = 1;
const MEDIUM = 2;
const HARD = 4;

type Difficulty = 1 | 2 | 4;

type Tile = {
  id: string;
  color: string;
};

export default function Play() {
  const [hasStarted, setHasStarted] = useState<boolean>(false);
  const [hasLost, setHasLost] = useState<boolean>(false);
  const [isOver, setIsOver] = useState<boolean>(false);

  const [difficulty, setDifficulty] = useState<Difficulty>(EASY);

  const [lastMatch, setLastMatch] = useState<string>("");

  const [sourceTile, setSourceTile] = useState<string>("");
  const [targetTile, setTargetTile] = useState<string>("");
  const [currentConfig, setCurrentConfig] = useState<Tile[]>([]);
  const [nextConfig, setNextConfig] = useState<Tile[]>([]);

  const [startTime, setStartTime] = useState<number>();
  const [endTime, setEndTime] = useState<number>();

  const [streak, setStreak] = useState<number>(0);

  useEffect(() => {
    const additionalTilePairs: Tile[] = [
      { id: "1-classic", color: WHITE },
      { id: "2-classic", color: BLACK },
      { id: "3-classic", color: WHITE },
      { id: "4-classic", color: BLACK },
    ];
    for (let i = 5; i < difficulty * 2 + 5; i += 2) {
      additionalTilePairs.push({ id: `${i}-classic`, color: WHITE }, { id: `${i + 1}-classic`, color: BLACK });
    }

    setCurrentConfig(JSON.parse(JSON.stringify(additionalTilePairs)));
    setNextConfig(JSON.parse(JSON.stringify(additionalTilePairs)));
    resetGameState();
  }, [difficulty]);

  const resetGameState = (): void => {
    setSourceTile("");
    setTargetTile("");
    setLastMatch("");
    setIsOver(false);
    setHasLost(false);
  };

  useEffect(() => {
    const blackTiles: number = nextConfig.filter((tile) => tile.color === BLACK).length;
    const whiteTiles: number = nextConfig.filter((tile) => tile.color === WHITE).length;
    const lost: boolean = blackTiles !== whiteTiles;

    if (lost) {
      setEndTime(Date.now());
      setHasLost(true);
      setIsOver(true);
      setStreak(0);
      return;
    }

    setCurrentConfig(JSON.parse(JSON.stringify(nextConfig)));
    setSourceTile("");

    const isOver = hasStarted && !nextConfig.filter((tile) => tile.color !== WHITE && tile.color !== BLACK).length;
    if (isOver) {
      setEndTime(Date.now());
      setIsOver(true);
      setStreak((s) => s + 1);
    }
  }, [hasStarted, nextConfig]);

  const selectColor = (id: string): void => {
    if (isOver || !hasStarted) {
      return;
    }

    if (!sourceTile) {
      getLastMatch(id);
      setSourceTile(id);
      return;
    }

    if (sourceTile === id) {
      setSourceTile("");
      return;
    }

    setTargetTile(id);
    combineTiles(sourceTile, id);
  };

  const getLastMatch = (last: string): void => {
    const lastColor = nextConfig.find((tile) => tile.id === last);

    if (!lastColor) {
      return;
    }
    const match: Tile | undefined = nextConfig.find((tile) => addHexColors(tile.color, lastColor.color) === WHITE);
    setLastMatch(match ? match.id : "");
  };

  const combineTiles = (givingId: string, receivingId: string): void => {
    const givingTile = nextConfig.find((tile) => tile.id === givingId);
    const receivingTile = nextConfig.find((tile) => tile.id === receivingId);

    if (!givingTile || !receivingTile) {
      return;
    }

    receivingTile.color = addHexColors(receivingTile.color, givingTile.color);
    givingTile.color = subtractHexColors(givingTile.color, givingTile.color);

    setNextConfig(
      currentConfig.map((tile) => {
        if (tile.id === givingId) {
          return givingTile;
        }
        if (tile.id === receivingId) {
          return receivingTile;
        }
        return tile;
      }),
    );
  };

  const resetTiles = (): Tile[] => {
    return currentConfig.map((tile, index) => (index % 2 == 0 ? { ...tile, color: BLACK } : { ...tile, color: WHITE }));
  };

  const shuffleColours = (): void => {
    resetGameState();
    const baseConfig: Tile[] = resetTiles();

    const newTiles: Tile[] = [];
    const whiteTiles = baseConfig.filter((tile) => tile.color === WHITE);
    const blackTiles = baseConfig.filter((tile) => tile.color === BLACK);

    whiteTiles.forEach((tile, index) => {
      const randomColor = getRandomHexColor(tile.color);
      newTiles.push({ id: tile.id, color: subtractHexColors(tile.color, randomColor) });
      newTiles.push({ id: blackTiles[index].id, color: addHexColors(blackTiles[index].color, randomColor) });
    });

    const shuffledTiles = shuffleArray<Tile>(newTiles);

    setCurrentConfig(shuffledTiles);
    setNextConfig(shuffledTiles);
    setHasStarted(true);
    setStartTime(Date.now());
  };

  return (
    <div className="flex flex-1 flex-col items-center justify-center px-5 pb-32 pt-20 xs:pb-20 sm:px-10">
      <div
        className={cn(
          "relative flex flex-wrap justify-center gap-4",
          currentConfig.length === 6 ? "max-w-80 sm:max-w-96" : "max-w-[24rem] sm:max-w-[32rem]",
        )}
      >
        <div className={cn("absolute -top-10 left-4 text-xl", hasLost ? "text-[#BA2D0B]" : "text-black")}>
          Streak: {streak}
        </div>
        <StopWatch
          className={cn("absolute -top-10 right-3 text-xl text-black", isOver && !hasLost ? "block" : "hidden")}
          value={endTime && startTime ? endTime - startTime : 5999999}
        />
        {currentConfig.map((tile) => (
          <Tile
            key={tile.id}
            onClick={() => selectColor(tile.id)}
            color={tile.color}
            isSelected={sourceTile === tile.id}
            isWrong={targetTile === tile.id && hasLost}
            isCorrect={lastMatch === tile.id && hasLost}
            isClickable={isOver || !hasStarted}
          />
        ))}
      </div>

      <button className="mt-10 rounded-[20px] border-2 border-black px-12 py-4 text-2xl" onClick={shuffleColours}>
        New Puzzle
      </button>

      <div className="mt-10 flex gap-3 xs:gap-5">
        <Button color="#73BA9B" onClick={() => setDifficulty(EASY)} isActive={difficulty === 1}>
          Easy
        </Button>
        <Button color="#6D98BA" onClick={() => setDifficulty(MEDIUM)} isActive={difficulty === 2}>
          Medium
        </Button>
        <Button color="#BA2D0B" onClick={() => setDifficulty(HARD)} isActive={difficulty === 4}>
          Hard
        </Button>
      </div>
    </div>
  );
}
