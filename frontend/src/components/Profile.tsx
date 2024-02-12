import React, { useEffect, useState } from "react";
import { User } from "../types/User.ts";
import axios from "axios";
import { Statistics } from "../types/Statistics.ts";
import { Statistic } from "./Statistic.tsx";

type PlayProps = {
  user: User;
};

export const Profile: React.FC<PlayProps> = ({ user }) => {
  const [statistics, setStatistics] = useState<Statistics>();

  useEffect(() => {
    if (!user) {
      return;
    }

    const statisticKeyToDisplayNameMap: object = {
      longestWinningStreak: "Longest winning streak",
      longestLosingStreak: "Longest losing streak",
      gamesPlayed: "Games played",
      gamesWon: "Games won",
      fastestSolve: "Fastest solve",
      averageTime: "Average solving time",
    };

    axios.get(`api/user/${user.id}/statistics`).then((response) =>
      setStatistics(
        (Object.keys(response.data) as [keyof typeof statisticKeyToDisplayNameMap]).map((statistic) => ({
          ...response.data[statistic],
          name: statisticKeyToDisplayNameMap[statistic],
        })),
      ),
    );
  }, [user]);

  if (user == null || !statistics) {
    return <div>loading</div>;
  }

  return (
    <div className="flex h-max flex-1 flex-col items-center px-5 pb-32 pt-20 xs:pb-20 sm:px-10">
      <div className="w-5/6">
        <div className="flex h-min w-full flex-col gap-2 rounded-2xl border-2 border-black px-6 py-4">
          <h3 className="mb-2 font-bold">Statistics</h3>

          {statistics.map((statistic) => (
            <Statistic
              key={statistic.name}
              name={statistic.name}
              easy={statistic.easy}
              medium={statistic.medium}
              hard={statistic.hard}
            />
          ))}
        </div>
      </div>
    </div>
  );
};
