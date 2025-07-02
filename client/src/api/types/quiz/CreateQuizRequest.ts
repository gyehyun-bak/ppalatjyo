export interface CreateQuizRequest {
    title: string;
    description?: string;
    visibility: 'PUBLIC' | 'PRIVATE';
}
