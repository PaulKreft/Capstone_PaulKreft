import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="flex flex-1 flex-col items-center justify-center pb-52">
      <h1 className="text-center text-8xl font-extrabold leading-snug">
        Welcome to <span className="text-[#3A0842]">H</span>
        <span className="text-[#9AB87A]">e</span>
        <span className="text-[#444B6E]">x</span>
        <span className="text-[#247BA0]">H</span>
        <span className="text-[#DC851F]">e</span>
        <span className="text-[#F8F991]">x</span>
        <span className="text-[#720026]">!</span>
      </h1>
      <Link to="/play">
        <button className="mt-32 rounded-3xl bg-black px-16 py-6 text-4xl text-white">Play!</button>
      </Link>
    </div>
  );
}
