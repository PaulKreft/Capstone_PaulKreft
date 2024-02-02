import "./App.css";
import Play from "./components/play.tsx";
import { Route, Routes } from "react-router-dom";
import Home from "./components/home.tsx";
import Login from "./components/login.tsx";
import SignUp from "./components/sign-up.tsx";
import NotFound from "./components/not-found.tsx";
import { Header } from "./components/header.tsx";

function App() {
    return (
        <>
            <div className="flex min-h-screen flex-col">
                <Header />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/play" element={<Play />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/signup" element={<SignUp />} />
                    <Route path={"/*"} element={<NotFound />} />
                </Routes>
            </div>
        </>
    );
}

export default App;
