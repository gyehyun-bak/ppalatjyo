import { Routes, Route } from 'react-router';
import './App.css';
import Landing from './page/Landing';
import RootLayout from './layout/RootLayout';
import Home from './page/Home';
import HomeLayout from './layout/HomeLayout';

function App() {
    return (
        <Routes>
            <Route element={<RootLayout />}>
                <Route path="/landing" element={<Landing />} />
                <Route element={<HomeLayout />}>
                    <Route path="/home" element={<Home />} />
                </Route>
            </Route>
        </Routes>
    );
}

export default App;
