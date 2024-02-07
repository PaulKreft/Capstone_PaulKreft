import { cn } from "../lib/utils.ts";
import React from "react";

type ButtonProps = {
  children: React.ReactNode;
  color: string;
  isActive: boolean;
  onClick: () => void;
};

export const Button: React.FC<ButtonProps> = ({ children, color, isActive, onClick }) => {
  return (
    <button
      className={cn("xs:px-5 xs:py-2 xs:text-xl rounded-2xl px-3 py-1 text-lg")}
      style={{
        backgroundColor: isActive ? color : "white",
        color: isActive ? "white" : color,
        border: isActive ? "none" : `${color} solid 2px`,
      }}
      onClick={onClick}
    >
      {children}
    </button>
  );
};
