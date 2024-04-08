import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Side.css';

const Side = ({ onEditClick, onInfoClick }) => {
    const navigate = useNavigate();

    const handleHomeClick = () => {
        navigate('/');
    };

    const goBack = () => {
        navigate(-1);
    }

    const handleLogout = async () => {
        const token = sessionStorage.getItem('token'); 
        console.log('Token:', token);
    
        try {
            const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/admin/logout`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
            });
    
            if (response.ok) {
                console.log('Logout successful');
                sessionStorage.removeItem('token');
                
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
    

    const handleWithdrawal = async () => {
        const confirmDelete = window.confirm('회원 탈퇴를 하시겠습니까? 이 작업은 되돌릴 수 없습니다.');
        // 회원 탈퇴 요청 보내기

        if (confirmDelete) {
            const token = sessionStorage.getItem('token');

            try {
                const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/admin/delete`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                    credentials: 'include',
                });

                if (response.ok) {
                    console.log('Withdrawal successful');
                    sessionStorage.removeItem('token');
                    alert('회원 탈퇴 되었습니다.');
                    navigate('/');
                } else {
                    console.error('Withdrawal failed');
                    alert('회원 탈퇴 실패');
                }
            } catch (error) {
                console.error('Error during withdrawal:', error.message);
            }
        }
    };
    
    return (
        <div className="side">
            <img src='/images/logo.png' alt='logo' className="profile" onClick={handleHomeClick} />
            <div className="user-info" onClick={onInfoClick}>회원 정보 조회</div> 
            <div className="edit-user" onClick={onEditClick}>회원 정보 수정</div> 
            <div className="logout" onClick={handleLogout}>로그아웃</div> 
            <div className="withdrawal" onClick={handleWithdrawal}>회원 탈퇴</div>
            <div onClick={goBack} className="back">뒤로 가기</div>
        </div>
    );
};

export default Side;