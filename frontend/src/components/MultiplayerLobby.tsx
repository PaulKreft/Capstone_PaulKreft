import { Spinner } from "./Spinner.tsx";
import React, { ChangeEvent } from "react";
import { Player } from "../types/Player.ts";
import { Lobby } from "../types/Lobby.ts";

type MultiplayerLobbyProps = {
  lobby: Lobby;
  player: Player;
  onStreakToWinChange: (event: ChangeEvent<HTMLInputElement>) => void;
  onDifficultyChange: (event: ChangeEvent<HTMLSelectElement>) => void;
  startGame: () => void;
};

const EASY = 1;
const MEDIUM = 2;
const HARD = 4;

export const MultiplayerLobby: React.FC<MultiplayerLobbyProps> = ({
  lobby,
  player,
  onStreakToWinChange,
  onDifficultyChange,
  startGame,
}) => {
  return (
    <div className="flex flex-1 flex-col items-center justify-center">
      <div className="flex justify-evenly rounded-2xl border-2 border-black px-20 py-10">
        <div className="flex flex-col items-center">
          <div className="text-xl font-extrabold">{`${lobby.host.name}'s lobby`}</div>
          <div className="text-lg font-light">{`ID: ${lobby.id}`}</div>
          <div className="mt-12 flex flex-col gap-1 rounded-lg border border-black p-3">
            {lobby.players.map((player) => (
              <div key={player.id}> {player.name}</div>
            ))}
          </div>
          <div className={lobby.host.id !== player.id ? "hidden" : "mt-10 flex flex-col gap-2"}>
            <div className="flex items-center justify-between">
              <span className="mr-5 font-medium">Difficulty</span>
              <select value={lobby.difficulty} onChange={onDifficultyChange}>
                <option value={EASY}>Easy</option>
                <option value={MEDIUM}>Medium</option>
                <option value={HARD}>Hard</option>
              </select>
            </div>
            <div className="flex items-center justify-between">
              <span className="mr-5 font-medium">Streak to win</span>
              <input
                className="h-max w-20 items-center rounded-lg border-2 border-black bg-white px-3 py-1 font-light text-black"
                value={lobby.streakToWin.toString()}
                onChange={onStreakToWinChange}
                type="number"
                min="1"
              />
            </div>
          </div>
          {lobby.players.length < 2 ? (
            <div className="mt-12 flex flex-col items-center gap-2">
              Waiting for players <Spinner size="sm" />
            </div>
          ) : lobby.host.id === player.id ? (
            <button
              className="mt-6 h-max items-center rounded-lg border-2 border-black bg-black px-9 py-3 text-xl font-light text-white hover:bg-white hover:text-black"
              onClick={startGame}
            >
              Start Game
            </button>
          ) : (
            <div className="mt-12 flex flex-col items-center gap-2">
              Waiting for host <Spinner size="sm" />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
