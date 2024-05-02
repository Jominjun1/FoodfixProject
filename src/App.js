import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Signup from "./components/Signin-Login/Signup";
import Login from "./components/Signin-Login/Login";
import Mainpage from './components/Mainpage/Mainpage';
import Restaurant from './components/Restaurant/RestaurantInfo';
import RestaurantInquiry from './components/Restaurant/RestaurantInquiry';
import Menu from './components/Menu/Menu';
import Information from './components/Information/Content';
import Mypage from './components/Mypage/Mypage';

function App() {
  return (
      <Router>
        <div>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/" element={<Mainpage />} />
            <Route path='/info' element={<Information />} />
            <Route path='/restaurant' element={<Restaurant />} />
            <Route path='/restaurant-info' element={<RestaurantInquiry />} />
            <Route path ='/menu' element={<Menu />} />
            <Route path='/mypage' element={<Mypage />} />
          </Routes>
        </div>
      </Router>
    
  );
}

export default App;
