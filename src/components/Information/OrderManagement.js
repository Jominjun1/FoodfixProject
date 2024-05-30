import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './OrderManagement.css';

const OrderManagement = () => {
    const [orders, setOrders] = useState([]);
    const [filteredOrders, setFilteredOrders] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchOrders = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/getPacking`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const allOrders = response.data;
            const filteredOrders = allOrders.filter(order => order.packing_status !== "2" && order.packing_status !== "3")
                .sort((a, b) => {
                    const dateA = new Date(`${a.packing_date} ${a.packing_time}`);
                    const dateB = new Date(`${b.packing_date} ${b.packing_time}`);
                    return dateA - dateB;
                });
            setOrders(allOrders);
            setFilteredOrders(filteredOrders);
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

    const updateOrderStatus = async (packing_id, packing_status) => {
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
    
            const updatedOrders = orders.map(order =>
                order.packing_id === packing_id ? { ...order, packing_status } : order
            );
    
            setOrders(updatedOrders);
            
            fetchOrders();
        } catch (error) {
            console.error('Error updating order status:', error);
            fetchOrders();
        }
    };
    

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    const calculateTotalPrice = (order) => {
        let totalPrice = 0;
        if (order.menuItemDTOList) {
            order.menuItemDTOList.forEach(menuItem => {
                totalPrice += menuItem.quantity * menuItem.menu_price;
            });
        }
        return totalPrice;
    };

    return (
        <div>
            <div className='plus-info-add-button'>
                <button onClick={openModal}>=</button>
            </div>

            <div className='order-info-view'>
                <div className='order-info-container'>
                    {filteredOrders.map(order => (
                        <div className='order-items-container' key={order.packing_id}>
                            <div className='order-items'>
                                <p className='order-name'>주문 번호 : {order.packing_id}</p>
                                <p className='order-price'>고객 정보 : {order.user_id} ({order.user_phone})</p>
                                <p>상태 : {order.packing_status === "0" ? "대기" : "확정"}</p>
                                <p>날짜 : {order.packing_date}</p>
                                <p>시간 : {order.packing_time}</p>
                                <p className='order-list'>주문 목록 : {order.menuItemDTOList && order.menuItemDTOList.map((menuItem, index) => (
                                    `${menuItem.menu_name} x ${menuItem.quantity}${index !== order.menuItemDTOList.length - 1 ? ' / ' : ''}`
                                ))}
                                </p>
                                <p>총 가격: {calculateTotalPrice(order)}</p>

                                <div className='order-buttons'>
                                    <button onClick={() => updateOrderStatus(order.packing_id, 1)}>확정</button>
                                    <button onClick={() => updateOrderStatus(order.packing_id, 2)}>취소</button>
                                    <button onClick={() => updateOrderStatus(order.packing_id, 3)}>완료</button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {isModalOpen && (
                <div className='order-plus-info-modal'>
                    <div className='order-plus-info-modal-content'>
                        <span className='order-plus-info-modal-close' onClick={closeModal}>&times;</span>
                        {orders.map(order => (
                            (order.packing_status === "2" || order.packing_status === "3") && 
                            <div className='order-items-container-modal' key={order.packing_id}>
                                <p>주문 번호: {order.packing_id}</p>
                                <p>고객 정보: {order.user_id} ({order.user_phone})</p>
                                <p>상태: {order.packing_status === "2" ? "취소" : "완료"}</p>
                                <p>날짜: {order.packing_date}</p>
                                <p>시간: {order.packing_time}</p>
                                <p>총 가격: {calculateTotalPrice(order)}</p>
                                <button className='order-modal-button' onClick={() => updateOrderStatus(order.packing_id, 1)}>확정</button>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default OrderManagement;