import React, { useState, useEffect } from 'react';
import './RestaurantInfo.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const RestaurantInfo = () => {
    const navigate = useNavigate();

    useEffect(() => {
        window.scrollTo(0, 0);
    }, []);

    const [restaurantName, setRestaurantName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [address, setAddress] = useState('');
    const [foodCategory, setFoodCategory] = useState('');
    const [timeCategory, setTimeCategory] = useState('');
    const [openTime, setOpenTime] = useState('');
    const [closeTime, setCloseTime] = useState('');
    const [reservationsAllowed, setReservationsAllowed] = useState(false); 
    const [maxReservations, setMaxReservations] = useState(0);
    const [reservationCancelTime, setReservationCancelTime] = useState('');
    const [storeImage, setStoreImage] = useState(null);
    const [storeDescription, setStoreDescription] = useState('');
    const [previewImage, setPreviewImage] = useState(null);

    const handleImageChange = (e) => {
        const selectedImage = e.target.files[0];
        setStoreImage(selectedImage);

        const imageUrl = URL.createObjectURL(selectedImage);
        setPreviewImage(imageUrl);
    };

    const handleSubmit = async () => {
        const formData = new FormData();
        formData.append('imageFile', storeImage);
        formData.append('store_name', restaurantName);
        formData.append('store_phone', phoneNumber);
        formData.append('store_address', address);
        formData.append('store_category', foodCategory);
        formData.append('openTime', openTime);
        formData.append('closeTime', closeTime);
        formData.append('minimumTime', timeCategory);
        formData.append('res_status', reservationsAllowed ? "1" : "0");
        formData.append('res_max', maxReservations);
        formData.append('reservationCancel', reservationCancelTime);
        formData.append('store_intro', storeDescription);

        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/admin/newstore`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${token}`
                }
            });
            console.log('Success:', response.data);
            alert('식당이 등록되었습니다.');
            navigate('/');
        } catch (error) {
            console.error('Error:', error);
            if (error.response.status === 500) {
                alert('이미 등록된 식당이 있습니다.');
            }
        }
    };

    return (
        <div className="res-register-full-container">
            <div className='register-container'>
                <div className="container-header">
                    <div className="register-header">
                        <span className="register-text">Restaurant Registration</span><span className="register-text">식당 등록</span>
                    </div>
                    <div className="register-button">
                        <button className='submit-button' onClick={handleSubmit}>등록하기</button>
                    </div>
                </div>

                <div className='total-container'>
                    <div className="left-container">
                        <div className="left-label-input-container">
                            <label htmlFor="restaurantImage">식당 사진</label>
                            <input className="image-input" type="file" id="restaurantImage" accept="image/*" onChange={handleImageChange} />
                        </div>
                        {previewImage && <img src={previewImage} alt="미리보기" className="preview-image" width={250} height={150}/>}
                
                        <div className="left-label-input-container">
                            <label htmlFor="restaurantName">식당 이름</label>
                            <input className="info-input" type="text" id="restaurantName" value={restaurantName} onChange={(e) => setRestaurantName(e.target.value)} />
                        </div>

                        <div className="left-label-input-container">
                            <label htmlFor="phoneNumber">전화번호 </label>
                            <input className="info-input" type="text" id="phoneNumber" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} />
                        </div>
                        
                        <div className="left-label-input-container">
                            <label htmlFor="address">식당 주소</label>
                            <input className="info-input" type="text" id="address" value={address} onChange={(e) => setAddress(e.target.value)} />
                        </div>

                        <div className="left-label-input-container">
                            <label htmlFor="storeDescription">식당 설명</label>
                            <input className="info-input" type="text" id="storeDescription" value={storeDescription} onChange={(e) => setStoreDescription(e.target.value)} />
                        </div>
                    </div>

                    <div className="right-container">
                        <label htmlFor="foodtype">식당 음식 종류</label>
                        <select className="select-style" value={foodCategory} onChange={(e) => setFoodCategory(e.target.value)}>
                            <option value="">음식 종류를 선택하세요</option>
                            <option value="한식">한식</option>
                            <option value="양식">양식</option>
                            <option value="아시아음식">아시아음식</option>
                            <option value="일식">일식</option>
                            <option value="중식">중식</option>
                            <option value="분식">분식</option>
                            <option value="카페">카페</option>
                            <option value="뷔페">뷔페</option>
                            <option value="기타">기타</option>
                        </select>
                        
                        <label htmlFor="opentime">시작 시간 / 마감 시간</label>
                        <div className="time-input-container">
                            <input className="time-input" type="time" id="openTime" value={openTime} onChange={(e) => setOpenTime(e.target.value)} />
                            <input className="time-input" type="time" id="closeTime" value={closeTime} onChange={(e) => setCloseTime(e.target.value)} />
                        </div>
                        
                        <label htmlFor="takeouttime">포장 준비 시간</label>
                        <select className="select-style" value={timeCategory} onChange={(e) => setTimeCategory(e.target.value)}>
                            <option value="">시간을 선택하세요</option>
                            <option value="10">10분</option>
                            <option value="20">20분</option>
                            <option value="30">30분</option>
                            <option value="40">40분</option>
                            <option value="50">50분</option>
                            <option value="60">60분</option>
                        </select>
                        
                        <div>
                            <label htmlFor="reservationsAllowed">예약 가능 여부</label>
                            <input type="checkbox" id="reservationsAllowed" checked={reservationsAllowed} onChange={() => setReservationsAllowed(!reservationsAllowed)} />
                        </div>
                        
                        {reservationsAllowed && (
                            <div className="reservation-input-container">
                                <div className="input-row">
                                    <label htmlFor="maxReservations">예약 최대 가능 팀</label>
                                    <input className="res-input" type="number" id="maxReservations" value={maxReservations} onChange={(e) => setMaxReservations(e.target.value)} />
                                </div>
                                <div className="input-row">
                                    <label htmlFor="reservationCancelTime">예약 취소 가능 시간(시간:분)</label>
                                    <input className="info-input" type="text" id="reservationCancelTime" value={reservationCancelTime} onChange={(e) => setReservationCancelTime(e.target.value)} />
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RestaurantInfo;
