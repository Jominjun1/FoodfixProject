import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './RestaurantInquiry.css'; 

const RestaurantInquiry = () => {
    const [storeInfo, setStoreInfo] = useState(null);
    const [storeImageSrc, setStoreImageSrc] = useState('');
    const [editModalOpen, setEditModalOpen] = useState(false);
    const [restaurantInfo, setRestaurantInfo] = useState({
        store_name: '',
        store_address: '',
        store_category: '',
        store_phone: '',
        store_image: '', 
        store_intro: '',
        openTime: '',
        closeTime: '',
        minimumTime: '',
        res_status: '',
        res_max: '',
        reservationCancel: '',
    });

    const handleOpenModal = () => {
        setEditModalOpen(true); 
    };

    const handleCloseModal = () => {
        setEditModalOpen(false);
    };

    const fetchData = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/store`, {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                }
            });
            if (response.status === 200) {
                const store = response.data;
                setStoreInfo(store);
                setRestaurantInfo(response.data);

                const imagePath = store.imagePath;
                if (imagePath) {
                    const imageName = imagePath.split('/').pop();
                    const imageSrc = `http://54.180.213.178:8080/images/${imageName}`;
                    setStoreImageSrc(imageSrc);
                } else {
                    setStoreImageSrc('/images/no_image.jpg'); 
                }
            } else {
                alert('Failed to fetch store information');
            }
        } catch (error) {
            console.error('Error fetching store:', error.message);
        }
    };

    const handleInputChange = (e) => {
        if (e.target.name === 'store_image') {
            setRestaurantInfo({ ...restaurantInfo, store_image: e.target.files[0] });
        } else {
            setRestaurantInfo({ ...restaurantInfo, [e.target.name]: e.target.value });
        }
    };

    useEffect(() => {
        fetchData(); 
    }, []); 


    if (!storeInfo) {
        return <div>로딩 중...</div>; 
    }

    const handleSubmit = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const formData = new FormData();
            formData.append('imageFile', restaurantInfo.store_image);
            formData.append('store_name', restaurantInfo.store_name);
            formData.append('store_phone', restaurantInfo.store_phone);
            formData.append('store_address', restaurantInfo.store_address);
            formData.append('store_category', restaurantInfo.store_category);
            formData.append('store_intro', restaurantInfo.store_intro);
            formData.append('openTime', restaurantInfo.openTime);
            formData.append('closeTime', restaurantInfo.closeTime);
            formData.append('minimumTime', restaurantInfo.minimumTime);
            formData.append('res_status', restaurantInfo.res_status);
            formData.append('res_max', restaurantInfo.res_max);
            formData.append('reservationCancel', restaurantInfo.reservationCancel);

            await axios.put(`${process.env.REACT_APP_SERVER_URL}/admin/updatestore`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            alert('식당 정보가 업데이트되었습니다.');
            window.location.reload();
        } catch (error) {
            console.error('Error:', error);
            alert('식당 정보 업데이트에 실패했습니다.');
        }
    };

    const [cancelHour, cancelMinute] = storeInfo.reservationCancel.split(':');

    const handleDeleteRestaurant = async () => {
        const confirmDelete = window.confirm('식당을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.');
    
        if (confirmDelete) {
            const token = sessionStorage.getItem('token');
    
            try {
                const response = await axios.delete(`${process.env.REACT_APP_SERVER_URL}/admin/deletestore`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                    credentials: 'include'
                });
            
                if (response.status === 200) {
                    console.log('Restaurant deletion successful');
                    alert('식당이 삭제되었습니다.');

                    setStoreInfo(null);
                } else {
                    console.error('Restaurant deletion failed');
                    alert('식당 삭제에 실패했습니다.');
                }
            } catch (error) {
                console.error('Error during restaurant deletion:', error.message);
            }   
        }
    };

    return (
        <div className="restaurant-full-container">
            <div className="restaurant-container">
                <div className="res-title-container">
                    <div className="res-title">
                        <span className='title-text'>Restaurant Information</span><span className="title-text">{storeInfo.store_name} ({storeInfo.store_id})</span>
                    </div>
                    <div className="res-button">
                        <div className="submit-button" onClick={handleOpenModal}>식당 수정</div>
                        <div className="submit-button" onClick={handleDeleteRestaurant}>식당 삭제</div> 
                    </div>
                </div>

                <div className="res-info-total-container">
                    <div className="info-left-container">
                        <img src={storeImageSrc} alt="매장 이미지" width={550} height={400}/>
                    </div>

                    <div className="right-container">
                        <div className="right-label-input-container">
                            <p>식당 설명</p><span>{storeInfo.store_intro}</span>
                        </div>
                        <div className="right-label-input-container">
                            <p>식당 주소</p><span>{storeInfo.store_address}</span>
                        </div>
                        <div className="right-label-input-container">
                            <p>식당 전화번호</p><span>{storeInfo.store_phone}</span>
                        </div>
                        <div className="right-label-input-container">
                            <p>음식 카테고리</p><span>{storeInfo.store_category}</span>
                        </div>
                        <div className="right-label-input-container">
                            <p>오픈 시간 / 마감 시간</p><span>{storeInfo.openTime} / {storeInfo.closeTime}</span>
                        </div>
                        <div className="right-label-input-container">
                            <p>포장 최소 준비 시간</p><span>{storeInfo.minimumTime}분</span>
                        </div>
                        <p className="reservation-text">예약 가능 여부 / 예약 최대 가능팀 / 예약 취소 가능 시간</p>
                        <span className="right-container-span">{storeInfo.res_status === '1' ? '가능' : '불가능'} / {storeInfo.res_max}팀 / 예약시간 {cancelHour}시 {cancelMinute}분전까지 가능</span>
                    </div>
                </div>
            </div>

            {editModalOpen && (
                <div className="edit-modal">
                    <div className="edit-modal-content">
                        <span className="edit-modal-close" onClick={handleCloseModal}>&times;</span><br />
                        <label htmlFor="store_image">식당 사진</label><br />
                        <input type="file" id="store_image" name="store_image" onChange={handleInputChange} /><br />
                
                        <label htmlFor="store_name">식당 이름</label><br />
                        <input type="text" id="store_name" name="store_name" value={restaurantInfo.store_name} onChange={handleInputChange} /><br />
                
                        <label htmlFor="store_phone">전화번호 </label><br />
                        <input type="text" id="store_phone" name="store_phone" value={restaurantInfo.store_phone} onChange={handleInputChange} /><br />
                
                        <label htmlFor="store_address">식당 주소</label><br />
                        <input type="text" id="store_address" name="store_address" value={restaurantInfo.store_address} onChange={handleInputChange} /><br />
                
                        <label htmlFor="store_intro">식당 설명</label><br />
                        <input type="text" id="store_intro" name="store_intro" value={restaurantInfo.store_intro} onChange={handleInputChange} /><br />
                
                        <label htmlFor="store_category">음식 종류</label><br />
                        <select id="store_category" name="store_category" value={restaurantInfo.store_category} onChange={handleInputChange}>
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
                        </select><br />
                
                        <label htmlFor="openTime">시작 시간 / 마감시간</label><br />
                        <input type="time" id="openTime" name="openTime" value={restaurantInfo.openTime} onChange={handleInputChange} />
                        <input type="time" id="closeTime" name="closeTime" value={restaurantInfo.closeTime} onChange={handleInputChange} /><br />
                
                        <label htmlFor="minimumTime">포장 준비 시간</label><br />
                        <select id="minimumTime" name="minimumTime" value={restaurantInfo.minimumTime} onChange={handleInputChange}>
                            <option value="">시간을 선택하세요</option>
                            <option value="10">10분</option>
                            <option value="20">20분</option>
                            <option value="30">30분</option>
                            <option value="40">40분</option>
                            <option value="50">50분</option>
                            <option value="60">60분</option>
                        </select><br />
                
                        <label htmlFor="res_status">예약 가능 여부</label><br />
                        <input type="checkbox" id="res_status" name="res_status" checked={restaurantInfo.res_status} onChange={() => setRestaurantInfo({ ...restaurantInfo, res_status: !restaurantInfo.res_status })} /><br />

                
                        {restaurantInfo.res_status && (
                            <div className="reservation-input-container">
                                <label htmlFor="res_max">예약 최대 가능 팀</label><br />
                                <input type="number" id="res_max" name="res_max" value={restaurantInfo.res_max} onChange={handleInputChange} /><br />
                        
                                <label htmlFor="reservationCancel">예약 취소 가능 시간(시간:분)</label><br />
                                <input type="text" name="reservationCancel" value={restaurantInfo.reservationCancel} onChange={handleInputChange} /><br />
                            </div>
                        )}
                        <button className="edit-submit-button" onClick={handleSubmit}>수정하기</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default RestaurantInquiry;