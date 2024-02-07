import React, { FormEvent, useEffect, useState } from "react";
import { cn } from "../lib/utils.ts";

type EmailLoginProps = {
  login: (email: string, password: string) => void;
};

export const EmailLogin: React.FC<EmailLoginProps> = ({ login }) => {
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

  const loginWithEmail = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    login(email, password);
  };

  return (
    <div className="mx-auto flex flex-1 flex-col items-center justify-center pb-20">
      <div className="flex flex-col items-center  rounded-2xl border-2 border-black px-20 pb-24 pt-12">
        <h2 className="pb-16 text-4xl">Log in with Email</h2>
        <form className="" onSubmit={loginWithEmail} noValidate>
          <div className="mb-1 pl-1 text-lg">Email</div>
          <input
            className={cn(
              "flex h-max items-center rounded-lg border-2 border-black px-5 py-4 text-lg font-light",
              email ? (isEmailValid ? "focus:border-[#73BA9B]" : "border-[#BA2D0B]") : "focus:border-black",
            )}
            type="email"
            placeholder="example@domain.com"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
          <div
            className={cn("mt-1 text-center text-sm", email && !isEmailValid ? "text-[#BA2D0B]" : "text-transparent")}
          >
            Invalid email format
          </div>

          <div className="mb-1 mt-1 pl-1 text-lg">Password</div>
          <input
            className={cn(
              "flex h-max items-center rounded-lg border-2 border-black px-5 py-4 text-lg font-light",
              password ? (isPasswordValid ? "focus:border-[#73BA9B]" : "border-[#BA2D0B]") : "focus:border-black",
            )}
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
          <div
            className={cn(
              "mt-1 text-center text-sm",
              password && !isPasswordValid ? "text-[#BA2D0B]" : "text-transparent",
            )}
          >
            Invalid password format
          </div>
          <button
            className="mt-4 w-full rounded-lg bg-black px-3 py-2 text-white disabled:bg-black/20"
            type="submit"
            disabled={!isEmailValid || !isPasswordValid}
          >
            Log In
          </button>
        </form>
      </div>
    </div>
  );
};
