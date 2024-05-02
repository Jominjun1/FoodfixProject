// Login.js
import React, { useState } from 'react';
import axios from 'axios';
import './Login.css';
import { useNavigate } from 'react-router-dom';
import { BrowserView, MobileView } from 'react-device-detect';

const Login = () => {
    const navigate = useNavigate();

    const handleHomeClick = () => {
        navigate('/');
    };
    
    const [formData, setFormData] = useState({
        admin_id: '',
        admin_pw: ''
    });

    const [isFilled, setIsFilled] = useState({
        admin_id: false,
        admin_pw: false
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setIsFilled({ ...isFilled, [name]: value.trim() !== '' });
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/admin/login`, formData);
            const token = response.data.token; 
            console.log("로그인에 성공했습니다.");
    
            sessionStorage.setItem('token', token);

            navigate('/');
        } catch (error) {
            console.error(error);
            alert("아이디 또는 비밀번호가 틀렸습니다");
        }
    };

    const isFormValid = isFilled.admin_id && isFilled.admin_pw;

    return (
        <div>
            <BrowserView>
            <div className="full-container">
            <div className='login-logo'>
                <img src='/images/logo.png' alt="푸드픽스 로고" onClick={handleHomeClick} />
            </div>

            <div className="login-container">
                <div className="login-header">
                    <div className="signin-header">
                        Welcome to FoodFix<span className="signin-text">Sign In</span>
                    </div>
                    <div className="signup-prompt">
                        No account?<span onClick={() => { navigate('/signup'); }}>Sign up</span>
                    </div>
                </div>
    
                <form className='signin-inputs' onSubmit={handleSubmit}>
                    <input className='signin-custom-input' placeholder='ID' type='text' name='admin_id' onChange={handleChange} />
                    <input className='signin-custom-input' placeholder='PASSWORD' type='password' name='admin_pw' onChange={handleChange} />
                    <button type="submit" className={`login-submit ${isFormValid ? 'active' : ''}`}>Sign In</button>
                </form>
    
                <div className='login-separator-line'></div>
    
                <div className="login-image">
                    <img src='/images/kakao.jpg' className='kakao' alt='카카오톡 로고'></img>
                    <img src='images/naver.jpg' className='naver' alt='네이버 로고'></img>
                    <img src='images/google.jpg' className='google' alt='구글 로고'></img>
                    <img src='images/apple.jpg' className='apple' alt='애플 로고'></img>
                </div>
            </div>
            </div>
            </BrowserView>
    
            <MobileView>
            <div className="full-container-mobile">
            <div className='login-logo-mobile'>
                <img src='/images/logo.png' alt="푸드픽스 로고" onClick={handleHomeClick} width={120} height={40}/>
            </div>

                <div className="login-container-mobile">
                <div className="login-header-mobile">
                    <div className="signin-header-mobile">
                        Welcome to FoodFix<span className="signin-text-mobile">Sign In</span>
                    </div>
                    <div className="signup-prompt-mobile">
                        No account?<span onClick={() => { navigate('/signup'); }}>Sign up</span>
                    </div>
                </div>
    
                <form className='signin-inputs-mobile' onSubmit={handleSubmit}>
                    <input className='signin-custom-input-mobile' placeholder='ID' type='text' name='admin_id' onChange={handleChange} />
                    <input className='signin-custom-input-mobile' placeholder='PASSWORD' type='password' name='admin_pw' onChange={handleChange} />
                    <button type="submit" className={`login-submit-mobile ${isFormValid ? 'active' : ''}`}>Sign In</button>
                </form>
    
                <div className='login-separator-line-mobile'></div>
    
                <div className="login-image-mobile">
                    <img src='/images/kakao.jpg' className='kakao-mobile' alt='카카오톡 로고'></img>
                    <img src='images/naver.jpg' className='naver-mobile' alt='네이버 로고'></img>
                    <img src='images/google.jpg' className='google-mobile' alt='구글 로고'></img>
                    <img src='images/apple.jpg' className='apple-mobile' alt='애플 로고'></img>
                </div>
                </div>
                </div>
            </MobileView>
        </div>
    );
    
};

export default Login;



