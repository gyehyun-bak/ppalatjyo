import { Outlet } from 'react-router';
import { Navbar, NavbarBrand } from '@heroui/react';

export default function HomeLayout() {
    return (
        <div className="flex flex-col size-full">
            <Navbar>
                <NavbarBrand>빨랐죠</NavbarBrand>
            </Navbar>
            <Outlet />
        </div>
    );
}
