import React from "react";
import { User } from "../types/User.ts";

type PlayProps = {
  user: User;
};

export const Profile: React.FC<PlayProps> = ({ user }) => {
  if (user == null) {
    return <div>loading</div>;
  }

  return (
    <div className="flex h-max flex-1 flex-col items-center px-5 pb-32 pt-20 xs:pb-20 sm:px-10">
      <div className="w-5/6">
        <div className="h-min w-full rounded-2xl border-2 border-black px-6 py-4">
          <h3 className="mb-2 font-bold">Statistics</h3>
          <div>
            Longest winning streak: {" "}
            <span className="text-green">easy</span> | <span className="text-blue">medium</span> |{" "}
            <span className="text-red">hard</span>
          </div>
        </div>
      </div>
    </div>
  );
};
