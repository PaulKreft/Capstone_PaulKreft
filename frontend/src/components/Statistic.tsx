import React from "react";

type StatisticProps = {
  name: string;
  easy: number;
  medium: number;
  hard: number;
};

export const Statistic: React.FC<StatisticProps> = ({ name, easy, medium, hard }) => {
  return (
    <div className="flex max-w-96 justify-between">
      <div className="font-bold">{name}</div>
      <div className="flex gap-2">
        <span className="text-sm rounded border border-green px-1 text-green">{easy?.toFixed() || "N/A"}</span>
        <span className="text-sm rounded border border-blue px-1 text-blue">{medium?.toFixed() || "N/A"}</span>
        <span className="text-sm rounded border border-red px-1 text-red">{hard?.toFixed() || "N/A"}</span>
      </div>
    </div>
  );
};
