import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './OrderManagement.css';

const OrderManagement = () => {
    const [reservations, setReservations] = useState([]);

    useEffect(() => {
        window.scrollTo(0, 0);

        const fetchReservations = async () => {
            try{
                const token = sessionStorage.getItem('token');
                const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/GetReservation`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setReservations(response.data); 
            } catch (error) {
                console.error('Error fetching reservations:', error);
            }
        };

        fetchReservations();
    }, []);

    return (
        <div>
            {reservations.map((reservation, index) => (
                <div key={index} className="res-container">
                    <div className="res-content-background">
                        <div>
                            <h3>{index+1}</h3>
                            <div className='person-info'>
                                <strong>고객 아이디</strong><span>{reservation.user_id}</span>
                                <strong>고객 전화번호</strong><span>{reservation.user_phone}</span>
                            </div>
                            <div className='res-info'>
                                <img src='/images/calendar.png' alt='달력 일러스트' className='icon'></img><strong>날짜 </strong><span>{reservation.reservation_date}</span>
                                <img src='/images/clock.png' alt='시계 일러스트' className='icon'></img><strong>시간 </strong><span>{reservation.reservation_time}</span>
                                <strong>주문목록 </strong><span></span>
                                <strong>요청사항 </strong><span>{reservation.user_comments}</span>

                                <div className='button-groups'>
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
        </div>
    );
};

export default OrderManagement;