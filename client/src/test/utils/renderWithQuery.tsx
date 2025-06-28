import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import React from "react";
import { render as rtlRender } from "@testing-library/react";

export function renderWithQuery(ui: React.ReactElement) {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false } },
    });
    return {
        ...rtlRender(
            <QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>
        ),
        queryClient,
    };
}
