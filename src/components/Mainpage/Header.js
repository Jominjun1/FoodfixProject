import React, { useState, useEffect } from 'react';
import './Header.css';
import { useNavigate } from 'react-router-dom';
import { BiUserCircle } from 'react-icons/bi';

const Header = () => {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = sessionStorage.getItem('token');
        setIsLoggedIn(!!token);
    }, []);

    const handleLoginButtonClick = () => {
        navigate('login');
    };

    const handleUserIconClick = () => {
        navigate('mypage');
    };

    const handleScrollToSection = (id) => {
        const section = document.getElementById(id);
        if (section) {
            section.scrollIntoView({ behavior: 'smooth' });
        }
    };

    return (
        <div className='header-container'>
            <div className='section'>
                <div className='logo-container'>
                    <img src='/images/logo.png' alt="푸드픽스 로고" />
                </div>

                <div className='nav-buttons'>
                    <button className='nav-button' onClick={() => handleScrollToSection('main')}>푸드픽스 소개</button>
                    <button className='nav-button' onClick={() => handleScrollToSection('restaurant')}>식당 등록 및 관리</button>
                    <button className='nav-button' onClick={() => handleScrollToSection('menu')}>메뉴 등록 및 관리</button>
                    <button className='nav-button' onClick={() => handleScrollToSection('info')}>예약&주문 관리</button>
                </div>

                <div className="user">
                    {!isLoggedIn && (
                    <button className='mainpage-header-button' onClick={handleLoginButtonClick}>로그인</button>
                )}
                
                {isLoggedIn && (
                    <BiUserCircle className='user-icon' onClick={handleUserIconClick} size={40} />
                )}
                </div>
                
            </div>
        </div>
    );
};

export default Header;