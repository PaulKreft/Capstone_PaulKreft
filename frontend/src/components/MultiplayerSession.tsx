import { useNavigate, useParams } from "react-router-dom";
import React, { ChangeEvent, useEffect, useState } from "react";
import { Lobby } from "../types/Lobby.ts";
import { Spinner } from "./Spinner.tsx";
import axios from "axios";
import { MultiplayerPlay } from "./MultiplayerPlay.tsx";
import { Player } from "../types/Player.ts";
import { User } from "../types/User.ts";
import { MultiplayerLobby } from "./MultiplayerLobby.tsx";
import { MultiplayerGameOverScreen } from "./MultiplayerGameOverScreen.tsx";
import { CountDown } from "./CountDown.tsx";

type ActiveLobbyProps = {
  user: User;
};

export const MultiplayerSession: React.FC<ActiveLobbyProps> = ({ user }) => {
  const navigate = useNavigate();
  const player: Player = { id: user ? user.id : "", name: user ? user.name : "" };

  const { id } = useParams();
  const [lobby, setLobby] = useState<Lobby>();

  const [startTime, setStartTime] = useState<number>();

  const [isGameOver, setIsGameOver] = useState<boolean>(false);

  useEffect(() => {
    if (lobby?.losers.length && lobby.losers.length >= lobby.players.length - 1) {
      setIsGameOver(true);
      return;
    }
    setIsGameOver(false);
  }, [lobby?.losers, lobby?.players]);

  useEffect(() => {
    axios.get(`/api/lobby/${id}`).then((response) => {
      setLobby(response.data);
    });
    pollForLobbyChanges();

    return () => {
      axios.put(`/api/lobby/${id}/leave`, player).then(() => {});
    };
  }, []);

  const pollForLobbyChanges = (): void => {
    axios
      .get(`/api/lobby/${id}/long`)
      .then((response) => {
        setLobby(response.data);
        pollForLobbyChanges();
      })
      .catch((error) => {
        if (error.response.status === 404) {
          return;
        }
        if (error.response.status === 410) {
          navigate("/multiplayer");
          return;
        }
        pollForLobbyChanges();
      });
  };

  const startTimer = (): void => {
    setStartTime(Date.now());
  };

  const initiateGame = (): void => {
    axios
      .put(`/api/lobby`, {
        ...lobby,
        isGameInProgress: true,
        isGameOver: false,
        timeToBeat: null,
        winner: null,
        losers: [],
        lastGameStarted: new Date(Date.now()),
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

  const navigateToLobby = (): void => {
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

  const onChangeStreakToWin = (event: ChangeEvent<HTMLInputElement>) => {
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

  const onChangeDifficulty = (event: ChangeEvent<HTMLSelectElement>) => {
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

  if (isGameOver) {
    return (
      <MultiplayerGameOverScreen lobby={lobby} player={player} startGame={initiateGame} backToLobby={navigateToLobby} />
    );
  }

  if (!lobby.isGameInProgress) {
    return (
      <MultiplayerLobby
        lobby={lobby}
        player={player}
        onChangeStreakToWin={onChangeStreakToWin}
        onChangeDifficulty={onChangeDifficulty}
        startGame={initiateGame}
      />
    );
  }

  return startTime && startTime > new Date(lobby.lastGameStarted ?? 0).getTime() ? (
    <MultiplayerPlay
      playerId={player.id}
      difficulty={lobby.difficulty}
      gameStartTime={startTime}
      onSuccess={onSuccess}
      streakToWin={lobby.streakToWin}
    />
  ) : (
    <CountDown key={`${lobby.lastGameStarted}-${player.id}`} trigger={startTimer} />
  );
};
