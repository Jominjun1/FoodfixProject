import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Signin from "./components/Signin-Login/Signin";
import Login from "./components/Signin-Login/Login";
import Mainpage from './components/Mainpage/Mainpage';
import Information from './components/Information/Content';
import Reservation from './components/Reservation/ResManagement';
import Order from './components/Order/OrderManagement';
import Mypage from './components/Mypage/Mypage';

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signin" element={<Signin />} />
          <Route path="/" element={<Mainpage />} />
          <Route path='/info' element={<Information />} />
          <Route path='/res' element={<Reservation />} />
          <Route path ='/order' element={<Order />} />
          <Route path='/mypage' element={<Mypage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
