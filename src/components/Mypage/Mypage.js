import React, { useState } from 'react';
import Side from './Side';
import EditUser from './EditUser';
import AdminInfo from './AdminInfo';
import SalePage from './SalePage';
import './Mypage.css';

const Mypage = () => {
    const [activeComponent, setActiveComponent] = useState('admin_info');

    const handleEditClick = () => {
        setActiveComponent('edit');
    };

    const handleInfoClick = () => {
        setActiveComponent('admin_info');
    };

    const handleSaleClick = () => {
        setActiveComponent('sale');
    }

    return (
        <div className='mypage-container'>
            <Side 
                onEditClick={handleEditClick} 
                onInfoClick={handleInfoClick} 
                onSaleClick={handleSaleClick}
            /> 
            {activeComponent === 'edit' ? <EditUser /> : null}
            {activeComponent === 'admin_info' ? <AdminInfo /> : null}
            {activeComponent === 'sale' ? <SalePage /> : null}
        </div>
    );
};

export default Mypage;