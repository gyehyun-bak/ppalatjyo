import { Outlet } from 'react-router';
import HomeHeader from '../components/HomeHeader';

export default function HomeLayout() {
    return (
        <section className="flex flex-col size-full">
            <HomeHeader />
            <Outlet />
        </section>
    );
}
