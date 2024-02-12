import React, { ChangeEvent, useState } from "react";
import { User } from "../types/User.ts";
import axios from "axios";

type ProfileUserInfoProps = {
  className: string;
  user: User;
};

export const ProfileUserInfo: React.FC<ProfileUserInfoProps> = ({ className, user }) => {
  const [name, setName] = useState<string>(user?.name ?? "No username found");
  const [isEditingName, setIsEditingName] = useState<boolean>(false);

  if (!user) {
    return <></>;
  }

  const onNameChange = (event: ChangeEvent<HTMLInputElement>): void => {
    setName(event.target.value);
  };

  const saveName = (): void => {
    axios.put("api/user", { ...user, name }).then(() => setIsEditingName(false));
  };

  return (
    <div className={className}>
      <div className="flex max-w-96 justify-between items-center">
        <div className="font-bold">Username</div>
        {isEditingName ? (
          <div className="flex gap-5">
            <input
              className="h-max rounded-lg border-2 border-black px-3 py-1 font-light"
              type="text"
              value={name}
              onChange={onNameChange}
            />
            <button
              className="h-max items-center rounded-lg border-2 border-black px-3 py-1 font-light hover:bg-black hover:text-white"
              onClick={saveName}
            >
              Save
            </button>
          </div>
        ) : (
          <button className="cursor-text" onClick={() => setIsEditingName(true)}>
            {name}
          </button>
        )}
      </div>
      <div className="flex max-w-96 justify-between">
        <div className="font-bold">Email</div>
        <div>{user.email}</div>
      </div>
    </div>
  );
};
