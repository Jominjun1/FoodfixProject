import React from 'react';
import './Header.css';
import { useNavigate } from 'react-router-dom';
import { BiUserCircle} from 'react-icons/bi';

const Header = () => {
    const navigate = useNavigate();

    const handleLoginButtonClick = () => {
        navigate('login');
    }

    const handleUserIconClick = () => {
        navigate('mypage');
    }

    const handleScrollToSection = (id) => {
        const section = document.getElementById(id);
        if (section) {
            section.scrollIntoView({ behavior: 'smooth' });
        }
    };

    return (
        <div className="header-container">
            <div className="section">
                <div className="logo-container">
                    <img src='/images/logo.png' alt="푸드픽스 로고" />
                </div>

                <button className="nav-button" onClick={() => handleScrollToSection('main')}>푸드픽스 소개</button>
                <button className="nav-button" onClick={() => handleScrollToSection('info')}>정보 등록 및 수정</button>
                <button className="nav-button" onClick={() => handleScrollToSection('res')}>예약 관리</button>
                <button className="nav-button" onClick={() => handleScrollToSection('order')}>주문 관리</button>

                <div className="user">
                    <button className="button" onClick={handleLoginButtonClick}>로그인</button>
                    <BiUserCircle className="user-icon" onClick={handleUserIconClick} size={40} />
                </div>
            </div>
        </div>
    );
};

export default Header;





