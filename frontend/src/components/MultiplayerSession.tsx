import { useParams } from "react-router-dom";
import React, { ChangeEvent, useEffect, useState } from "react";
import { Lobby } from "../types/Lobby.ts";
import { Spinner } from "./Spinner.tsx";
import axios from "axios";
import { MultiplayerPlay } from "./MultiplayerPlay.tsx";
import { Player } from "../types/Player.ts";
import { User } from "../types/User.ts";
import { MultiplayerLobby } from "./MultiplayerLobby.tsx";

type ActiveLobbyProps = {
  user: User;
};

export const MultiplayerSession: React.FC<ActiveLobbyProps> = ({ user }) => {
  const player: Player = { id: user ? user.id : "", name: user ? user.name : "" };

  const { id } = useParams();
  const [lobby, setLobby] = useState<Lobby>();

  const [startTime, setStartTime] = useState<number>();

  useEffect(() => {
    axios.get(`/api/lobby/${id}`).then((response) => {
      setLobby(response.data);
    });
    startListening();

    return () => {
      // on navigating away from the lobby component, leave the lobby
      axios.put(`/api/lobby/${id}/leave`, player).then((response) => {
        console.log(response.data);

        // if you were the last one in the lobby, delete the lobby
        if (!response.data.players.length) {
          axios.delete(`/api/lobby/${id}`).then((response) => console.log(response));
          return;
        }

        // otherwise, if was host, transfer ownership
        if (response.data.host.id === player.id) {
          axios
            .put(`/api/lobby`, {
              ...response.data,
              host: response.data.players[0],
            })
            .then((response) => console.log(response));
        }
      });
    };
  }, []);

  const startListening = (): void => {
    axios
      .get(`/api/lobby/long`)
      .then((response) => {
        setLobby(response.data);
        startListening();
      })
      .catch((error) => {
        console.log(error);
        startListening();
      });
  };

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

  useEffect(() => {
    if (!lobby) {
      return;
    }

    const isOver =
      lobby.losers.filter((loser: Player) => loser.id === player.id).length || lobby.winner?.id === player.id;

    if (isOver || !lobby.timeToBeat) {
      return;
    }

    if (startTime && Date.now() - startTime > lobby.timeToBeat) {
      onLose();
    }
  }, [lobby]);

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

  const onStreakToWinChange = (event: ChangeEvent<HTMLInputElement>) => {
    let newStreakToWin: number | null = Math.abs(parseInt(event.target.value));

    if (!event.target.value || event.target.value === "0") {
      newStreakToWin = null;
    }

    axios
      .put("/api/lobby", {
        ...lobby,
        streakToWin: newStreakToWin,
      })
      .then((response) => setLobby(response.data));
  };

  const onDifficultyChange = (event: ChangeEvent<HTMLSelectElement>) => {
    let newDifficulty: number = Math.abs(parseInt(event.target.value));
    if (!event.target.value) {
      newDifficulty = lobby.difficulty;
    }

    axios
      .put("/api/lobby", {
        ...lobby,
        difficulty: newDifficulty,
      })
      .then((response) => setLobby(response.data));
  };

  if (!user) {
    return <div>no user</div>;
  }

  if (lobby.losers.length && lobby.losers.length >= lobby.players.length - 1) {
    return (
      <div className="flex flex-1 flex-col items-center justify-center gap-5">
        <div className="text-center text-5xl font-light">
          <span className="font-extrabold">{lobby.winner?.name}</span> won!
        </div>

        <div className="mb-16 mt-3 text-2xl font-thin">in <span className="font light">{((lobby.timeToBeat ?? 0) / 1000).toFixed(3)}</span> seconds</div>
        {lobby.host.id === player.id ? (
          <button
            className="h-max items-center rounded-2xl border-2 border-black bg-black px-12 py-4 text-3xl font-light text-white hover:bg-white hover:text-black"
            onClick={startGame}
          >
            Rematch
          </button>
        ) : (
          <div className="mb-10">
            <Spinner size="md" />
          </div>
        )}
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
      <MultiplayerLobby
        lobby={lobby}
        player={player}
        onStreakToWinChange={onStreakToWinChange}
        onDifficultyChange={onDifficultyChange}
        startGame={startGame}
      />
    );
  }

  if (!startTime) {
    return <Spinner />;
  }

  return (
    <MultiplayerPlay
      playerId={player.id}
      difficulty={lobby.difficulty}
      gameStartTime={startTime}
      onSuccess={onSuccess}
      streakToWin={lobby.streakToWin}
    />
  );
};
