import React, { useState } from 'react';
import axios from 'axios';
import './Signin-Login.css';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        admin_id: '',
        admin_pw: ''
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/admin/login', formData); 
            const token = response.data.token; 
            console.log("로그인에 성공했습니다.");

            sessionStorage.setItem('token', token);
            
            navigate('/');
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className="login-container">
            <div className="header">
                <div className="text">LOGIN</div>
            </div>

            <div className='separator-line'></div>

            <form className='inputs' onSubmit={handleSubmit}>
                <div className='input'>
                    <input className='custom-input' placeholder='ID' type='text' name='admin_id' onChange={handleChange} />
                </div>
                <div className='input'>
                    <input className='custom-input' placeholder='PASSWORD' type='password' name='admin_pw' onChange={handleChange} />
                </div>
                <button type="submit" className='login-submit'>로그인</button>
            </form>

            <div className='login-separator-line'></div>

            <div className="login-image">
                <img src='/images/kakao.jpg' className='kakao' alt='카카오톡 로고'></img>
                <img src='images/naver.jpg' className='naver' alt='네이버 로고'></img>
                <img src='images/google.jpg' className='google' alt='구글 로고'></img>
                <img src='images/apple.jpg' className='apple' alt='애플 로고'></img>
            </div>

            <div className='signin-box'>
                <div>아직 푸드픽스의 회원이 아니신가요?</div>
                <div>회원가입을 하시면 더 많은 정보와 혜택을 받으실 수 있습니다.</div>
                <div className='signin-submit' onClick={() => { window.location.href = '/signin'; }}>
                    회원가입
                </div>
            </div>
        </div>
    );
};

export default Login;

