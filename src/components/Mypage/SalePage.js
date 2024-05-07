import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import './SalePage.css';

const SalePage = () => {
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [selectedMonth, setSelectedMonth] = useState(new Date());
    const [orders, setOrders] = useState([]);
    const [totalPriceByDate, setTotalPriceByDate] = useState(0);
    const [totalPriceByMonth, setTotalPriceByMonth] = useState(0);
    const [graphData, setGraphData] = useState([]);

    const fetchOrders = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/getPacking`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const allOrders = response.data;
            setOrders(allOrders);
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    };

    const calculateTotalPriceByDate = useCallback(() => {
        const filteredOrders = orders.filter(order => {
            const orderDate = new Date(`${order.packing_date} ${order.packing_time}`);
            return orderDate.toDateString() === selectedDate.toDateString() && order.packing_status !== "2";
        });

        let totalPrice = 0;
        filteredOrders.forEach(order => {
            if (order.menuItemDTOList) {
                order.menuItemDTOList.forEach(menuItem => {
                    totalPrice += menuItem.quantity * menuItem.menu_price;
                });
            }
        });
        setTotalPriceByDate(totalPrice);
    }, [orders, selectedDate]);

    const calculateTotalPriceByMonth = useCallback(() => {
        const filteredOrders = orders.filter(order => {
            const orderDate = new Date(`${order.packing_date} ${order.packing_time}`);
            return orderDate.getMonth() === selectedMonth.getMonth() && order.packing_status !== "2";
        });

        let totalPrice = 0;
        filteredOrders.forEach(order => {
            if (order.menuItemDTOList) {
                order.menuItemDTOList.forEach(menuItem => {
                    totalPrice += menuItem.quantity * menuItem.menu_price;
                });
            }
        });
        setTotalPriceByMonth(totalPrice);
    }, [orders, selectedMonth]);

    const calculateTotalPriceByDay = useCallback((date) => {
        const filteredOrders = orders.filter(order => {
            const orderDate = new Date(`${order.packing_date} ${order.packing_time}`);
            return orderDate.toDateString() === date.toDateString() && order.packing_status !== "2";
        });

        let totalPrice = 0;
        filteredOrders.forEach(order => {
            if (order.menuItemDTOList) {
                order.menuItemDTOList.forEach(menuItem => {
                    totalPrice += menuItem.quantity * menuItem.menu_price;
                });
            }
        });
        return totalPrice;
    }, [orders]);

    const generateGraphData = useCallback(() => {
        const startDate = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth(), 1);
        const endDate = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() + 1, 0);
        const graphData = [];
        let maxPrice = 0;

        for (let date = startDate; date <= endDate; date.setDate(date.getDate() + 1)) {
            const totalPrice = calculateTotalPriceByDay(date);
            if (totalPrice > maxPrice) {
                maxPrice = totalPrice;
            }
            graphData.push({ date: date.toDateString(), totalPrice });
        }

        const maxHeight = 200;

        graphData.forEach(data => {
            data.barHeight = (data.totalPrice / maxPrice) * maxHeight;
        });

        return graphData;
    }, [selectedMonth, calculateTotalPriceByDay]);

    useEffect(() => {
        fetchOrders();
    }, []);

    useEffect(() => {
        calculateTotalPriceByDate();
        calculateTotalPriceByMonth();
    }, [calculateTotalPriceByDate, calculateTotalPriceByMonth]);

    useEffect(() => {
        const graphData = generateGraphData();
        setGraphData(graphData);
    }, [generateGraphData]);

    return (
        <div className='sale-container'>
            <div className='background'>
                <div className='section1'>
                    <DatePicker
                        selected={selectedDate}
                        onChange={date => setSelectedDate(date)}
                        dateFormat="yyyy-MM-dd"
                    />
                    <p className='order-description'> {selectedDate.getMonth() + 1}월 {selectedDate.getDate()}일의 매출 : {totalPriceByDate}</p>
                </div>

                <div className='section2'>
                    <div className='date-picker-container'>
                        <DatePicker
                            selected={selectedMonth}
                            onChange={date => setSelectedMonth(date)}
                            dateFormat="yyyy-MM"
                            showMonthYearPicker
                        />
                        <p className='order-total-description'>{selectedDate.getMonth() + 1}월의 매출 : {totalPriceByMonth}</p>
                    </div>

                    <div className='graph-container'>
                        {graphData.map(data => (
                            <div className='bar' key={data.date} style={{ height: `${data.barHeight}px` }}>
                                <p className='bar-label'><span data-day={data.date.split(' ')[2]}></span></p>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SalePage;