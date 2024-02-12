import React from "react";
import { cn } from "../lib/utils.ts";

type HamburgerTwoProps = {
  onHamburgerClick: () => void;
  isActive: boolean;
};

export const HamburgerTwo: React.FC<HamburgerTwoProps> = ({ onHamburgerClick, isActive }) => {
  return (
    <div>
      <button
        onClick={onHamburgerClick}
        className="border-grey-light flex h-8 w-8 items-center justify-center rounded-full border border-black"
      >
        <div className="relative h-2 w-[14px]">
          <div
            className={cn(
              "bg-grey absolute h-[1.5px] w-[14px] rounded-full border border-black transition-all duration-200",
              isActive ? "top-1/2 rotate-45" : "top-0",
            )}
          ></div>
          <div
            className={cn(
              "bg-grey absolute h-[1.5px] w-[14px] rounded-full border border-black transition-all duration-200",
              isActive ? "top-1/2 -rotate-45" : "bottom-0",
            )}
          ></div>
        </div>
      </button>
    </div>
  );
};
