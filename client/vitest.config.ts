import { defineConfig } from 'vitest/config';

export default defineConfig({
    test: {
        globals: true,
        environment: 'jsdom',
        coverage: {
            provider: 'v8',
            exclude: [
                'src/main.tsx',
                '**/*.d.ts', // 모든 타입 선언 파일 제외
            ],
        },
        setupFiles: ['src/vitest.setup.ts'],
    },
});
