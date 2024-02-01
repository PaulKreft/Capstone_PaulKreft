import "./App.css";
import Play from "./components/play.tsx";
import { Route, Routes } from "react-router-dom";
import Home from "./components/home.tsx";
import NotFound from "./components/not-found.tsx";

function App() {
  return (
    <>
      <div className="flex min-h-screen flex-col">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/play" element={<Play />} />
          <Route path={"/*"} element={<NotFound />} />
        </Routes>
      </div>
    </>
  );
}

export default App;
