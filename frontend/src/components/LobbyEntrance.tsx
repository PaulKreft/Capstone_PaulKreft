import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Lobby } from "../types/Lobby.ts";
import { Player } from "../types/Player.ts";
import { User } from "../types/User.ts";

type MultiPlayerProps = {
  user: User;
};

const EASY = 1;

export const LobbyEntrance: React.FC<MultiPlayerProps> = ({ user }) => {
  const [lobbyId, setLobbyId] = useState<string>("");

  const navigate = useNavigate();

  if (!user) {
    return <div>No user found</div>;
  }

  const currentPlayer: Player = { id: user.id, name: user.name };

  const createLobby = (): void => {
    const lobbyId = Math.ceil(Math.random() * 1000000).toString();

    const lobby: Lobby = {
      id: lobbyId,
      host: currentPlayer,
      players: [currentPlayer],
      isGameInProgress: false,
      isGameOver: false,
      difficulty: EASY,
      losers: [],
      streakToWin: 3,
    };

    axios.post("/api/lobby", lobby).then(() => navigate(`/multiplayer/lobby/${lobbyId}`));
  };

  const joinLobby = (): void => {
    axios.put(`/api/lobby/${lobbyId}/join`, currentPlayer).then(() => navigate(`/multiplayer/lobby/${lobbyId}`));
  };

  const handleKeyDownOnInput = (event: React.KeyboardEvent<HTMLInputElement>): void => {
    if(event.key === "Enter") {
      joinLobby();
    }
  }

  return (
    <div className="flex h-max flex-1 flex-col items-center px-5 pb-32 pt-20 xs:pb-20 sm:px-10">
      <div className="flex h-80 w-80 flex-col items-center justify-evenly gap-5 rounded-2xl border-2 border-black p-5">
        <button
          className="h-max items-center rounded-lg border-2 border-black px-6 py-2 text-xl font-light hover:bg-black hover:text-white"
          onClick={createLobby}
        >
          Create new lobby
        </button>
        <div className="flex justify-center gap-5">
          <input
            className="h-max w-2/5 rounded-lg border-2 border-black px-3 py-1 font-light"
            type="text"
            value={lobbyId}
            onChange={(event) => setLobbyId(event.target.value)}
            placeholder="Lobby ID"
            onKeyDown={handleKeyDownOnInput}
          />
          <button
            className="h-max items-center rounded-lg border-2 border-black px-3 py-1 font-light hover:bg-black hover:text-white"
            onClick={joinLobby}
          >
            Join
          </button>
        </div>
      </div>
    </div>
  );
};
