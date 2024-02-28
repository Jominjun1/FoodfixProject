import React, { useState } from 'react';
import axios from 'axios';
import './RestaurantInfo.css'; 

const RestaurantInfo = () => {
    const [photo, setPhoto] = useState(null);
    const [photoPreview, setPhotoPreview] = useState('');
    const [restaurantName, setRestaurantName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [address, setAddress] = useState('');
    const [details, setDetails] = useState({
        parking: false,
        petFriendly: false,
        wifi: false,
        reservation: false,
        groupUse: false,
        takeout: false,
        waiting: false
    });
    const [foodCategory, setFoodCategory] = useState('');

    const handlePhotoChange = (event) => {
        const selectedPhoto = event.target.files[0];
        setPhoto(selectedPhoto);
        
        const reader = new FileReader();
        reader.onloadend = () => {
            setPhotoPreview(reader.result);
        };
        reader.readAsDataURL(selectedPhoto);
    };

    const handleDetailChange = (detail) => {
        setDetails(prevDetails => ({
            ...prevDetails,
            [detail]: !prevDetails[detail]
        }));
    }; 

    return (
        <div className="container">
            <div className="photo-container">
                <div className="photo-box">
                    {!photoPreview && <h2>식당 대표 사진</h2>}
                    {photoPreview && <img src={photoPreview} alt="Selected" />}
                </div>
                <div className="upload-btn">
                    <input type="file" id="photo" accept="image/*" onChange={handlePhotoChange} />
                </div>
            </div>

            <div className="info-container">
                <div className="info-content-background">
                    <div>
                        <h3>식당 정보</h3>
                        <label htmlFor="restaurantName">식당 이름</label>
                        <input className="info-input" type="text" id="restaurantName" value={restaurantName} onChange={(e) => setRestaurantName(e.target.value)} /><br></br>
                        <label htmlFor="phoneNumber">전화번호</label>
                        <input className="info-input" type="text" id="phoneNumber" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} /><br></br>
                        <label htmlFor="address">주소</label>
                        <input className="info-input" type="text" id="address" value={address} onChange={(e) => setAddress(e.target.value)} />
                    </div>
                    <div>
                        <h3>상세 정보</h3>
                        <label>
                            <input type="checkbox" checked={details.parking} onChange={() => handleDetailChange('parking')} />
                            주차장
                        </label>
                        <label>
                            <input type="checkbox" checked={details.petFriendly} onChange={() => handleDetailChange('petFriendly')} />
                            반려동물
                        </label>
                        <label>
                            <input type="checkbox" checked={details.wifi} onChange={() => handleDetailChange('wifi')} />
                            와이파이
                        </label>
                        <label>
                            <input type="checkbox" checked={details.reservation} onChange={() => handleDetailChange('reservation')} />
                            예약
                        </label>
                        <label>
                            <input type="checkbox" checked={details.groupUse} onChange={() => handleDetailChange('groupUse')} />
                            단체석
                        </label>
                        <label>
                            <input type="checkbox" checked={details.takeout} onChange={() => handleDetailChange('takeout')} />
                            포장
                        </label>
                        <label>
                            <input type="checkbox" checked={details.waiting} onChange={() => handleDetailChange('takeout')} />
                            대기공간
                        </label>
                    </div>

                    <div className="food-category">
                    <h3>음식 종류</h3>
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
                </div>
                </div>
            </div>

            <button className='submit-button'>등록하기</button>
        </div>
    );
};

export default RestaurantInfo;

