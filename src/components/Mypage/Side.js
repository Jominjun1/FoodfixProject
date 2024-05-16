import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Side.css';

const Side = ({ onEditClick, onInfoClick, onSaleClick }) => {
    const navigate = useNavigate();
    const [showPasswordModal, setShowPasswordModal] = useState(false);
    const [password, setPassword] = useState('');

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

    const handleEditClick = () => {
        setShowPasswordModal(true);
        setPassword('');
    };

    const handleEditUser = () => {
        onEditClick();
        setShowPasswordModal(false);
    };
    
    return (
        <div className='side'>
            <img src='/images/logo2.png' alt='logo' className='profile' width={180} height={35} onClick={handleHomeClick} />
            <div className='user-info' onClick={onInfoClick}>회원 정보 조회</div> 
            <div className='edit-user' onClick={handleEditClick}>회원 정보 수정</div> 
            <div className='restaurant-sales' onClick={onSaleClick}>매장 매출 정보</div> 
            <div className='logout' onClick={handleLogout}>로그아웃</div> 
            <div className='withdrawal' onClick={handleWithdrawal}>회원 탈퇴</div>
            <div onClick={goBack} className="back">뒤로 가기</div>
            {showPasswordModal && (
                <div className='password-modal'>
                    <div className='password-modal-content'>
                        <input
                            type="password"
                            placeholder="비밀번호를 입력하세요"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <button onClick={handleEditUser}>확인</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Side;