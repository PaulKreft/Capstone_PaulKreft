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
    <div
      className={cn(
        "h-20 w-20 rounded-xl text-blue-600 sm:h-28 sm:w-28",
        isSelected ? "border-2 border-white outline outline-2 outline-black" : "",
        isWrong ? "border-4 border-white outline outline-4 outline-[#BA2D0B]" : "",
        isCorrect ? "border-4 border-white outline outline-4 outline-[#73BA9B]" : "",
        isClickable ? "cursor-pointer" : "cursor-default",
        ["#fff", "#FFFFFF", "#ffffff", "#FFF"].includes(color) ? "border border-black" : "",
      )}
      onClick={isClickable ? onClick : () => {}}
      style={{ backgroundColor: color }}
    />
  );
};
