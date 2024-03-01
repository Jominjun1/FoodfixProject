import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './EditUser.css';

const EditUser = () => {
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

    const handleChange = (e) => {
        setUserData({ ...userData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = sessionStorage.getItem('token');
            await axios.put('/admin/update', userData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            console.log('User data updated successfully');
            alert('정보가 수정되었습니다.');
        } catch (error) {
            console.error('Error updating user data:', error);
            alert('오류입니다.');
        }
    };

    return (
        <div className="editpage-container">
            <h2>관리자 정보 수정</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Password</label>
                    <input type="text" name="admin_pw" value={userData.admin_pw} onChange={handleChange} />
                </div>
                <div>
                    <label>Address</label>
                    <input type="text" name="admin_address" value={userData.admin_address} onChange={handleChange} />
                </div>
                <div>
                    <label>Phone Number</label>
                    <input type="text" name="admin_phone" value={userData.admin_phone} onChange={handleChange} />
                </div>
                <div>
                    <label>Name</label>
                    <input type="text" name="admin_name" value={userData.admin_name} onChange={handleChange} />
                </div>
                <button type="submit">Update</button>
            </form>
        </div>
    );
};

export default EditUser;