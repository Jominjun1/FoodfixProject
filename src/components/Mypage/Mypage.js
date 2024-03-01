import React, { useState } from 'react';
import Side from './Side';
import EditUser from './EditUser';
import AdminInfo from './AdminInfo';
import './Mypage.css';

const Mypage = () => {
  const [activeComponent, setActiveComponent] = useState('edit');

  const handleEditClick = () => {
    setActiveComponent('edit');
  };

  const handleInfoClick = () => {
    setActiveComponent('admin_info'); 
  };

  return (
    <div className="mypage-container">
      <Side onEditClick={handleEditClick} onInfoClick={handleInfoClick} />
      {activeComponent === 'edit' ? <EditUser /> : <AdminInfo />} 
    </div>
  );
}

export default Mypage;
