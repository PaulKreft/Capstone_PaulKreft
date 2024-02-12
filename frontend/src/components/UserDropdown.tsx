import React from "react";
import { cn } from "../lib/utils.ts";

type UserDropdownProps = {
  userName?: string;
  toggleDropdown: () => void;
  isActive: boolean;
};

export const UserDropdown: React.FC<UserDropdownProps> = ({ userName, toggleDropdown, isActive }) => {
  return (
    <div>
      <button
        onClick={toggleDropdown}
        className={cn(
          "border-grey-light flex h-8 w-8 items-center justify-center rounded-full border border-black",
          isActive ? "bg-black text-white" : "bg-white text-black",
        )}
      >
        <div>{userName ? userName.substring(0, 1).toUpperCase() : "X"}</div>
      </button>
    </div>
  );
};
