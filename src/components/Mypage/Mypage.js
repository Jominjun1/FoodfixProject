import React, { useState } from 'react';
import Side from './Side';
import EditUser from './EditUser';
import LeaveUser from './LeaveUser'; // LeaveUser 컴포넌트 추가
import './Mypage.css';

const Mypage = () => {
  const [activeComponent, setActiveComponent] = useState('edit'); // 'edit' 또는 'leave'로 초기화

  const handleEditClick = () => {
    setActiveComponent('edit'); // 'edit' 클릭 시 EditUser 컴포넌트를 표시
  };

  const handleLeaveClick = () => {
    setActiveComponent('leave'); // 'leave' 클릭 시 LeaveUser 컴포넌트를 표시
  };

  return (
    <div className="mypage-container">
      <Side onEditClick={handleEditClick} onLeaveClick={handleLeaveClick} />
      {activeComponent === 'edit' ? <EditUser /> : <LeaveUser />}
    </div>
  );
}

export default Mypage;
