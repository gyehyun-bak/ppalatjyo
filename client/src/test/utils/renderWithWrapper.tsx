import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import React from "react";
import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";

export function renderWithWrapper(ui: React.ReactElement) {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false } },
    });

    render(
        <QueryClientProvider client={queryClient}>
            <MemoryRouter>{ui}</MemoryRouter>
        </QueryClientProvider>
    );
}
