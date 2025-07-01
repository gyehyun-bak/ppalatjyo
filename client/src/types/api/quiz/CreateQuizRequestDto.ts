export interface CreateQuizRequestDto {
    title: string;
    description?: string;
    visibility: 'PUBLIC' | 'PRIVATE';
}
