import React, { useState, useEffect } from 'react';
import './ResManagement.css';
import axios from 'axios';

const ResManagement = () => {
    const [reservations, setReservations] = useState([]);

    const fetchReservations = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/getReservation`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const filteredReservations = response.data.filter(reservation => reservation.reservation_status !== "2" && reservation.reservation_status !== "3");
            setReservations(filteredReservations);
        } catch (error) {
            console.error('Error fetching reservations:', error);
        }
    };

    useEffect(() => {
        fetchReservations();
    }, []);

    const updateReservationStatus = async (reservation_id, reservation_status) => {
        const updatedReservations = reservations.map(reservation =>
            reservation.reservation_id === reservation_id ? { ...reservation, reservation_status } : reservation
        );
        setReservations(updatedReservations);

        try {
            const token = sessionStorage.getItem('token');
            await axios.put(`${process.env.REACT_APP_SERVER_URL}/admin/updateReservation`, {
                reservation_id,
                reservation_status
            }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
        } catch (error) {
            console.error('Error updating reservation status:', error);
            fetchReservations();
        }
    };

    return (
        <div>
            {reservations.map((reservation, index) => (
                <div key={index} className="res-container">
                    <div className="res-content-background">
                        <div>
                            <h3>{index + 1}</h3>
                            <div className='person-info'>
                                <strong>예약 번호</strong><span>{reservation.reservation_id}</span>
                                <strong>예약 상태</strong><span>{reservation.reservation_status}</span>
                                <strong>예약자 아이디</strong><span>{reservation.user_id}</span>
                                <strong>예약자 전화번호</strong><span>{reservation.user_phone}</span>
                            </div>
                            <div className='res-info'>
                                <strong>날짜 </strong><span>{reservation.reservation_date}</span>
                                <strong>시간 </strong><span>{reservation.reservation_time}</span>
                                <strong>인원수 </strong><span>{reservation.people_cnt}</span>
                                <strong>요청사항 </strong><span>{reservation.user_comments}</span>
                            </div>
                            <div className='res-button-groups'>
                                <div>
                                    <button onClick={() => updateReservationStatus(reservation.reservation_id, 2)}>예약 취소하기</button>
                                </div>
                                <div>
                                    <button onClick={() => updateReservationStatus(reservation.reservation_id, 1)}>예약 확정하기</button>
                                </div>
                                <div>
                                    <button onClick={() => updateReservationStatus(reservation.reservation_id, 3)}>예약 완료하기</button>
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