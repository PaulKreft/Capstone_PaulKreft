import { useParams } from "react-router-dom";
import React, { ChangeEvent, useEffect, useState } from "react";
import { Lobby } from "../types/Lobby.ts";
import Spinner from "./Spinner.tsx";
import axios from "axios";
import { MultiPlay } from "./MultiPlay.tsx";
import { Player } from "../types/Player.ts";
import { User } from "../types/User.ts";

type ActiveLobbyProps = {
  user: User;
};

export const MultiPlayerLobby: React.FC<ActiveLobbyProps> = ({ user }) => {
  const { id } = useParams();
  const [lobby, setLobby] = useState<Lobby>();

  const [streakToWin, setStreakToWin] = useState<number>(3);

  const [startTime, setStartTime] = useState<number>();

  const player: Player = { id: user ? user.id : "", name: user ? user.name : "" };

  useEffect(() => {
    const interval = setInterval(() => {
      axios.get(`/api/lobby/${id}`).then((response) => setLobby(response.data));
    }, 3000);
    return () => {
      axios.put(`/api/lobby/${id}/leave`, player).then((response) => setLobby(response.data));
      clearInterval(interval);
    };
  }, [id]);

  useEffect(() => {
    setStartTime(Date.now());
  }, [lobby?.lastGameStarted]);

  const startGame = (): void => {
    axios
      .put(`/api/lobby`, {
        ...lobby,
        isGameInProgress: true,
        isGameOver: false,
        timeToBeat: null,
        winner: null,
        losers: [],
        lastGameStarted: Date.now(),
      })
      .then((response) => {
        setLobby(response.data);
      });
  };

  const backToLobby = (): void => {
    axios
      .put(`/api/lobby`, {
        ...lobby,
        isGameInProgress: false,
        isGameOver: false,
        timeToBeat: null,
        winner: null,
        losers: [],
      })
      .then((response) => setLobby(response.data));
  };

  const onSuccess = (time: number): void => {
    axios.put(`/api/lobby/${id}/setWinner`, { player, time }).then((response) => setLobby(response.data));
  };

  const onLose = (): void => {
    axios.put(`/api/lobby/${id}/setLoser`, player).then((response) => setLobby(response.data));
  };

  if (!lobby) {
    return (
      <div className="flex flex-1 flex-col items-center justify-center">
        <Spinner />
      </div>
    );
  }

  if (!user) {
    return <div>no user</div>;
  }

  if (lobby.losers.length && lobby.losers.length >= lobby.players.length - 1) {
    return (
      <div className="flex flex-1 flex-col items-center justify-center gap-5">
        <div className="mb-16 text-5xl font-light">
          <span className="font-extrabold">{lobby.winner?.name}</span> won!
        </div>
        <button
          className="h-max items-center rounded-2xl border-2 border-black bg-black px-12 py-4 text-3xl font-light text-white hover:bg-white hover:text-black"
          onClick={startGame}
        >
          Rematch
        </button>
        <button
          className="h-max items-center rounded-lg border-2 border-black px-3 py-1 font-light hover:bg-black hover:text-white"
          onClick={backToLobby}
        >
          Back to lobby
        </button>
      </div>
    );
  }

  if (!lobby.isGameInProgress) {
    return (
      <div className="flex flex-1 flex-col items-center justify-center">
        <div className="flex justify-evenly rounded-2xl border-2 border-black px-20 py-10">
          <div className="flex flex-col items-center gap-2">
            <div className="mb-5 text-xl font-extrabold">Players in lobby</div>
            {lobby.players.map((player) => (
              <div key={player.id}> {player.name}</div>
            ))}
            <div className="mt-10">
              <span className="font-medium mr-5">Streak to win</span>
              <input
                className="h-max w-20 items-center rounded-lg border-2 border-black bg-white px-3 py-1 font-light text-black"
                value={streakToWin.toString()}
                onChange={(event: ChangeEvent<HTMLInputElement>) => setStreakToWin(parseInt(event.target.value))}
                type="number"
              />
            </div>
            <button
              className="mt-6 h-max items-center rounded-lg border-2 border-black bg-black px-9 py-3 text-xl font-light text-white hover:bg-white hover:text-black disabled:bg-black disabled:text-white"
              onClick={startGame}
              disabled={lobby.players.length <= 1}
            >
              Start Game
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (!startTime) {
    return <Spinner />;
  }

  return (
    <MultiPlay
      playerId={player.id}
      difficulty={lobby.difficulty}
      timeToBeat={lobby.timeToBeat}
      gameStartTime={startTime}
      onLose={onLose}
      onSuccess={onSuccess}
      streakToWin={streakToWin}
    />
  );
};
