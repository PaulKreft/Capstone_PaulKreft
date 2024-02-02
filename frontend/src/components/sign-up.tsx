import githubMarkUrl from "../assets/github-mark.png";

export default function SignUp() {
  const link = import.meta.env.PROD
    ? "/oauth2/authorization/github"
    : "http://localhost:8080/oauth2/authorization/github";

  return (
    <div className="mx-auto flex flex-1 items-center justify-center pb-20">
      <button className="flex h-max items-center rounded-lg border border-black px-5 py-4 text-xl font-light">
        <img className="mr-3 h-8" src={githubMarkUrl} alt="GitHub" />
        <a href={link}>Sign up with GitHub</a>
      </button>
    </div>
  );
}
