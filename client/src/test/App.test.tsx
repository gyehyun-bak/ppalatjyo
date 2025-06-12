import {describe, it} from "vitest";
import {render, screen} from "@testing-library/react";
import '@testing-library/jest-dom'
import App from "../App.tsx";

describe("App", () => {
    it('renders Hello World', async () => {
        render(<App/>)
        const element = screen.getByText(/Hello World/i)
        expect(element).toBeInTheDocument()
    })
})