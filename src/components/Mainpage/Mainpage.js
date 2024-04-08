import React, { useState, useEffect } from 'react';
import './Mainpage.css';
import Header from './Header';
import { MainSection, RestaurantSection, MenuSection, InformationSection } from './Contents';
import { FaRegArrowAltCircleUp } from 'react-icons/fa';

const Mainpage = () => {
    const [showScroll, setShowScroll] = useState(false);

    useEffect(() => {
        window.scrollTo(0, 0);
        
        const handleScroll = () => {
            if (window.pageYOffset > 300) {
                setShowScroll(true);
            } else {
                setShowScroll(false);
            }
        };

        window.addEventListener('scroll', handleScroll);

        return () => window.removeEventListener('scroll', handleScroll);
    }, []);

    const scrollToTop = () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    return (
        <div>
            <Header />
            <MainSection />
            <RestaurantSection />
            <MenuSection />
            <InformationSection />
            {showScroll && (
                <button className="scroll-top" onClick={scrollToTop}>
                    <FaRegArrowAltCircleUp />
                </button>
            )}
        </div>
    );
};

export default Mainpage;