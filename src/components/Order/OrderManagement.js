import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './OrderManagement.css';

const OrderManagement = () => {
    const navigate = useNavigate();

    const handleHomeClick = () => {
        navigate('/');
    };

    useEffect(() => {
        window.scrollTo(0, 0);
    }, []); 

    const order = [
        { id: 1, name: "김ㅇㅇ", phoneNumber: "010-1234-5678", time: "nn시 nn분", order: "파스타, 샐러드" },
        { id: 2, name: "박ㅇㅇ", phoneNumber: "010-5678-1234", time: "nn시 nn분", order: "파스타, 샐러드" },
        { id: 3, name: "이ㅇㅇ", phoneNumber: "010-9876-5432", time: "nn시 nn분", order: "파스타, 샐러드" }
    ];

    return (
        <>
            <div className="order">
                <div className="top-logo">
                    <img src='/images/logo.png' alt="푸드픽스 로고" onClick={handleHomeClick} />
                </div>
            </div>

            {order.map(order => (
                <div key={order.id} className="order-container">
                    <div className="order-content-background">
                        <div>
                            <h3>{order.id}</h3>
                            <div className='person-info'>
                                <strong>고객 이름 </strong> <span>{order.name}</span>
                                <strong>고객 전화번호 </strong> <span>{order.phoneNumber}</span>
                            </div>
                            <div className='order-info'>
                                <img src='/images/clock.png' alt='시계 일러스트' className='clock-icon'></img> <strong>시간 </strong> <span>{order.time}</span>
                                <strong>주문 목록 </strong> <span>{order.order}</span>
                                <strong>요청사항 </strong>

                                <div className='order-button-groups'>
                                    <div>
                                        <button>주문 취소하기</button>
                                    </div>
                                    <div>
                                        <button>주문 확정하기</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            ))}
        </>
    );
};

export default OrderManagement;