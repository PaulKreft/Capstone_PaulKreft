import React, { ChangeEvent, useEffect, useState } from "react";
import { Chat } from "../types/Chat.ts";
import axios from "axios";
import { ChatMessage } from "../types/ChatMessage.ts";

type ChatProps = {
  className: string;
  lobbyId: string;
  username: string;
  chat: Chat;
};

export const ChatContainer: React.FC<ChatProps> = ({ className, username, lobbyId, chat }) => {
  const [message, setMessage] = useState<string>();
  const [chatMessages, setChatMessages] = useState<ChatMessage[]>(chat.messages);

  const onMessageChange = (event: ChangeEvent<HTMLInputElement>) => {
    setMessage(event.target.value);
  };

  const onSendMessage = () => {
    axios
      .post(`/api/lobby/${lobbyId}/chatMessage`, {
        author: username,
        content: message,
      })
      .then(() => setMessage(""));
  };

  useEffect(() => {
    setChatMessages(chat.messages);
  }, [chat]);


  const handleKeyDownOnInput = (event: React.KeyboardEvent<HTMLInputElement>): void => {
    if (event.key === "Enter") {
      onSendMessage();
    }
  };

  return (
    <div className={className}>
      <div className="flex flex-col">
        {chatMessages.map((message) => (
          <div key={`${message}-${Math.random() * 1000000}`}><strong className="mr-2">{message.author}</strong>{message.content}</div>
        ))}
      </div>
      <div className="flex h-10 gap-2">
        <input
          className="h-full rounded-lg border-2 border-black px-3 font-light"
          type="text"
          value={message}
          onChange={onMessageChange}
          onKeyDown={handleKeyDownOnInput}
        />
        <button
          className="w-max rounded-lg border-2 border-transparent bg-black px-5 py-2 leading-none text-white hover:border-black hover:bg-white hover:text-black"
          onClick={onSendMessage}
        >
          Send
        </button>
      </div>
    </div>
  );
};
