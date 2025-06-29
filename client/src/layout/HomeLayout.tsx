import { Outlet } from 'react-router';
import { Navbar, NavbarBrand } from '@heroui/react';
import Logo from '../components/Logo';

export default function HomeLayout() {
    return (
        <div className="flex flex-col size-full">
            <Navbar>
                <NavbarBrand>
                    <span>빨랐죠</span>
                    <Logo size={22} />
                </NavbarBrand>
            </Navbar>
            <Outlet />
        </div>
    );
}
