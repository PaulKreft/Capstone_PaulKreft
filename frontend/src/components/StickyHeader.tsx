import React, { useEffect, useRef, useState } from "react";
import { cn } from "../lib/utils.ts";

type StickyHeaderProps = {
  isFixed: boolean;
  children: React.ReactNode;
  className?: string;
};

export const StickyHeader: React.FC<StickyHeaderProps> = ({ isFixed, children, className }) => {
  const [top, setTop] = useState<number>(0);
  const header = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleScroll = () => {
      if (header.current) {
        setTop(header.current.offsetTop);
      }
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  return (
    <div
      ref={header}
      className={cn(
        "top-0 flex h-16 w-full items-center justify-start transition-colors",
        top > 0 && "border-b",
        isFixed ? "fixed" : "sticky",
        className,
      )}
    >
      {children}
    </div>
  );
};
