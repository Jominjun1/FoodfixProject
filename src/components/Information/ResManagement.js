import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './ResManagement.css';

const ResManagement = () => {
    const [reservations, setReservations] = useState([]);
    const [filteredReservations, setFilteredReservations] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchReservations = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/getReservation`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const allReservations = response.data;
            const filteredReservations = allReservations.filter(reservation => reservation.reservation_status !== "2" && reservation.reservation_status !== "3")
                .sort((a, b) => {
                    const dateA = new Date(`${a.reservation_date} ${a.reservation_time}`);
                    const dateB = new Date(`${b.reservation_date} ${b.reservation_time}`);
                    return dateA - dateB;
                });
            setReservations(allReservations);
            setFilteredReservations(filteredReservations);
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    };

    useEffect(() => {
        fetchReservations();
    }, []);

    const updateReservationStatus = async (reservation_id, reservation_status) => {
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

            const updatedReservations = reservations.map(reservation =>
                reservation.reservation_id === reservation_id ? { ...reservation, reservation_status } : reservation
            );
            
            setReservations(updatedReservations);
            fetchReservations();

        } catch (error) {
            console.error('Error updating order status:', error);
            fetchReservations();
        }
    };

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div>
            <div className='plus-info-add-button'>
                <button onClick={openModal}>=</button>
            </div>

            <div className='res-info-view'>
                <div className='res-info-container'>
                    {filteredReservations.map(reservation => (
                        <div className='res-items-container' key={reservation.reservation_id}>
                            <div className='res-items'>
                                <p className='res-name'>주문 번호 : {reservation.reservation_id}</p>
                                <p className='res-person'>고객 정보 : {reservation.user_id} ({reservation.user_phone})</p>
                                <p className='res-description'>상태 : {reservation.reservation_status === "0" ? "대기" : "확정"}</p>
                                <p className='res-description'>날짜 : {reservation.reservation_date}</p>
                                <p className='res-description'>시간 : {reservation.reservation_time}</p>
				                <p className='res-description'>인원 : {reservation.people_cnt}</p>
                                <p className='res-description'>요청사항 : {reservation.user_comments}</p>

                                <div className='res-buttons'>
                                    <button onClick={() => updateReservationStatus(reservation.reservation_id, 1)}>확정</button>
                                    <button onClick={() => updateReservationStatus(reservation.reservation_id, 2)}>취소</button>
                                    <button onClick={() => updateReservationStatus(reservation.reservation_id, 3)}>완료</button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {isModalOpen && (
                <div className='res-plus-info-modal'>
                    <div className='res-plus-info-modal-content'>
                        <span className='res-plus-info-modal-close' onClick={closeModal}>&times;</span>
                        {reservations.map(reservation => (
                            (reservation.reservation_status === "2" || reservation.reservation_status === "3") && 
                            <div className='res-items-container' key={reservation.reservation_id}>
                                <p>주문 번호: {reservation.reservation_id}</p>
                                <p>고객 정보: {reservation.user_id} ({reservation.user_phone})</p>
                                <p>상태: {reservation.reservation_status === "2" ? "취소" : "완료"}</p>
                                <p>날짜: {reservation.reservation_date}</p>
                                <p>시간: {reservation.reservation_time}</p>
				                <p className='res-description'>인원 : {reservation.people_cnt}</p>
                                <p className='res-description'>요청사항 : {reservation.user_comments}</p>

                                <button className='res-modal-button' onClick={() => updateReservationStatus(reservation.reservation_id, 1)}>확정</button>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default ResManagement;