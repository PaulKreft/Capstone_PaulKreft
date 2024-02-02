import "./App.css";
import Play from "./components/play.tsx";
import { Route, Routes, useNavigate } from "react-router-dom";
import Home from "./components/home.tsx";
import Login from "./components/login.tsx";
import SignUp from "./components/sign-up.tsx";
import NotFound from "./components/not-found.tsx";
import { Header } from "./components/header.tsx";
import { useEffect, useState } from "react";
import { User } from "./types/User.ts";

import axios from "axios";

function App() {
  const navigate = useNavigate();
  const [user, setUser] = useState<User>(null);

  useEffect(() => {
    axios.get("/api/user").then((response) => {
      console.log(response);
      setUser(response.data);
    });
  }, []);

  const logout = () =>
    axios.get("/api/user/logout").then(() => {
      setUser(null);
      navigate("/");
    });

  return (
    <div className="flex min-h-screen flex-col">
      <Header isLoggedIn={!!user} logout={logout} />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/play" element={<Play />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path={"/*"} element={<NotFound />} />
      </Routes>
    </div>
  );
}

export default App;
