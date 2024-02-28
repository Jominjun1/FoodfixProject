import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Side.css';

const Side = ({ onEditClick, onLeaveClick }) => {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    }

    return (
        <div className="side">
            <img src='/images/usericon.png' alt='usericon' className="profile" />
            <div className="edit-user" onClick={onEditClick}>회원 정보 수정</div> 
            <div className="leave-user" onClick={onLeaveClick}>회원 탈퇴</div> 
            <div onClick={goBack} className="back">뒤로 가기</div>
        </div>
    );
};

export default Side;
