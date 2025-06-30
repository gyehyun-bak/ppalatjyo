import { beforeAll, afterEach, afterAll } from 'vitest';
import { server } from './mocks/server.js';
import '@testing-library/jest-dom';

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

vi.mock('zustand'); // to make it work like Jest (auto-mocking)
