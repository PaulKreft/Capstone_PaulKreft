import "./App.css";
import { Play } from "./components/Play.tsx";
import { LobbyEntrance } from "./components/LobbyEntrance.tsx";
import { MultiPlayerLobby } from "./components/MultiPlayerLobby.tsx";
import { Route, Routes, useNavigate } from "react-router-dom";
import { Home } from "./components/Home.tsx";
import Login from "./components/Login.tsx";
import SignUp from "./components/SignUp.tsx";
import NotFound from "./components/NotFound.tsx";
import { Header } from "./components/Header.tsx";
import { useEffect, useState } from "react";
import { User } from "./types/User.ts";

import axios from "axios";
import Footer from "./components/Footer.tsx";
import { EmailLogin } from "./components/EmailLogin.tsx";
import { EmailSignUp } from "./components/EmailSignUp.tsx";
import { Profile } from "./components/Profile.tsx";
import { Spinner } from "./components/Spinner.tsx";

function App() {
  const navigate = useNavigate();
  const [user, setUser] = useState<User>(null);

  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    axios.get("/api/user").then((response) => {
      setUser(response.data);
      setIsLoading(false);
    });
  }, []);

  const login = (email: string, password: string) =>
    axios
      .get("/api/user", {
        auth: { username: email, password: password },
      })
      .then((response) => {
        setUser(response.data);
        navigate("/");
      });

  const logout = () =>
    axios.post("/api/logout").then(() => {
      setUser(null);
      navigate("/");
    });

  if (isLoading) {
    return (
      <div className="flex h-screen w-screen items-center justify-center">
        <Spinner />
      </div>
    );
  }

  return (
    <div className="flex min-h-screen w-screen flex-col">
      <Header isLoggedIn={!!user} logout={logout} userName={user?.name} />
      <Routes>
        <Route path="/" element={<Home user={user} />} />
        <Route path="/play" element={<Play userId={user?.id} />} />
        <Route path="/multiplayer" element={<LobbyEntrance user={user} />} />
        <Route path="/multiplayer/lobby/:id" element={<MultiPlayerLobby user={user} />} />
        <Route path="/profile" element={<Profile user={user} />} />
        <Route path="/login" element={<Login />} />
        <Route path="/login/email" element={<EmailLogin login={login} />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/signup/email" element={<EmailSignUp login={login} />} />
        <Route path={"/*"} element={<NotFound />} />
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
