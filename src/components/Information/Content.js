import React, { useState } from 'react';
import './Content.css';
import ResManagement from './ResManagement';
import OrderManagement from './OrderManagement';
import { useNavigate } from 'react-router-dom';

const Content = () => {
    const initialTab = localStorage.getItem('selectedTab') || 'resManagement';
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
                    <button onClick={() => handleTabSelect('resManagement')} className={selectedTab === 'resManagement' ? 'selected' : ''}>
                        예약 관리
                    </button>
                    <button onClick={() => handleTabSelect('orderManagement')} className={selectedTab === 'orderManagement' ? 'selected' : ''}>
                        주문 관리
                    </button>
                </div>

                <div className="content-area">
                    {selectedTab === 'resManagement' && <ResManagement />}
                    {selectedTab === 'orderManagement' && <OrderManagement />}
                </div>
            </div>
        </div>
    );
}

export default Content;