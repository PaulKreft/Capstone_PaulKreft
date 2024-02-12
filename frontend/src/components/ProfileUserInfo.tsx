import React, { ChangeEvent, useState } from "react";
import { User } from "../types/User.ts";

type ProfileUserInfoProps = {
  className: string;
  user: User;
};

export const ProfileUserInfo: React.FC<ProfileUserInfoProps> = ({ className, user }) => {

  // TODO add default random name, like "Erratic Bunny #1" to a newly created user
  const [name, setName] = useState<string>(user?.name ?? "No username found");
  const [isEditingName, setIsEditingName] = useState<boolean>(false);

  const onNameChange = (event: ChangeEvent<HTMLInputElement>): void => {
    setName(event.target.value);
  };

  return (
    <div className={className}>
      {isEditingName ? (
        <input
          className="flex h-max items-center rounded-lg border-2 border-black px-3 py-1 font-light"
          type="text"
          value={name}
          onChange={onNameChange}
          onBlur={() => setIsEditingName(false)}
        />
      ) : (
        <button onClick={() => setIsEditingName(true)}>{name}</button>
      )}
    </div>
  );
};
