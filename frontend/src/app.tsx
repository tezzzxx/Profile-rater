import { NavLink, Navigate, Route, Routes } from "react-router";
import RatingPage from "./Pages/ratingPage";
import PostPage from "./Pages/postProfilePage";
import ResultPage from "./Pages/resultPage";

export default function App() {
    return (
        <>
            <header>
                <h1>Profile Rater</h1>
                <nav>
                    <NavLink to="/rating">Rating</NavLink>
                    <NavLink to="/postProfile">Post</NavLink>
                    <NavLink to="/result">Result</NavLink>
                </nav>
            </header>
            <main>
                <Routes>
                    <Route path="/" element={<Navigate to="/rating" replace />} />
                    <Route path="/rating" element={<RatingPage />} />
                    <Route path="/postProfile" element={<PostPage />} />
                    <Route path="/result" element={<ResultPage />} />
                </Routes>
            </main>
        </>
    );
}