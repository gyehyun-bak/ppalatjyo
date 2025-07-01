import { Radio, RadioGroup, Textarea } from '@heroui/react';
import { useState } from 'react';
import Input from '../../components/common/Input';

export default function CreateQuizPage() {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [visibility, setVisibility] = useState('PUBLIC');

    return (
        <div>
            <Input label="퀴즈 이름" value={title} onValueChange={setTitle} />
            <Textarea
                label="퀴즈 설명"
                value={description}
                onValueChange={setDescription}
            />
            <RadioGroup
                id="visibility"
                label="공개 설정"
                value={visibility}
                onValueChange={setVisibility}
            >
                <Radio
                    value="PUBLIC"
                    description="누구나 퀴즈를 조회하고 로비를 만들 수 있습니다."
                >
                    공개
                </Radio>
                <Radio
                    value="PRIVATE"
                    description="작성자만 퀴즈를 조회하고 로비를 만들 수 있습니다."
                >
                    비공개
                </Radio>
            </RadioGroup>
        </div>
    );
}
