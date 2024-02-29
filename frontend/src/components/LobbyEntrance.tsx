import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Lobby } from "../types/Lobby.ts";
import { Player } from "../types/Player.ts";
import { User } from "../types/User.ts";
import { Spinner } from "./Spinner.tsx";

type MultiPlayerProps = {
  user: User;
};

const EASY = 1;

const DUEL_CAPACITY = 2;

export const LobbyEntrance: React.FC<MultiPlayerProps> = ({ user }) => {
  const [lobbyId, setLobbyId] = useState<string>("");

  const navigate = useNavigate();

  if (!user) {
    return (
      <div className="flex flex-1 items-center justify-center">
        <Spinner />
      </div>
    );
  }

  const currentPlayer: Player = { id: user.id, name: user.name };

  const createLobby = (capacity: number | null = null): void => {
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
      ...(capacity && { capacity }),
    };

    axios.post("/api/lobby", lobby).then(() => navigate(`/multiplayer/lobby/${lobbyId}`));
  };

  const joinLobby = (): void => {
    axios
      .put(`/api/lobby/${lobbyId}/join`, currentPlayer)
      .then(() => navigate(`/multiplayer/lobby/${lobbyId}`))
      .catch((error) => console.log(error));
  };

  const handleKeyDownOnInput = (event: React.KeyboardEvent<HTMLInputElement>): void => {
    if (event.key === "Enter") {
      joinLobby();
    }
  };

  return (
    <div className="flex h-max flex-1 flex-col items-center px-5 pb-32 pt-20 xs:pb-20 sm:px-10">
      <div className="flex h-[24rem] w-80 flex-col items-center justify-between rounded-2xl border-2 border-black px-12 pb-14 pt-8">
        <div className="text-2xl font-bold">Enter a lobby</div>
        <div className="mb-4 flex w-full justify-between">
          <input
            className="h-max w-3/5 rounded-lg border-2 border-black px-3 py-1 font-light"
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
        <div className="flex w-full flex-col gap-5">
          <button
            className="h-max w-full items-center rounded-lg border-2 border-black py-2 text-lg font-light hover:bg-black hover:text-white"
            onClick={() => createLobby(DUEL_CAPACITY)}
          >
            New Duel lobby
          </button>
          <button
            className="h-max w-full items-center rounded-lg border-2 border-black py-2 text-lg font-light hover:bg-black hover:text-white"
            onClick={() => createLobby()}
          >
            New Multiplayer lobby
          </button>
        </div>
      </div>
    </div>
  );
};
