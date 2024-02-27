import { cn } from "../lib/utils.ts";
import React from "react";

type TileProps = {
  color: string;
  isClickable: boolean;
  isSelected: boolean;
  isWrong: boolean;
  isCorrect: boolean;
  onClick: () => void;
};

export const Tile: React.FC<TileProps> = ({ color, isClickable, isSelected, isWrong, isCorrect, onClick }) => {
  return (
    <button
      className={cn(
        "text-blue-600 h-20 w-20 rounded-xl sm:h-28 sm:w-28",
        isSelected ? "border-2 border-white outline outline-2 outline-black" : "",
        isWrong ? "border-4 border-white outline outline-4 outline-red" : "",
        isCorrect ? "border-4 border-white outline outline-4 outline-green" : "",
        ["#fff", "#FFFFFF", "#ffffff", "#FFF"].includes(color) ? "border border-black" : "",
      )}
      onClick={onClick}
      style={{ backgroundColor: color }}
      disabled={!isClickable}
    />
  );
};
