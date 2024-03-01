import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './AdminInfo.css';

const AdminInfo = () => {
    const [userData, setUserData] = useState({
        admin_id: '',
        admin_address: '',
        admin_phone: '',
        admin_name: '',
        admin_pw: ''
    });

    const fetchUserData = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get('/admin/profile', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            const { admin_id, admin_pw, admin_address, admin_phone, admin_name } = response.data;
            setUserData({ admin_id, admin_pw, admin_address, admin_phone, admin_name });
        } catch (error) {
            console.error('Error fetching user data:', error);
        }
    };

    useEffect(() => {
        fetchUserData();
    }, []);

    return (
        <div className="admininfo_container">
            <h2>관리자 정보</h2>
            <div>
                <p><strong>ID</strong><br></br><span>{userData.admin_id}</span></p>
                <p><strong>Address</strong><br></br><span>{userData.admin_address}</span></p>
                <p><strong>Phone Number</strong><br></br><span>{userData.admin_phone}</span></p>
                <p><strong>Name</strong><br></br><span>{userData.admin_name}</span></p>
            </div>
        </div>
    );
};

export default AdminInfo;