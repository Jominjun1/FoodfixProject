import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './EditUser.css';

const EditUser = () => {
    const [adminInfo, setAdminInfo] = useState(null);
    const [formData, setFormData] = useState({
        admin_id: '',
        admin_pw: '',
        admin_address: '',
        admin_name: '',
        admin_phone: ''
    });
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        const fetchAdminInfo = async () => {
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    return;
                }

                const response = await axios.get('/admin/profile', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                setAdminInfo(response.data);
                setFormData(response.data);
            } catch (error) {
                console.error('사용자 정보를 가져오는 데 실패했습니다:', error);
            }
        };

        fetchAdminInfo();
    }, []); 

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                return;
            }
    
            const response = await axios.put('/admin/update', formData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
    
            console.log('정보 수정 완료:', response.data);

            localStorage.setItem('adminInfo', JSON.stringify(formData));
        } catch (error) {
            console.error('정보 수정에 실패했습니다:', error);
        }
    };
    

    const toggleEditing = () => {
        setIsEditing(!isEditing);
    };

    if (!adminInfo) {
        return <div>Loading...</div>;
    }

    return (
        <div className="edit-user-container">
            <div className="editpage-title"><h3>회원 정보 {isEditing ? '수정' : '보기'}</h3></div>
            
            {isEditing ? (
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Password</label>
                        <input type="password" name="admin_pw" value={formData.admin_pw} onChange={handleChange} />
                    </div>
                    <div>
                        <label>Address</label>
                        <input type="text" name="admin_address" value={formData.admin_address} onChange={handleChange} />
                    </div>
                    <div>
                        <label>Name</label>
                        <input type="text" name="admin_name" value={formData.admin_name} onChange={handleChange} />
                    </div>
                    <div>
                        <label>Phone</label>
                        <input type="text" name="admin_phone" value={formData.admin_phone} onChange={handleChange} />
                    </div>
                    <button type="submit">등록하기</button>
                    <button type="button" onClick={toggleEditing}>취소하기</button>
                </form>
            ) : (
                <div>
                    <div>ID {formData.admin_id}</div>
                    <div>Address {formData.admin_address}</div>
                    <div>Name {formData.admin_name}</div>
                    <div>Phone {formData.admin_phone}</div>
                    <button onClick={toggleEditing}>수정하기</button>
                </div>
            )}
        </div>
    );
};

export default EditUser;