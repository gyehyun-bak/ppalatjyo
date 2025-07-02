import { http, HttpResponse } from 'msw';
import type { JoinAsGuestRequestDto } from '../types/api/auth/JoinAsGuestRequestDto';
import type { JoinAsGuestResponseDto } from '../types/api/auth/JoinAsGuestResponseDto';
import { baseUrl } from './server';

export const handlers = [
    http.get('https://api.example.com/user', () => {
        return HttpResponse.json({
            id: 'abc-123',
            firstName: 'John',
            lastName: 'Maverick',
        });
    }),
    http.post<never, JoinAsGuestRequestDto, JoinAsGuestResponseDto>(
        baseUrl + '/auth/sign-up/guest',
        async ({ request }) => {
            const body = await request.json();

            if (body.nickname === 'error') {
                return HttpResponse.json(
                    {
                        accessToken: '',
                    },
                    { status: 500 }
                );
            }

            return HttpResponse.json({
                accessToken: 'mock-access-token',
            });
        }
    ),
];
