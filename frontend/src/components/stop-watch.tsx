import { cn } from "../lib/utils.ts";
import React, { useEffect, useState } from "react";

type StopWatchProps = {
  className: string;
  value: number;
};

export const StopWatch: React.FC<StopWatchProps> = ({ className, value }) => {
  const [displayTime, setDisplayTime] = useState<string>("00:00:00");

  useEffect(() => {
    const minutes: number = Math.floor(value / 60000);
    const seconds: number = (value % 60000) / 1000;
    const centiseconds: number = Math.floor((value % 1000) / 10);

    const minuteString = minutes >= 10 ? minutes.toString() : "0" + minutes.toString();
    const secondString = seconds >= 10 ? seconds.toFixed(0) : "0" + seconds.toFixed(0);
    const millisecondString = centiseconds >= 10 ? centiseconds.toString() : "0" + centiseconds.toString();

    setDisplayTime(`${minuteString}:${secondString}:${millisecondString}`);
  }, [value]);

  return <div className={cn("font-mono tracking-tighter", className)}>{displayTime}</div>;
};
