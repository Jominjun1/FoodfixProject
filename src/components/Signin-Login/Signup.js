import React, { useState } from 'react';
import axios from 'axios';
import './Signup.css';
import { useNavigate } from 'react-router-dom';

const Signup = () => {
    const navigate = useNavigate();

    const handleHomeClick= () => {
        navigate('/');
    }
    
    const [formData, setFormData] = useState({
        admin_id: '',
        admin_pw: '',
        admin_phone: '',
        admin_address: '',
        admin_name: ''
    });

    const [isFilled, setIsFilled] = useState({
        admin_id: false,
        admin_pw: false,
        admin_phone: false,
        admin_address: false,
        admin_name: false
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setIsFilled({ ...isFilled, [name]: value.trim() !== '' });
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const validateForm = () => {
        const idPattern = /^[a-zA-Z0-9]{4,10}$/;
        const pwPattern = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}$/; 

        if (!idPattern.test(formData.admin_id)) {
            alert('아이디는 4자 이상 10자 이하의 대소문자 영문자 또는 숫자여야 합니다.');
            return false;
        }

        if (!pwPattern.test(formData.admin_pw)) {
            alert('비밀번호는 최소 8자에서 최대 16자까지의 대소문자 영문자, 숫자, 특수 문자를 포함해야 합니다.');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!validateForm()) {
            return;
        }

        try {
            const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/admin/signup`, formData);
            console.log(response.data);
            console.log("회원가입에 성공했습니다.");
            navigate('/login');
    
        } catch (error) {
            console.error(error);
        }
    };

    const isFormValid = isFilled.admin_id && isFilled.admin_pw && isFilled.admin_phone && isFilled.admin_address && isFilled.admin_name;
    
    return (
        <div className='full-container'>
            <div className='login-logo'>
                <img src='/images/logo.png' alt="푸드픽스 로고" onClick={handleHomeClick} />
            </div>

            <div className='signup-container'>
                <div className='signup-header'>
                    <div className='header'>
                        Welcome to FoodFix<span className='signup-text'>Sign Up</span>
                    </div>
                    <div className='signup-prompt'>
                        Have an Account?<span onClick={() => { navigate('/login'); }}>Sign In</span>
                    </div>
                </div>

            <form className='signup-inputs' onSubmit={handleSubmit}>
                <input className='signup-custom-input' placeholder='ID' type='text' name='admin_id' onChange={handleChange} />
                <input className='signup-custom-input' placeholder='PASSWORD' type='password' name='admin_pw' onChange={handleChange} />
                <input className='signup-custom-input' placeholder='PHONE' type='phone' name='admin_phone' onChange={handleChange} />
                <input className='signup-custom-input' placeholder='ADDRESS' type='text' name='admin_address' onChange={handleChange} />
                <input className='signup-custom-input' placeholder='NAME' type='text' name='admin_name' onChange={handleChange} />
                <button type="submit" className={`signup-submit ${isFormValid ? 'active' : ''}`}>Sign Up</button>
            </form>
        </div>
    </div>
    );
};

export default Signup;