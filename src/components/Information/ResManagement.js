import React, { useState, useEffect } from 'react';
import './ResManagement.css';
import axios from 'axios';

const ResManagement = () => {
    const [reservations, setReservations] = useState([]);

    useEffect(() => {
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
                                <strong>예약 번호</strong><span>{reservation.reservation_id}</span>
                                <strong>예약 상태</strong><span>{reservation.reservation_status}</span>
                                <strong>예약자 아이디</strong><span>{reservation.user_id}</span>
                                <strong>예약자 전화번호</strong><span>{reservation.user_phone}</span>
                            </div>
                            <div className='res-info'>
                                <img src='/images/calendar.png' alt='달력 일러스트' className='icon'></img><strong>날짜 </strong><span>{reservation.reservation_date}</span>
                                <img src='/images/clock.png' alt='시계 일러스트' className='icon'></img><strong>시간 </strong><span>{reservation.reservation_time}</span>
                                <img src='/images/person.png' alt='사람 일러스트' className='icon'></img><strong>인원수 </strong><span>{reservation.people_cnt}</span>
                                <strong>요청사항 </strong><span>{reservation.user_comments}</span>

                                <div className='button-groups'>
                                    <div>
                                        <button>예약 취소하기</button>
                                    </div>
                                    <div>
                                        <button>예약 확정하기</button>
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

export default ResManagement;