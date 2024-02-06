import { useEffect, useState } from "react";
import { addHexColors, getRandomHexColor, subtractHexColors } from "../lib/hexUtils.ts";
import { shuffleArray } from "../lib/shuffleArray.tsx";
import { cn } from "../lib/utils.ts";

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
  const [hasLost, setHasLost] = useState<boolean>(false);
  const [isOver, setIsOver] = useState<boolean>(false);

  const [difficulty, setDifficulty] = useState<Difficulty>(EASY);

  const [lastMatch, setLastMatch] = useState<string>("");

  const [sourceTile, setSourceTile] = useState<string>("");
  const [targetTile, setTargetTile] = useState<string>("");
  const [currentConfig, setCurrentConfig] = useState<Tile[]>([]);
  const [nextConfig, setNextConfig] = useState<Tile[]>([]);

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
      setHasLost(true);
      setIsOver(true);
      return;
    }

    setCurrentConfig(JSON.parse(JSON.stringify(nextConfig)));
    setSourceTile("");
    setIsOver(!nextConfig.filter((tile) => tile.color !== WHITE && tile.color !== BLACK).length);
  }, [nextConfig]);

  const selectColor = (id: string): void => {
    if (isOver) {
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
  };

  return (
    <div className="xs:pb-20 flex flex-1 flex-col items-center justify-center px-5 pb-32 sm:px-10">
      <div className={cn("mb-8 text-2xl sm:mb-10 sm:text-5xl", isOver ? "text-black" : "text-transparent")}>
        You {hasLost ? <span className="text-[#9F0003]"> lost...</span> : <span> won!</span>}
      </div>

      <div
        className={cn(
          "flex flex-wrap justify-center gap-4",
          currentConfig.length === 6 ? "max-w-80 sm:max-w-96" : "max-w-[24rem] sm:max-w-[32rem]",
        )}
      >
        {currentConfig.map((tile) => (
          <div
            key={tile.id}
            className={cn(
              "h-20 w-20 rounded-xl text-blue-600 sm:h-28 sm:w-28",
              sourceTile === tile.id ? "border-2 border-white outline outline-2 outline-black" : "",
              targetTile === tile.id && hasLost ? "border-4 border-white outline outline-4 outline-[#BA2D0B]" : "",
              lastMatch === tile.id && hasLost ? "border-4 border-white outline outline-4 outline-[#73BA9B]" : "",
              isOver ? "cursor-default" : "cursor-pointer",
              [WHITE, "#FFF"].includes(tile.color) ? "border border-black" : "",
            )}
            onClick={() => selectColor(tile.id)}
            style={{ backgroundColor: tile.color }}
          ></div>
        ))}
      </div>

      <button className="mt-10 rounded-[20px] border-2 border-black px-12 py-4 text-2xl" onClick={shuffleColours}>
        New Puzzle
      </button>

      <div className="xs:gap-5 mt-10 flex gap-3">
        <button
          className={cn(
            "xs:px-5 xs:py-2 xs:text-xl rounded-2xl px-3 py-1 text-lg",
            difficulty === 1 ? "bg-[#73BA9B] text-white" : "border-2 border-[#73BA9B] text-[#73BA9B]",
          )}
          onClick={() => setDifficulty(EASY)}
        >
          Easy
        </button>
        <button
          className={cn(
            "xs:px-5 xs:py-2 xs:text-xl rounded-2xl px-3 py-1 text-lg",
            difficulty === 2 ? "bg-[#6D98BA] text-white" : "border-2 border-[#6D98BA] text-[#6D98BA]",
          )}
          onClick={() => setDifficulty(MEDIUM)}
        >
          Medium
        </button>
        <button
          className={cn(
            "xs:px-5 xs:py-2 xs:text-xl rounded-2xl px-3 py-1 text-lg",
            difficulty === 4 ? "bg-[#BA2D0B] text-white" : "border-2 border-[#BA2D0B] text-[#BA2D0B]",
          )}
          onClick={() => setDifficulty(HARD)}
        >
          Hard
        </button>
      </div>
    </div>
  );
}
