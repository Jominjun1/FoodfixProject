import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import './ResManagement.css';

const ResManagement = () => {
    const navigate = useNavigate();
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [showDatePicker, setShowDatePicker] = useState(false);

    const handleHomeClick = () => {
        navigate('/');
    };

    useEffect(() => {
        window.scrollTo(0, 0);
    }, []);

    const handleDateChange = (date) => {
        setSelectedDate(date);
        setShowDatePicker(false);
    };

    const person = [
        { id: 1, name: "김ㅇㅇ", phoneNumber: "010-1234-5678", date: "nn월 nn일", time: "nn시 nn분", number: "n명" },
        { id: 2, name: "박ㅇㅇ", phoneNumber: "010-5678-1234", date: "nn월 nn일", time: "nn시 nn분", number: "n명" },
        { id: 3, name: "이ㅇㅇ", phoneNumber: "010-9876-5432", date: "nn월 nn일", time: "nn시 nn분", number: "n명" }
    ];

    return (
        <>
            <div className="reservation">
                <div className="top-logo">
                    <img src='/images/logo.png' alt="푸드픽스 로고" onClick={handleHomeClick} />
                </div>

                <div className="date-picker-container">
                    <div className="date-picker" onClick={() => setShowDatePicker(true)}>
                        {selectedDate ? selectedDate.toLocaleDateString() : "Select Date"}
                    </div>
                    {showDatePicker && (
                        <div className="date-picker-modal">
                            <DatePicker
                                selected={selectedDate}
                                onChange={handleDateChange}
                                dateFormat="MM/dd/yyyy"
                                inline
                            />
                        </div>
                    )}
                </div>
            </div>

            {person.map(person => (
                <div key={person.id} className="res-container">
                    <div className="res-content-background">
                        <div>
                            <h3>{person.id}</h3>
                            <div className='person-info'>
                                <strong>예약자 이름 </strong> <span>{person.name}</span>
                                <strong>예약자 전화번호 </strong> <span>{person.phoneNumber}</span>
                            </div>
                            <div className='res-info'>
                                <img src='/images/calendar.png' alt='달력 일러스트' className='icon'></img> <strong>날짜 </strong> <span>{person.date}</span>
                                <img src='/images/clock.png' alt='시계 일러스트' className='icon'></img> <strong>시간 </strong> <span>{person.time}</span>
                                <img src='/images/person.png' alt='사람 일러스트' className='icon'></img> <strong>인원수 </strong> <span>{person.number}</span>
                                <strong>요청사항 </strong>

                                <div className='button-groups'>
                                    <div>
                                        <button>예약 취소하기</button>
                                    </div>
                                    <div>
                                        <button>예약 확정하기</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            ))}
        </>
    );
};

export default ResManagement;

