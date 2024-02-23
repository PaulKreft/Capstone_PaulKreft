import { Spinner } from "./Spinner.tsx";
import React, { ChangeEvent, useEffect, useState } from "react";
import { Player } from "../types/Player.ts";
import { Lobby } from "../types/Lobby.ts";
import copyToClipBoardIconUrl from "./../assets/copy-to-clipboard-icon.svg";
import axios from "axios";
import { DuelStatistics } from "../types/DuelStatistics.ts";

type MultiplayerLobbyProps = {
  lobby: Lobby;
  player: Player;
  onChangeStreakToWin: (event: ChangeEvent<HTMLInputElement>) => void;
  onChangeDifficulty: (event: ChangeEvent<HTMLSelectElement>) => void;
  startGame: () => void;
};

const EASY = 1;
const MEDIUM = 2;
const HARD = 4;

const DUEL_CAPACITY = 2;
const DUEL_TOTAL_PLAYERS = 2;

export const MultiplayerLobby: React.FC<MultiplayerLobbyProps> = ({
  lobby,
  player,
  onChangeStreakToWin,
  onChangeDifficulty,
  startGame,
}) => {
  const [duelStatistics, setDuelStatistics] = useState<DuelStatistics>();

  useEffect(() => {
    if (lobby.capacity === DUEL_CAPACITY && lobby.players.length === DUEL_TOTAL_PLAYERS) {
      const opponent: Player | undefined = lobby.players.find((opponent) => opponent.id != player.id);
      if (!opponent) {
        return;
      }

      const opponentId: string = opponent.id;

      axios.get(`/api/user/${player.id}/statistics/duel?opponentId=${opponentId}`).then((response) =>
        setDuelStatistics({
          gamesPlayed: response.data.gamesPlayed,
          gamesWon: response.data.gamesWon,
        }),
      );
    }
  }, [lobby.players]);

  return (
    <div className="flex flex-1 flex-col items-center justify-center px-2">
      <div className="flex w-full items-center justify-evenly rounded-2xl border-2 border-black py-10 xs:w-max xs:px-20">
        <div className="flex flex-col items-center">
          <div className="text-xl font-extrabold">{`${lobby.host.name.substring(0, 15)}'s lobby`}</div>
          <div
            className="flex cursor-pointer items-center"
            onClick={() => {
              navigator.clipboard.writeText(lobby.id);
            }}
          >
            <div className="text-lg font-light">{lobby.id}</div>
            <img className="h-7" src={copyToClipBoardIconUrl} alt="copy to clipboard" />
          </div>

          {lobby.host.id !== player.id && (
            <div className="mt-5 flex gap-5">
              <div className="flex flex-col items-center">
                <span className="font-light">Streak to win</span>
                <span className="font-bold">{lobby.streakToWin ?? "1"}</span>
              </div>
              <div className="flex flex-col items-center">
                <span className="font-light">Difficulty</span>
                <span className="font-bold">
                  {lobby.difficulty === 1 ? "Easy" : lobby.difficulty === 2 ? "Medium" : "Hard"}
                </span>
              </div>
            </div>
          )}

          {duelStatistics && (
            <div className="mt-10 flex flex-col items-center">
              <span className="font-light">Record</span>
              <span className="font-bold">
                {duelStatistics.gamesWon[lobby.difficulty === 1 ? "easy" : lobby.difficulty === 2 ? "medium" : "hard"]}{" "}
                -{" "}
                {duelStatistics.gamesPlayed[
                  lobby.difficulty === 1 ? "easy" : lobby.difficulty === 2 ? "medium" : "hard"
                ] -
                  duelStatistics.gamesWon[lobby.difficulty === 1 ? "easy" : lobby.difficulty === 2 ? "medium" : "hard"]}
              </span>
            </div>
          )}

          <div className="mt-4 flex flex-col gap-1">
            {lobby.players.map((player) => (
              <div key={player.id}>
                <div>{player.name}</div>
                <div className="mb-1 mt-1 h-[1px] w-full bg-gradient-to-r from-white via-black to-white" />
              </div>
            ))}
          </div>
          <div className={lobby.host.id !== player.id ? "hidden" : "mt-10 flex flex-col gap-2"}>
            <div className="flex items-center justify-between">
              <label htmlFor="difficultySelect" className="mr-5 font-medium">
                Difficulty
              </label>
              <select
                className="h-max items-center rounded-lg border-2 border-transparent bg-white px-3 py-1 font-light text-black"
                id="difficultySelect"
                value={lobby.difficulty}
                onChange={onChangeDifficulty}
              >
                <option value={EASY}>Easy</option>
                <option value={MEDIUM}>Medium</option>
                <option value={HARD}>Hard</option>
              </select>
            </div>
            <div className="flex items-center justify-between">
              <label htmlFor="streakToWinInput" className="mr-5 font-medium">
                Streak to win
              </label>
              <input
                id="streakToWinInput"
                className="h-max w-20 items-center rounded-lg border border-black bg-white px-3 py-1 font-light text-black"
                value={lobby.streakToWin?.toString() || ""}
                onChange={onChangeStreakToWin}
                type="number"
                min="1"
              />
            </div>
          </div>
          {lobby.players.length < 2 && lobby.host.id === player.id && (
            <div className="mt-12 flex flex-col items-center gap-2">
              Waiting for players <Spinner size="sm" />
            </div>
          )}

          {lobby.players.length > 1 && lobby.host.id === player.id && (
            <button
              className="mt-6 h-max items-center rounded-lg border-2 border-black bg-black px-9 py-3 text-xl font-light text-white hover:bg-white hover:text-black disabled:border-transparent disabled:bg-black/70 disabled:text-white/80"
              onClick={startGame}
              disabled={!lobby.streakToWin}
            >
              Start Game
            </button>
          )}
          {lobby.host.id !== player.id && (
            <div className="mt-12 flex flex-col items-center gap-2">
              Waiting for host <Spinner size="sm" />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
