import { Routes, useNavigate, useHref } from 'react-router';
import './App.css';
import { HeroUIProvider } from '@heroui/react';

function App() {
    const navigate = useNavigate();

    return (
        <HeroUIProvider navigate={navigate} useHref={useHref}>
            <Routes></Routes>
        </HeroUIProvider>
    );
}

export default App;
