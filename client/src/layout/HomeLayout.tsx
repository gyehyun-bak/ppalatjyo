import { Outlet } from "react-router";
import { Navbar, NavbarBrand } from "@heroui/react";
import Logo from "../components/Logo";

export default function HomeLayout() {
    return (
        <div className="flex flex-col size-full">
            <Navbar isBordered>
                <NavbarBrand>
                    <span className="text-primary font-bold">빨랐죠</span>
                    <Logo size={22} />
                </NavbarBrand>
            </Navbar>
            <Outlet />
        </div>
    );
}
