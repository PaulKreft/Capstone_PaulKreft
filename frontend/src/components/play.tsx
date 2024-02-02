import { useEffect, useState } from "react";
import { addHexColors, getRandomHexColor, subtractHexColors } from "../lib/hexUtils.ts";
import { shuffleArray } from "../lib/shuffleArray.tsx";
import { cn } from "../lib/utils.ts";

const WHITE = "#ffffff";
const BLACK = "#000000";

type Color = {
  id: string;
  value: string;
};

export default function Play() {
  const [hasLost, setHasLost] = useState<boolean>(false);
  const [isOver, setIsOver] = useState<boolean>(false);

  const [lastMatch, setLastMatch] = useState<string>("");

  const [selectedColor, setSelectedColor] = useState<string>("");
  const [colors, setColors] = useState<Color[]>([
    { id: "1", value: "#FFF" },
    { id: "2", value: "#000" },
    { id: "3", value: "#FFF" },
    { id: "4", value: "#000" },
    { id: "5", value: "#FFF" },
    { id: "6", value: "#000" },
  ]);

  useEffect(() => {
    const blackTiles: number = colors.filter((tile) => tile.value === BLACK).length;
    const whiteTiles: number = colors.filter((tile) => tile.value === WHITE).length;
    const lost: boolean = blackTiles !== whiteTiles;

    if (lost) {
      setHasLost(true);
      setIsOver(true);
      return;
    }

    setIsOver(!colors.filter((tile) => tile.value !== WHITE && tile.value !== BLACK).length);
  }, [colors]);

  const selectColor = (id: string): void => {
    if (isOver) {
      return;
    }

    if (!selectedColor) {
      getLastMatch(id);
      setSelectedColor(id);
      return;
    }

    if (selectedColor === id) {
      setSelectedColor("");
      return;
    }

    combineTiles(selectedColor, id);
    setSelectedColor("");
  };

  const getLastMatch = (last: string): void => {
    const lastColor = colors.find((color) => color.id === last);

    if (!lastColor) {
      return;
    }
    const match: Color | undefined = colors.find((color) => addHexColors(color.value, lastColor.value) === WHITE);
    setLastMatch(match ? match.id : "Nothing found");
  };

  const combineTiles = (givingId: string, receivingId: string): void => {
    const givingTile = colors.find((tile) => tile.id === givingId);
    const receivingTile = colors.find((tile) => tile.id === receivingId);

    if (!givingTile || !receivingTile) {
      return;
    }

    receivingTile.value = addHexColors(receivingTile.value, givingTile.value);
    givingTile.value = subtractHexColors(givingTile.value, givingTile.value);

    setColors(
      colors.map((color) => {
        if (color.id === givingId) {
          return givingTile;
        }
        if (color.id === receivingId) {
          return receivingTile;
        }
        return color;
      }),
    );
  };

  const resetTiles = (): Color[] => {
    return colors.map((color, index) => (index % 2 == 0 ? { ...color, value: BLACK } : { ...color, value: WHITE }));
  };

  const shuffleColours = (): void => {
    setIsOver(false);
    setHasLost(false);
    setSelectedColor("");
    const colors: Color[] = resetTiles();

    const newTiles: Color[] = [];
    const whiteTiles = colors.filter((color) => color.value === WHITE);
    const blackTiles = colors.filter((color) => color.value === BLACK);

    whiteTiles.forEach((tile, index) => {
      const randomColor = getRandomHexColor(tile.value);
      newTiles.push({ id: tile.id, value: subtractHexColors(tile.value, randomColor) });
      newTiles.push({ id: blackTiles[index].id, value: addHexColors(blackTiles[index].value, randomColor) });
    });

    const shuffledTiles = shuffleArray<Color>(newTiles);

    setColors(shuffledTiles);
  };

  return (
    <div className="mx-auto flex max-w-72 flex-1 flex-col items-center justify-center sm:max-w-none sm:px-10 sm:pb-20">
      <div className={cn("mb-8 text-5xl sm:mb-10", isOver ? "text-black" : "text-transparent")}>
        You {hasLost ? <span className="text-[#9F0003]"> lost...</span> : <span> won!</span>}
      </div>

      <div className="flex max-w-96 flex-wrap justify-center gap-4">
        {colors.map((color) => (
          <div
            key={color.id}
            className={cn(
              "h-28 w-28 rounded-xl text-blue-600",
              selectedColor === color.id ? "border-2 border-black" : "",
              lastMatch === color.id && hasLost ? "border-4 border-white outline outline-4 outline-black" : "",
              isOver ? "cursor-default" : "cursor-pointer",
              [WHITE, "#FFF"].includes(color.value) ? "border border-black" : "",
            )}
            onClick={() => selectColor(color.id)}
            style={{ backgroundColor: color.value }}
          ></div>
        ))}
      </div>

      <button className="mt-10 rounded-[20px] border-2 border-black px-12 py-4 text-2xl" onClick={shuffleColours}>
        New Puzzle
      </button>
    </div>
  );
}
