import React, { useEffect, useState } from "react";
import { User } from "../types/User.ts";
import axios from "axios";
import { Statistics } from "../types/Statistics.ts";

type PlayProps = {
  user: User;
};

export const Profile: React.FC<PlayProps> = ({ user }) => {
  const [statistics, setStatistics] = useState<Statistics>();

  useEffect(() => {
    if (!user) {
      return;
    }
    axios;
    axios.get(`api/user/${user.id}/statistics`).then((response) => setStatistics(response.data));
  }, [user]);

  if (user == null || !statistics) {
    return <div>loading</div>;
  }

  return (
    <div className="flex h-max flex-1 flex-col items-center px-5 pb-32 pt-20 xs:pb-20 sm:px-10">
      <div className="w-5/6">
        <div className="flex h-min w-full flex-col gap-2 rounded-2xl border-2 border-black px-6 py-4">
          <h3 className="mb-2 font-bold">Statistics</h3>

          <div className="flex max-w-96 justify-between">
            <div>Longest winning streak:</div>
            <div className="flex gap-5">
              <span className="text-green">{statistics?.longestWinningStreak?.easy || "N/A"}</span>
              <span className="text-blue">{statistics?.longestWinningStreak?.medium || "N/A"}</span>
              <span className="text-red">{statistics?.longestWinningStreak?.hard || "N/A"}</span>
            </div>
          </div>

          <div className="flex max-w-96 justify-between">
            <div>Longest losing streak:</div>
            <div className="flex gap-5">
              <span className="text-green">{statistics?.longestLosingStreak?.easy || "N/A"}</span>
              <span className="text-blue">{statistics?.longestLosingStreak?.medium || "N/A"}</span>
              <span className="text-red">{statistics?.longestLosingStreak?.hard || "N/A"}</span>
            </div>
          </div>

          <div className="flex max-w-96 justify-between">
            <div>Games played:</div>
            <div className="flex gap-5">
              <span className="text-green">{statistics?.gamesPlayed?.easy || "N/A"}</span>
              <span className="text-blue">{statistics?.gamesPlayed?.medium || "N/A"}</span>
              <span className="text-red">{statistics?.gamesPlayed?.hard || "N/A"}</span>
            </div>
          </div>

          <div className="flex max-w-96 justify-between">
            <div>Games won:</div>
            <div className="flex gap-5">
              <span className="text-green">{statistics?.gamesWon?.easy || "N/A"}</span>
              <span className="text-blue">{statistics?.gamesWon?.medium || "N/A"}</span>
              <span className="text-red">{statistics?.gamesWon?.hard || "N/A"}</span>
            </div>
          </div>

          <div className="flex max-w-96 justify-between">
            <div>Fastest solve:</div>
            <div className="flex gap-5">
              <span className="text-green">{statistics?.fastestSolve?.easy || "N/A"}</span>
              <span className="text-blue">{statistics?.fastestSolve?.medium || "N/A"}</span>
              <span className="text-red">{statistics?.fastestSolve?.hard || "N/A"}</span>
            </div>
          </div>

          <div className="flex max-w-96 justify-between">
            <div>Average solving time:</div>
            <div className="flex gap-5">
              <span className="text-green">{statistics?.averageTime?.easy?.toFixed() || "N/A"}</span>
              <span className="text-blue">{statistics?.averageTime?.medium?.toFixed() || "N/A"}</span>
              <span className="text-red">{statistics?.averageTime?.hard?.toFixed() || "N/A"}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
