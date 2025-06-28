import { Routes, Route } from 'react-router';
import './App.css';
import Landing from './page/Landing';
import RootLayout from './layout/RootLayout';

function App() {
    return (
        <Routes>
            <Route element={<RootLayout />}>
                <Route path="/landing" element={<Landing />} />
            </Route>
        </Routes>
    );
}

export default App;
