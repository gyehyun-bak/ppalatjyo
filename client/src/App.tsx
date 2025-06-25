import { Routes, useNavigate, useHref, Route } from 'react-router';
import './App.css';
import { HeroUIProvider } from '@heroui/react';
import Landing from './page/Landing';

function App() {
    const navigate = useNavigate();

    return (
        <HeroUIProvider navigate={navigate} useHref={useHref}>
            <Routes>
                <Route path="/landing" element={<Landing />} />
            </Routes>
        </HeroUIProvider>
    );
}

export default App;
