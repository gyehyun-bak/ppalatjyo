import { Outlet } from 'react-router';
import HomeHeader from '../components/HomeHeader';

export default function HomeLayout() {
    return (
        <div className="flex flex-col size-full">
            <HomeHeader />
            <Outlet />
        </div>
    );
}
