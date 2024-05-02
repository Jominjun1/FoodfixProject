import React, { useState } from 'react';
import Side from './Side';
import EditUser from './EditUser';
import AdminInfo from './AdminInfo';

import './Mypage.css';

const Mypage = () => {
    const [activeComponent, setActiveComponent] = useState('admin_info');

    const handleEditClick = () => {
        setActiveComponent('edit');
    };

    const handleInfoClick = () => {
        setActiveComponent('admin_info');
    };

    return (
        <div className="mypage-container">
            <Side 
                onEditClick={handleEditClick} 
                onInfoClick={handleInfoClick} 
            /> 
            {activeComponent === 'edit' ? <EditUser /> : null}
            {activeComponent === 'admin_info' ? <AdminInfo /> : null}
        </div>
    );
};

export default Mypage;