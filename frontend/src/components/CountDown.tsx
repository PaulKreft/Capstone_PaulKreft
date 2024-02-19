import React, { useEffect, useState } from "react";

type CountDownProps = {
  trigger: () => void;
};

export const CountDown: React.FC<CountDownProps> = ({ trigger }) => {
  const [count, setCount] = useState<number>(3);

  useEffect(() => {
    const interval = setInterval(() => {
      if (count === 1) {
        trigger();
      } else {
        setCount((c) => c - 1);
      }
    }, 1000);

    return () => {
      clearInterval(interval);
    };
  }, [count]);

  return (
    <div className="flex flex-1 flex-col items-center justify-center">
      <div className="text-9xl transition-all">{count}</div>
    </div>
  );
};
