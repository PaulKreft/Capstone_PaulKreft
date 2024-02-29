import React, { ChangeEvent, useState } from "react";

type ChatProps = {
  className: string;
};

export const Chat: React.FC<ChatProps> = ({ className }) => {
  const [message, setMessage] = useState<string>("");

  const onMessageChange = (event: ChangeEvent<HTMLInputElement>) => {
    setMessage(event.target.value);
  };

  return (
    <div className={className}>
      <div className=""></div>
      <div className="flex h-10 gap-2">
        <input
          className="h-full rounded-lg border-2 border-black px-3 font-light"
          type="text"
          value={message}
          onChange={onMessageChange}
        />
        <button className="w-max rounded-lg border-2 border-transparent bg-black px-5 py-2 leading-none text-white hover:border-black hover:bg-white hover:text-black">
          Send
        </button>
      </div>
    </div>
  );
};
