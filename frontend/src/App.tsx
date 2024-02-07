import "./App.css";
import { Play } from "./components/play.tsx";
import { Route, Routes, useNavigate } from "react-router-dom";
import Home from "./components/home.tsx";
import Login from "./components/login.tsx";
import SignUp from "./components/sign-up.tsx";
import NotFound from "./components/not-found.tsx";
import { Header } from "./components/header.tsx";
import { useEffect, useState } from "react";
import { User } from "./types/User.ts";

import axios from "axios";
import Footer from "./components/footer.tsx";
import { EmailLogin } from "./components/email-login.tsx";
import { EmailSignUp } from "./components/email-sign-up.tsx";

function App() {
  const navigate = useNavigate();
  const [user, setUser] = useState<User>(null);

  useEffect(() => {
    axios.get("/api/user").then((response) => setUser(response.data));
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

  return (
    <div className="flex min-h-screen w-screen flex-col">
      <Header isLoggedIn={!!user} logout={logout} />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/play" element={<Play userId={user?.id}/>} />
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
