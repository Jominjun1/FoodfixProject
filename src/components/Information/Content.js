import React, { useState } from 'react';
import './Content.css';
import RestaurantInfo from './RestaurantInfo';
import MenuManagement from './MenuManagement';
import { useNavigate } from 'react-router-dom';

const Content = () => {
    const initialTab = localStorage.getItem('selectedTab') || 'restaurantInfo';
    const [selectedTab, setSelectedTab] = useState(initialTab);

    const handleTabSelect = (tabName) => {
        setSelectedTab(tabName);
        localStorage.setItem('selectedTab', tabName);
    };

    const navigate = useNavigate();

    const handleHomeClick = () => {
        navigate('/');
    };
    
    return (
        <div className="my-body">
            <div className="header-content">
                <img src='/images/logo.png' alt="푸드픽스 로고" className="logo" onClick={handleHomeClick} />
            </div>

            <div>
                <div className="tab-buttons">
                    <button onClick={() => handleTabSelect('restaurantInfo')} className={selectedTab === 'restaurantInfo' ? 'selected' : ''}>
                        정보 관리
                    </button>
                    <button onClick={() => handleTabSelect('menuManagement')} className={selectedTab === 'menuManagement' ? 'selected' : ''}>
                        메뉴 관리
                    </button>
                </div>

                <div className="content-area">
                    {selectedTab === 'restaurantInfo' && <RestaurantInfo />}
                    {selectedTab === 'menuManagement' && <MenuManagement />}
                </div>
            </div>
        </div>
    );
}

export default Content;