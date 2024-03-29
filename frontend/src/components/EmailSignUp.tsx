import React, { FormEvent, useEffect, useState } from "react";
import { cn } from "../lib/utils.ts";
import axios from "axios";
import { Spinner } from "./Spinner.tsx";

type EmailSignUpProps = {
  login: (email: string, password: string) => void;
};

export const EmailSignUp: React.FC<EmailSignUpProps> = ({ login }) => {
  const [isSigningUp, setIsSigningUp] = useState<boolean>(false);

  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const [isPasswordValid, setIsPasswordValid] = useState<boolean>(false);
  const [isEmailValid, setIsEmailValid] = useState<boolean>(false);

  useEffect(() => {
    const regX: RegExp = /^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$/;
    setIsEmailValid(regX.test(email));
  }, [email]);

  useEffect(() => {
    const regX: RegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[ !@#$%^&*])[A-Za-z\d !@#$%^&*]{8,100}$/;
    setIsPasswordValid(regX.test(password));
  }, [password]);

  const signUpWithEmail = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    setIsSigningUp(true);
    axios
      .post("/api/signup/email", { email, password })
      .then(() => {
        login(email, password);
      })
      .catch((e) => console.log(e));
  };

  if (isSigningUp) {
    return (
      <div className="flex flex-1 items-center justify-center">
        <Spinner />
      </div>
    );
  }

  return (
    <div className="mx-auto flex flex-1 flex-col items-center justify-center pb-20">
      <div className="flex flex-col items-center rounded-2xl border-black px-20 pb-24 pt-12 sm:border-2">
        <h2 className="pb-16 text-4xl">Sign up with Email</h2>
        <form className="" onSubmit={signUpWithEmail} noValidate>
          <div className="mb-1 pl-1 text-lg">Email</div>
          <input
            className={cn(
              "flex h-max items-center rounded-lg border-2 border-black px-5 py-4 text-lg font-light",
              email ? (isEmailValid ? "focus:border-green" : "border-red") : "focus:border-black",
            )}
            type="email"
            placeholder="example@domain.com"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
          <div className={cn("mt-1 text-center text-sm", email && !isEmailValid ? "text-red" : "text-transparent")}>
            Invalid email format
          </div>

          <div className="mb-1 mt-1 pl-1 text-lg">Password</div>
          <input
            className={cn(
              "flex h-max items-center rounded-lg border-2 border-black px-5 py-4 text-lg font-light",
              password ? (isPasswordValid ? "focus:border-green" : "border-red") : "focus:border-black",
            )}
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
          <div
            className={cn("mt-1 text-center text-sm", password && !isPasswordValid ? "text-red" : "text-transparent")}
          >
            Invalid password format
          </div>
          <button
            className="mt-4 w-full rounded-lg bg-black px-3 py-2 text-white disabled:bg-black/20"
            type="submit"
            disabled={!isEmailValid || !isPasswordValid || isSigningUp}
          >
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
};
