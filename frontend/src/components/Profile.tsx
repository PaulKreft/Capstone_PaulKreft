import React, { useEffect, useState } from "react";
import { User } from "../types/User.ts";
import axios from "axios";
import { Statistics } from "../types/Statistics.ts";
import { ProfileUserInfo } from "./ProfileUserInfo.tsx";
import { Statistic } from "./Statistic.tsx";
import { Spinner } from "./Spinner.tsx";

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

    axios.get(`/api/user/${user.id}/statistics`).then((response) =>
      setStatistics(
        (Object.keys(response.data) as [keyof typeof statisticKeyToDisplayNameMap]).map((statistic) => ({
          ...response.data[statistic],
          name: statisticKeyToDisplayNameMap[statistic],
        })),
      ),
    );
  }, [user]);

  if (!user || !statistics) {
    return (
      <div className="flex h-screen w-screen items-center justify-center">
        <Spinner />
      </div>
    );
  }

  return (
    <div className="flex h-max flex-1 justify-center px-5 pb-32 sm:pt-20 xs:pb-20 sm:px-10">
      <div className="h-min w-96 rounded-2xl border-black py-8 sm:w-full sm:border-2 sm:px-10">
        <h3 className="mb-1 text-lg font-extralight">User Information</h3>
        <ProfileUserInfo className="mb-16 flex flex-col gap-2" user={user} />

          <h3 className="mb-1 text-lg font-extralight">Statistics</h3>
        <div className="flex flex-col gap-2">
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
