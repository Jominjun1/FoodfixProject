import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './OrderManagement.css';

const OrderManagement = () => {
    const [orders, setOrders] = useState([]);

    const fetchOrders = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/getPacking`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const filteredOrders = response.data.filter(order => order.packing_status !== "2" && order.packing_status !== "3");
            setOrders(filteredOrders);
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

    const updateOrderStatus = async (packing_id, packing_status) => {
        const updatedOrders = orders.map(order =>
            order.packing_id === packing_id ? { ...order, packing_status } : order
        );

        setOrders(updatedOrders); 

        try {
            const token = sessionStorage.getItem('token');
            await axios.put(`${process.env.REACT_APP_SERVER_URL}/admin/updatePacking`, {
                packing_id,
                packing_status
            }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
        } catch (error) {
            console.error('Error updating order status:', error);
            fetchOrders();
        }
    };

    return (
        <div>
            {orders.map((order, index) => (
                <div key={index} className="order-container">
                    <div className="order-content-background">
                        <div>
                            <h3>{index + 1}</h3>
                            <div className='person-info'>
                                <strong>주문 번호</strong><span>{order.packing_id}</span>
                                <strong>고객 아이디</strong><span>{order.user_id}</span>
                                <strong>고객 전화번호</strong><span>{order.user_phone}</span>
                                <strong>포장 주문 상태</strong><span>{order.packing_status}</span>
                            </div>
                            <div className='order-info'>
                                <strong>날짜 </strong><span>{order.packing_date}</span>
                                <strong>시간 </strong><span>{order.packing_time}</span>
                                <strong>주문목록 </strong><span>{order.menuItemDTOList ? order.menuItemDTOList.map(item => `${item.menu_name} x ${item.quantity}`).join(", ") : '정보 없음'}</span> 
                                <strong>요청사항 </strong><span>{order.user_comments}</span>
                            </div>
                            <div className='order-button-groups'>
                                <div>
                                    <button onClick={() => updateOrderStatus(order.packing_id, 2)}>주문 취소하기</button>
                                </div>
                                <div>
                                    <button onClick={() => updateOrderStatus(order.packing_id, 1)}>주문 확정하기</button>
                                </div>
                                <div>
                                    <button onClick={() => updateOrderStatus(order.packing_id, 3)}>주문 완료하기</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
    
};

export default OrderManagement;