import React, { useEffect, useState } from "react";
import { cn } from "../lib/utils.ts";

import { Tile } from "./Tile.tsx";
import { addHexColors } from "../lib/hexUtils.ts";

type TileConfigurationProps = {
  children: React.ReactNode;
  baseConfig: string[];
  overEvent: (result: "lost" | "won") => void;
};

type Tile = {
  id: string;
  color: string;
};

const WHITE = "#ffffff";
const BLACK = "#000000";

export const TileConfiguration: React.FC<TileConfigurationProps> = ({ children, baseConfig, overEvent }) => {
  const [currentConfig, setCurrentConfig] = useState<Tile[]>([]);

  const [isLocked, setIsLocked] = useState<boolean>(false);

  const [actualMatch, setActualMatch] = useState<string>();
  const [wrongMatch, setWrongMatch] = useState<string>();
  const [sourceTile, setSourceTile] = useState<Tile>();

  useEffect(() => {
    setCurrentConfig(baseConfig.map((color, index) => ({ id: `${index}-classic`, color })));
    resetState();
  }, [baseConfig]);

  const resetState = (): void => {
    const isGameInterrupted: boolean = !!currentConfig.filter((tile) => !(tile.color === WHITE || tile.color === BLACK))
      .length;

    if (isGameInterrupted && !isLocked) {
      overEvent("lost");
    }

    setIsLocked(false);
    setSourceTile(undefined);
    setActualMatch(undefined);
    setWrongMatch(undefined);
  };

  const selectTile = (tile: Tile): void => {
    if (isLocked || !tile) {
      return;
    }

    if (!sourceTile) {
      setSourceTile(tile);
      return;
    }

    if (sourceTile.id === tile.id) {
      setSourceTile(undefined);
      return;
    }

    combineTiles(sourceTile, tile);
  };

  const getMatchingTile = (tile: Tile): string => {
    const match: Tile | undefined = currentConfig.find(
      (matchingTile) => addHexColors(matchingTile.color, tile.color) === WHITE,
    );

    return match ? match.id : "";
  };

  const combineTiles = (giving: Tile, receiving: Tile): void => {
    if (!giving || !receiving) {
      return;
    }

    const newColor = addHexColors(receiving.color, giving.color);

    if (newColor !== WHITE) {
      setActualMatch(getMatchingTile(giving));
      setWrongMatch(receiving.id);
      overEvent("lost");
      setIsLocked(true);
      return;
    }

    const newConfig = currentConfig.map((tile) => {
      if (tile?.id === giving.id) {
        return { ...tile, color: BLACK };
      }
      if (tile?.id === receiving.id) {
        return { ...tile, color: newColor };
      }
      return tile;
    });

    setSourceTile(undefined);

    if (!newConfig.filter((tile) => !(tile.color === WHITE || tile.color === BLACK)).length) {
      overEvent("won");
      setIsLocked(true);
    }

    setCurrentConfig(newConfig);
  };

  return (
    <div
      className={cn(
        "relative flex flex-wrap justify-center gap-4",
        baseConfig.length === 6 ? "max-w-80 sm:max-w-96" : "max-w-[24rem] sm:max-w-[32rem]",
      )}
    >
      {children}
      {currentConfig.map((tile) => (
        <Tile
          key={tile.id}
          onClick={() => selectTile(tile)}
          color={tile.color}
          isSelected={sourceTile?.id === tile.id}
          isWrong={wrongMatch === tile.id}
          isCorrect={actualMatch === tile.id}
          isClickable={!(isLocked || tile.color === WHITE || tile.color === BLACK)}
        />
      ))}
    </div>
  );
};
