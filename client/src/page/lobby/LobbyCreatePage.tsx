import { NumberInput } from '@heroui/react';
import Input from '../../components/common/Input';
import { useLobbyCreateStore } from '../../store/useLobbyCreateStore';
import { useState } from 'react';

export default function LobbyCreatePage() {
    const store = useLobbyCreateStore();

    const [name, setName] = useState(store.name);
    const [password, setPassword] = useState('');
    const [maxUsers, setMaxUsers] = useState(store.maxUsers);
    const [minPerGame, setMinPerGame] = useState(store.minPerGame);
    const [secPerQuestion, setSecPerQuestion] = useState(store.secPerQuestion);

    return (
        <form>
            <Input label={'로비 이름'} value={name} onValueChange={setName} />
            <Input
                label={'비밀번호'}
                value={password}
                onValueChange={setPassword}
            />
            <NumberInput
                label={'최대 인원'}
                minValue={1}
                maxValue={100}
                value={maxUsers}
                onValueChange={setMaxUsers}
            />
            <NumberInput
                label={'게임 시간 (분)'}
                minValue={1}
                maxValue={60}
                value={minPerGame}
                onValueChange={setMinPerGame}
            />
            <NumberInput
                label={'문제당 제한 시간 (초)'}
                minValue={1}
                maxValue={300}
                value={secPerQuestion}
                onValueChange={setSecPerQuestion}
            />
        </form>
    );
}
