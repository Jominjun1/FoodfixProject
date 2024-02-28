import React from 'react';
import './Contents.css';
import { useNavigate } from 'react-router-dom';

export const MainSection = () => (
    <section id="main" className="fullscreen-section notice-section">
        <div className="notice-content">
            <p className="fade-in-text">맛있는 선택,</p>
            <p className="fade-in-text">당신을 위한 푸드픽스</p>
        </div>
    </section>
);

export const InformationSection = () => {
    const navigate = useNavigate();

    const handleButtonClick = () => {
        navigate('/info');
    };

    return (
        <section id="info" className="fullscreen-section faq-section">
            <p className='text-header'>식당 정보 등록 및 수정</p>
            <p className='text-content'>식당에 대한 정보를 등록하세요!</p>
            <button className='button custom-button' onClick={handleButtonClick}>등록 및 수정</button>
        </section>
    );
};

export const ReservationSection = () => {
    const navigate = useNavigate();

    const handleButtonClick = () => {
        navigate('/res');
    };
    
    return (
        <section id="res" className="fullscreen-section res-section">
            <p className='text-header'>식당 예약 관리</p>
            <p className='text-content'>식당 예약을 편리하게 관리하세요!</p>
            <button className='button custom-button' onClick={handleButtonClick}>예약 관리하기</button>
        </section>
    );
};

export const OrderSection = () => {
    const navigate = useNavigate();

    const handleButtonClick = () => {
        navigate('/order');
    };

    return (
        <section id="order" className="fullscreen-section order-section">
            <p className='text-header'>식당 주문 관리</p>
            <p className='text-content'>식당 주문을 편리하게 관리하세요!</p>
            <button className='button custom-button' onClick={handleButtonClick}>주문 관리하기</button>
        </section>
    );
};