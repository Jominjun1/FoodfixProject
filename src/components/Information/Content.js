import React, { useState, useEffect } from 'react';
import './Content.css';
import ResManagement from './ResManagement';
import OrderManagement from './OrderManagement';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Content = () => {
    const initialTab = localStorage.getItem('selectedTab') || 'resManagement';
    const [selectedTab, setSelectedTab] = useState(initialTab);
    const navigate = useNavigate();

    useEffect(() => {
        let webSocket;
        const fetchStoreIdAndConnectWebSocket = async () => {
            try {
                const token = sessionStorage.getItem('token');
                const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/store`, {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`
                    }
                });

                const storeId = response.data.store_id;
                webSocket = new WebSocket(`ws://54.180.213.178:8080/wsk?store_id=${storeId}`);
        
                webSocket.onopen = () => {
                    console.log('웹 소켓 연결 성공.');
                    webSocket.send(JSON.stringify({ type: 'store_id', store_id: storeId }));
                    console.log('store_id를 서버로 전송했습니다:', storeId);
                };
        
                webSocket.onmessage = (event) => {
                    console.log('서버 : ', event.data);
                    const sessionId = event.data;
                    console.log('서버에서 세션 ID 수신:', sessionId);
                    localStorage.setItem('sessionId', sessionId); 
                };
            } catch (error) {
                console.error('store_id를 가져오거나 웹소켓 연결하는데 실패했습니다:', error);
            }
        };

        fetchStoreIdAndConnectWebSocket();

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
        <div>
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

