import React, { useState, useEffect } from 'react';
import './Content.css';
import ResManagement from './ResManagement';
import OrderManagement from './OrderManagement';
import { useNavigate } from 'react-router-dom';

const Content = () => {
    const initialTab = localStorage.getItem('selectedTab') || 'resManagement';
    const [selectedTab, setSelectedTab] = useState(initialTab);
    const navigate = useNavigate();
    
    useEffect(() => {
        let webSocket;
        if (!webSocket || webSocket.readyState !== WebSocket.OPEN) {
            webSocket = new WebSocket('ws://54.180.213.178:8080/wsk');
        
            webSocket.onopen = function(event) {
                console.log('웹 소켓 연결 성공.');
            };
        
            webSocket.onmessage = function(event) {
                console.log('서버 : ', event.data);
            };
        }
    
        return () => {
            if (webSocket && webSocket.readyState === WebSocket.OPEN) {
                console.log('웹 소켓 연결 해제.');
                webSocket.close();
            }
        };
    }, []);
    
    

    const handleTabSelect = (tabName) => {
        setSelectedTab(tabName);
        localStorage.setItem('selectedTab', tabName);
    };

    const handleHomeClick = () => {
        navigate('/');
    };
    
    return (
        <div className="my-body">
    <div className="header-content">
        <img src='/images/logo.png' alt="푸드픽스 로고" className="logo" onClick={handleHomeClick} />
        <div className="tab-buttons">
            <button onClick={() => handleTabSelect('resManagement')} className={selectedTab === 'resManagement' ? 'selected' : ''}>
                예약 관리
            </button>
            <button onClick={() => handleTabSelect('orderManagement')} className={selectedTab === 'orderManagement' ? 'selected' : ''}>
                주문 관리
            </button>
        </div>
    </div>

    <div className="content-area">
        {selectedTab === 'resManagement' && <ResManagement />}
        {selectedTab === 'orderManagement' && <OrderManagement />}
    </div>
</div>

    );
}

export default Content;
