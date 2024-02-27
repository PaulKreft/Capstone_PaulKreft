import React, { ChangeEvent, useState } from "react";
import { User } from "../types/User.ts";
import axios from "axios";
import checkMarkUrl from "./../assets/checkmark.svg";

type ProfileUserInfoProps = {
  className: string;
  user: User;
  updateUser: (user: User) => void;
};

export const ProfileUserInfo: React.FC<ProfileUserInfoProps> = ({ className, user, updateUser }) => {
  const [name, setName] = useState<string>(user?.name ?? "No username found");
  const [isEditingName, setIsEditingName] = useState<boolean>(false);

  if (!user) {
    return <></>;
  }

  const onNameChange = (event: ChangeEvent<HTMLInputElement>): void => {
    setName(event.target.value);
  };

  const saveName = (): void => {
    axios.put("api/user", { ...user, name }).then((response) => {
      setIsEditingName(false);
      updateUser(response.data);
    });
  };

  return (
    <div className={className}>
      <div className="flex h-8 max-w-96 items-center justify-between">
        <div>Username</div>
        {isEditingName ? (
          <div className="flex h-full gap-3">
            <button
              className="h-full items-center rounded-lg border-2 border-black font-light hover:bg-black hover:text-white"
              onClick={saveName}
            >
              <img className="h-full px-2 py-1 hover:invert" src={checkMarkUrl} alt="checkmark" />
            </button>
            <input
              className="h-full w-32 rounded-lg border-2 border-black px-3 font-light"
              type="text"
              value={name}
              onChange={onNameChange}
            />
          </div>
        ) : (
          <button className="cursor-text" onClick={() => setIsEditingName(true)}>
            {name}
          </button>
        )}
      </div>
      <div className="flex h-8 max-w-96 justify-between items-center">
        <div>Email</div>
        <div>{user.email}</div>
      </div>
    </div>
  );
};
