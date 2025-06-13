import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import '@testing-library/jest-dom'
import App from "../App.tsx";

describe("App", () => {
    it('renders Hello World', async () => {
        // given
        render(<App/>)

        // when
        const text = screen.getByText('Hello World');

        // then
        expect(text).toBeInTheDocument()
    })
})