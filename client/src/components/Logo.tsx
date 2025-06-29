type LogoProps = {
    size?: number | string; // number(px 단위) 또는 string("2rem" 등) 모두 허용
};

export default function Logo({ size = 44 }: LogoProps) {
    return (
        <svg
            width={size}
            height={size}
            viewBox="0 0 44 44"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
        >
            <path
                d="M27 4L9 26.2H21L19 39.2L37 17.2H25L27 4Z"
                fill="#FFBF00"
                stroke="#FFBF00"
                strokeWidth="4"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <circle cx="23" cy="22" r="2" fill="#2C2C2C" />
            <circle cx="30" cy="22" r="2" fill="#2C2C2C" />
            <circle cx="16" cy="22" r="2" fill="#2C2C2C" />
        </svg>
    );
}
