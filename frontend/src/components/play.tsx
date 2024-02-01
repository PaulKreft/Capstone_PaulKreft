import { useEffect, useState } from "react";
import { addHexColors, getRandomHexColor, subtractHexColors } from "../lib/hexUtils.ts";
import { shuffleArray } from "../lib/shuffleArray.tsx";

const WHITE = "#ffffff";
const BLACK = "#000000";

type Color = {
  id: string;
  value: string;
};

export default function Play() {
  const [hasLost, setHasLost] = useState<boolean>(false);
  const [isOver, setIsOver] = useState<boolean>(false);

  const [selectedColor, setSelectedColor] = useState<string>("");
  const [colors, setColors] = useState<Color[]>([
    { id: "1", value: "#FFf" },
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

  const selectColor = (id: string) => {
    if (!selectedColor) {
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
    <div className="mx-auto flex flex-1 flex-col items-center justify-center pb-52">
      {!isOver || (
        <div className="mb-16 text-5xl">
          You {hasLost ? <span className="text-[#9F0003]"> lost...</span> : <span> won!</span>}
        </div>
      )}
      <div className="flex max-w-96 flex-wrap justify-center gap-4">
        {colors.map((color) => (
          <div
            key={color.id}
            className={`h-28 w-28 rounded-xl border border-black text-blue-600 ${selectedColor === color.id ? "border-4 border-black" : ""}`}
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
