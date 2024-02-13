import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { Lobby } from "../types/Lobby.ts";

export function ActiveLobby() {
  const { id } = useParams();
  const [lobby, setLobby] = useState<Lobby>();

  useEffect(() => {
    axios.get(`/api/lobby/${id}`).then((response) => setLobby(response.data));
  }, [id]);

  if (!lobby) {
    return <div>No lobby here</div>;
  }

  return (
    <div className="flex flex-1 flex-col items-center justify-center">
      <div className="flex justify-evenly rounded-2xl border-2 border-black px-20 py-10">
        <div className="flex flex-col items-center gap-2">
          <div className="mb-5 text-xl font-extrabold">Players in lobby</div>
          {lobby.players.map((player) => (
            <div key={player.id}> {player.name}</div>
          ))}
          <button
            className="mt-10 h-max items-center rounded-lg border-2 border-black px-9 py-3 text-xl font-light bg-black text-white hover:bg-white hover:text-black"
            disabled={lobby.players.length <= 1}
          >
            Start Game
          </button>
        </div>
      </div>
    </div>
  );
}
