import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Side.css';

const Side = ({ onEditClick, onLeaveClick }) => {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    }

    const handleLogout = async () => {
        const token = localStorage.getItem('token'); 
        console.log('Token:', token);
    
        try {
            const response = await fetch('/admin/logout', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
            });
    
            if (response.ok) {
                console.log('Logout successful');
                localStorage.removeItem('token');
                
                alert('로그아웃 되었습니다.');
                navigate('/');
            } else {
                console.error('Logout failed');
                alert('로그아웃 실패');
            }
        } catch (error) {
            console.error('Error during logout:', error.message);
        }
    };
    

    return (
        <div className="side">
            <img src='/images/usericon.png' alt='usericon' className="profile" />
            <div className="edit-user" onClick={onEditClick}>회원 정보 수정</div> 
            <div className="leave-user" onClick={onLeaveClick}>회원 탈퇴</div>
            <div className="logout" onClick={handleLogout}>로그아웃</div> 
            <div onClick={goBack} className="back">뒤로 가기</div>
        </div>
    );
};

export default Side;


