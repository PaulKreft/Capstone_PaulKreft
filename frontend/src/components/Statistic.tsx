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
      <div className="flex gap-5">
        <span className="text-green">{easy?.toFixed() || "N/A"}</span>
        <span className="text-blue">{medium?.toFixed() || "N/A"}</span>
        <span className="text-red">{hard?.toFixed() || "N/A"}</span>
      </div>
    </div>
  );
};
