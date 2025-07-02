import type { LobbyResponse } from '../../api/types/lobby/LobbyResponse';

interface LobbyItemProps {
    lobby: LobbyResponse;
}

export default function LobbyItem({ lobby }: LobbyItemProps) {
    return (
        <li>
            <h2>{lobby.name}</h2>
            <p>Host: {lobby.host.nickname}</p>
            <p>Quiz: {lobby.quiz.title}</p>
            <p>Max Users: {lobby.options.maxUsers}</p>
            <p>Min Per Game: {lobby.options.minPerGame}</p>
            <p>Seconds Per Question: {lobby.options.secPerQuestion}</p>
            <p>Current Users: {lobby.currentUserCount}</p>
        </li>
    );
}
