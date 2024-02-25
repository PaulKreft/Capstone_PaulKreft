import githubMarkUrl from "./../assets/github-mark.png";
import { Link } from "react-router-dom";
import { Spinner } from "./Spinner.tsx";
import { useState } from "react";

export default function Login() {
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const link = import.meta.env.PROD
    ? "/oauth2/authorization/github"
    : "http://localhost:8080/oauth2/authorization/github";

  if (isLoading) {
    return (
      <div className="flex flex-1 items-center justify-center">
        <Spinner />
      </div>
    );
  }

  return (
    <div className="mx-auto flex flex-1 flex-col items-center justify-center gap-10 pb-20">
      <a className="w-full" href={link} onClick={() => setIsLoading(true)}>
        <button className="flex h-16 w-full items-center justify-center rounded-lg border border-black px-5 py-4 text-xl font-light">
          <img className="mr-3 h-8" src={githubMarkUrl} alt="GitHub" />
          <span>Login with GitHub</span>
        </button>
      </a>{" "}
      <Link className="w-full" to={"/login/email"}>
        <button className="flex h-16 w-full items-center justify-center rounded-lg border border-black px-5 py-4 text-xl font-light">
          <span>Login with Email</span>
        </button>
      </Link>
    </div>
  );
}
