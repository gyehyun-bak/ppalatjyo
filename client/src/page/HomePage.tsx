import { useQuery } from '@tanstack/react-query';
import { getLobbies } from '../api/lobby.api';
import LobbyItem from '../components/lobby/LobbyItem';
import { addToast, Button } from '@heroui/react';
import { useNavigate } from 'react-router';

export default function HomePage() {
    const navigate = useNavigate();

    const { data, isPending, refetch, isError, isSuccess } = useQuery({
        queryKey: ['lobbies'],
        queryFn: getLobbies,
    });

    if (isError) {
        addToast({
            title: '로비를 불러오는 중 문제가 발생하였습니다.',
            color: 'danger',
        });
    }

    const handleCreateLobby = () => {
        navigate('/lobbies/create');
    };

    return (
        <div>
            <h1>Home</h1>
            <Button
                aria-label="refresh"
                isLoading={isPending}
                onPress={() => refetch()}
            >
                새로고침
            </Button>
            <Button aria-label="create-lobby" onPress={handleCreateLobby}>
                로비 만들기
            </Button>
            {isSuccess && data.lobbies.length > 0 ? (
                <ul>
                    {data.lobbies.map((lobby) => (
                        <LobbyItem key={lobby.id} lobby={lobby} />
                    ))}
                </ul>
            ) : (
                <p>No lobbies available.</p>
            )}
        </div>
    );
}
