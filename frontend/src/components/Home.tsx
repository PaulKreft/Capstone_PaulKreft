import { Link } from "react-router-dom";
import { User } from "../types/User.ts";
import React from "react";

type HomeProps = {
  user: User;
};

export const Home: React.FC<HomeProps> = ({ user }) => {
  return (
    <div className="flex flex-1 flex-col items-center justify-center pb-52">
      <h1 className="max-w-96 cursor-default text-center text-6xl font-extrabold leading-snug sm:max-w-none sm:text-8xl sm:leading-snug">
        Welcome to <span className="cursor-magic text-[#3A0842] hover:text-[#c5f7bd]">H</span>
        <span className="cursor-magic text-[#9AB87A] hover:text-[#654785]">e</span>
        <span className="cursor-magic text-[#444B6E] hover:text-[#bbb491]">x</span>
        <span className="cursor-magic text-[#247BA0] hover:text-[#db845f]">H</span>
        <span className="cursor-magic text-[#DC851F] hover:text-[#237ae0]">e</span>
        <span className="cursor-magic text-[#F8F991] hover:text-[#07066e]">x</span>
        <span className="cursor-magic text-[#720026] hover:text-[#8dffd9]">!</span>
      </h1>
      <div className="mt-20 flex flex-col items-center gap-10 md:mt-32 md:flex-row md:gap-20">
        {user && (
          <Link to="/multiplayer">
            <button className="rounded-3xl bg-black px-16 py-6 text-4xl text-white">Multiplayer</button>
          </Link>
        )}
        <Link to="/play">
          <button className="rounded-3xl bg-black px-16 py-6 text-4xl text-white">Classic</button>
        </Link>
      </div>
    </div>
  );
};
