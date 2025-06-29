import { Outlet } from 'react-router';
import HomeHeader from '../components/HomeHeader';
import BottomNavigation from '../components/BottomNavigation';

export default function HomeLayout() {
    return (
        <section className="flex flex-col size-full">
            <HomeHeader />
            <Outlet />
            <BottomNavigation />
        </section>
    );
}
