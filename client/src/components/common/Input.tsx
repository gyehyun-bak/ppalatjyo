import {
    Input as HeroInput,
    type InputProps as HeroInputProps,
} from '@heroui/react';

interface InputProps extends HeroInputProps {
    showLength?: boolean;
}

export default function Input({
    showLength = false,
    maxLength,
    value,
    ...rest
}: InputProps) {
    const currentValue = typeof value === 'string' ? value : '';

    return (
        <div className="flex flex-col items-end">
            <HeroInput {...rest} value={currentValue} maxLength={maxLength} />
            {showLength && maxLength !== undefined && (
                <div className="text-sm text-gray-400 select-none">
                    {currentValue.length} / {maxLength}
                </div>
            )}
        </div>
    );
}
