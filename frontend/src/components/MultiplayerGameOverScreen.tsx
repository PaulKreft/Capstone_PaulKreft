import React from "react";
import {Spinner} from "./Spinner.tsx";
import {Lobby} from "../types/Lobby.ts";
import {Player} from "../types/Player.ts";

type MultiplayerGameOverScreenProps = {
    lobby: Lobby;
    player: Player;
    startGame: () => void;
    backToLobby: () => void;
}

export const MultiplayerGameOverScreen: React.FC<MultiplayerGameOverScreenProps> = ({lobby, player, startGame, backToLobby}) => {
    return (
        <div className="flex flex-1 flex-col items-center justify-center gap-5">
            <div className="text-center text-5xl font-light">
                <span className="font-extrabold">{lobby.winner?.name}</span> won!
            </div>

            <div className="mb-16 mt-3 text-2xl font-thin">
                in <span className="font light">{((lobby.timeToBeat ?? 0) / 1000).toFixed(3)}</span> seconds
            </div>
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