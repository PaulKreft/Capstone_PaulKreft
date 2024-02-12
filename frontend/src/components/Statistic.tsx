import React from "react";

type StatisticProps = {
  name: string;
  easy: string;
  medium: string;
  hard: string;
};

export const Statistic: React.FC<StatisticProps> = ({ name, easy, medium, hard }) => {
  return (
    <div className="flex max-w-96 justify-between">
      <div>{name}:</div>
      <div className="flex gap-5">
        <span className="text-green">{easy || "N/A"}</span>
        <span className="text-blue">{medium || "N/A"}</span>
        <span className="text-red">{hard || "N/A"}</span>
      </div>
    </div>
  );
};
