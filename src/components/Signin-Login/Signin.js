import React, { useState } from 'react';
import axios from 'axios';
import './Signin-Login.css';
import { useNavigate } from 'react-router-dom';

const Signin = () => {
    const navigate = useNavigate();
    
    const [formData, setFormData] = useState({
        admin_id: '',
        admin_pw: '',
        admin_phone: '',
        admin_address: '',
        admin_name: ''
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/admin/signup', formData);
            console.log(response.data);
            console.log("회원가입에 성공했습니다.");
            navigate('/login');
    
        } catch (error) {
            console.error(error);
        }
    };
    
    return (
        <div className="signin-container">
            <div className="header">
                <div className="text">SIGN UP</div>
            </div>

            <div className='separator-line'></div>

            <form className='inputs' onSubmit={handleSubmit}>
                <div className='input'>
                    <input className='custom-input' placeholder='ID' type='text' name='admin_id' onChange={handleChange} />
                </div>
                <div className='input'>
                    <input className='custom-input' placeholder='PASSWORD' type='password' name='admin_pw' onChange={handleChange} />
                </div>
                <div className='input'>
                    <input className='custom-input' placeholder='PHONE' type='phone' name='admin_phone' onChange={handleChange} />
                </div>
                <div className='input'>
                    <input className='custom-input' placeholder='ADDRESS' type='text' name='admin_address' onChange={handleChange} />
                </div>
                <div className='input'>
                    <input className='custom-input' placeholder='NAME' type='text' name='admin_name' onChange={handleChange} />
                </div>
                <button type="submit" className='login-submit' onClick={handleSubmit}>회원가입</button>
            </form>

            <div className='login-separator-line'></div>

            <div className="login-image">
                <img src='/images/kakao.jpg' className='kakao' alt='카카오톡 로고' />
                <img src='images/naver.jpg' className='naver' alt='네이버 로고' />
                <img src='images/google.jpg' className='google' alt='구글 로고' />
                <img src='images/apple.jpg' className='apple' alt='애플 로고' />
            </div>
        </div>
    );
};

export default Signin;