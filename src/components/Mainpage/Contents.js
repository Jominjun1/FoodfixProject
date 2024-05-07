import React from 'react';
import './Contents.css';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { motion } from "framer-motion";
import useObserver, { opacityVariants } from "../Mainpage/useObserver";

export const MainSection = () => {
  const { ref, animation } = useObserver();

  return (
      <section id="main" className='fullscreen-section'>
      <motion.div
        ref={ref}
        initial="hidden"
        animate={animation}
        variants={opacityVariants}
        className='main-image-container'
      >
        <img
          src="/images/main-background.jpg"
          alt="main-background"
          className='main-image'
        />
        <div className='main-text'>
          <p className='main-text-header'>맛있는 선택</p>
          <p className='main-text-header'>당신을 위한 푸드픽스</p>
        </div>
      </motion.div>
    </section>
  );
};

export const RestaurantSection = () => {
  const navigate = useNavigate();
  const { ref, animation } = useObserver();
  const [hasRestaurantInfo, setHasRestaurantInfo] = useState(false);

  useEffect(() => {
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
                  setHasRestaurantInfo(true);
              }
          } catch (error) {
              console.error('Error fetching store information:', error);
              setHasRestaurantInfo(false);
          }
      };

      fetchData();
  }, []);

  const handleButtonClick = () => {
      if (hasRestaurantInfo) {
          navigate('/restaurant-info');
      } else {
          navigate('/restaurant');
      }
  };

  return (
      <section id="restaurant" className='fullscreen-section'>
          <motion.div
              ref={ref}
              initial="hidden"
              animate={animation}
              variants={opacityVariants}
              className='restaurant-image-container'
          >
              <img
                  src="/images/001.jpg"
                  alt="restaurant-background"
                  className='restaurant-image'
              />
              <div className='restaurant-text'>
                  <p className='restaurant-text-header'>식당 등록 및 관리</p>
                  <p className='restaurant-text-header'>Restaurant registration & management</p>
                  <p className='restaurant-text-content'>식당의 정보를 등록하고 편리하게 관리할 수 있습니다</p>
                  <p className='restaurant-text-content'>You can register restaurant information<br></br>and manage restaurant information conveniently</p>
                  <button className='restaurant-custom-button' onClick={handleButtonClick}>식당 등록 및 관리</button>
              </div>
          </motion.div>
      </section>
  );
};

export const MenuSection = () => {
    const navigate = useNavigate();
    const { ref, animation } = useObserver();
  
    const handleButtonClick = () => {
        navigate('/menu');
    };
  
    return (
        <section id="menu" className='fullscreen-section'>
        <motion.div
          ref={ref}
          initial="hidden"
          animate={animation}
          variants={opacityVariants}
          className='menu-image-container'
        >
          <img
            src="/images/003.jpg"
            alt="menu-background"
            className='menu-image'
          />
          <div className='menu-text'>
            <p className='menu-text-header'>메뉴 등록 및 관리</p>
            <p className='menu-text-header'>Menu registration & management</p>
            <p className='menu-text-content'>식당의 메뉴를 등록하고 편리하게 관리할 수 있습니다</p>
            <p className='menu-text-content'>You can register menu information<br></br>and manage menu information conveniently</p>
            <button className='menu-custom-button' onClick={handleButtonClick}>메뉴 등록 및 관리</button>
          </div>
        </motion.div>
      </section>
    );
};

export const InformationSection = () => {
    const navigate = useNavigate();
    const { ref, animation } = useObserver();
  
    const handleButtonClick = () => {
      navigate("/content");
    };
  
    return (
      <section id="info" className='fullscreen-section'>
        <motion.div
          ref={ref}
          initial="hidden"
          animate={animation}
          variants={opacityVariants}
          className='info-image-container'
        >
          <img
            src="/images/002.jpg"
            alt="info-background"
            className='info-image'
          />
          <div className='info-text'>
            <p className='info-text-header'>예약 및 주문 관리</p>
            <p className='info-text-header'>Reservation & Order Management</p>
            <p className='info-text-content'>식당의 예약과 포장 주문을 편리하게 관리할 수 있습니다</p>
            <p className='info-text-content'>You can manage restaurant reservation<br></br>and takeaway orders conveniently</p>
            <button className='info-custom-button' onClick={handleButtonClick}>예약 및 주문 관리</button>
          </div>
        </motion.div>
      </section>
    );
};