import { Routes, Route } from "react-router";
import "./App.css";
import LandingPage from "./page/LandingPage";
import RootLayout from "./layout/RootLayout";
import HomePage from "./page/HomePage";
import HomeLayout from "./layout/HomeLayout";
import BottomNavigationLayout from "./layout/BottomNavigationLayout";
import QuizPage from "./page/QuizPage";
import AuthenticationLayout from "./layout/AuthenticationLayout";
import CreateLobbyPage from "./page/lobby/CreateLobbyPage";
import QuizDetailsPage from "./page/quiz/QuizDetailsPage";
import GitHubCallbackPage from "./page/auth/GitHubCallbackPage";
import ProfilePage from "./page/profile/ProfilePage";

function App() {
    return (
        <Routes>
            <Route element={<AuthenticationLayout />}>
                <Route element={<RootLayout />}>
                    <Route path="/landing" element={<LandingPage />} />

                    <Route element={<BottomNavigationLayout />}>
                        <Route element={<HomeLayout />}>
                            <Route path="/home" element={<HomePage />} />
                        </Route>
                        <Route path="/quizzes" element={<QuizPage />} />
                        <Route path="/profile" element={<ProfilePage />} />
                    </Route>

                    <Route
                        path="/lobbies/create"
                        element={<CreateLobbyPage />}
                    />
                    <Route
                        path="/quizzes/:quizId"
                        element={<QuizDetailsPage />}
                    />
                    <Route
                        path="/callback/github"
                        element={<GitHubCallbackPage />}
                    />
                </Route>
            </Route>
        </Routes>
    );
}

export default App;
