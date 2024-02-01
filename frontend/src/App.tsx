import "./App.css";
import { Route, Routes } from "react-router-dom";
import Home from "./components/home.tsx";

function App() {
  return (
    <>
      <div className="flex min-h-screen flex-col">
        <Routes>
          <Route path="/" element={<Home />} />
        </Routes>
      </div>
    </>
  );
}

export default App;
