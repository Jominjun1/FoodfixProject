import React, { useState } from 'react';
import './MenuManagement.css'; 

function MenuPage() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMenuIndex, setModalMenuIndex] = useState(null); 
  const [menuName, setMenuName] = useState('');
  const [menuPrice, setMenuPrice] = useState('');
  const [menuInfo, setMenuInfo] = useState('');
  const [menuImage, setMenuImage] = useState(null);
  const [addedMenus, setAddedMenus] = useState([]); 

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => {
    setIsModalOpen(false);
    setModalMenuIndex(null); 
  };

  const handleMenuNameChange = (e) => setMenuName(e.target.value);
  const handleMenuPriceChange = (e) => setMenuPrice(e.target.value);
  const handleMenuInfoChange = (e) => setMenuInfo(e.target.value);

  const handleImageChange = (e) => {
    const imageFile = e.target.files[0];
    if (imageFile) {
      const imageUrl = URL.createObjectURL(imageFile);
      setMenuImage(imageUrl);
    }
  };

  const handleAddMenu = () => {
    const newMenu = { name: menuName, price: menuPrice, info: menuInfo, image: menuImage };
    setAddedMenus([...addedMenus, newMenu]);

    setMenuName('');
    setMenuPrice('');
    setMenuInfo('');
    setMenuImage(null);

    closeModal();
  };

  const handleEditMenu = (index) => {
    setModalMenuIndex(index);
    const menuToEdit = addedMenus[index]; 
    setMenuName(menuToEdit.name); 
    setMenuPrice(menuToEdit.price); 
    setMenuInfo(menuToEdit.info);
    setMenuImage(menuToEdit.image); 
    openModal();
  };

  const handleUpdateMenu = () => {
    const updatedMenus = [...addedMenus];
    updatedMenus[modalMenuIndex] = { name: menuName, price: menuPrice, info: menuInfo, image: menuImage };
    setAddedMenus(updatedMenus);

    closeModal();
  };

  const handleDeleteMenu = (index) => {
    const updatedMenus = [...addedMenus];
    updatedMenus.splice(index, 1);
    setAddedMenus(updatedMenus);
  };

  const handleMenuDescriptionToggle = (index) => {
    const descriptionElement = document.getElementById(`menu-description-${index}`);
    descriptionElement.classList.toggle('expanded');
  };

  return (
    <div>
      <button className="add-button" onClick={openModal}>+</button> 
      {isModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <span className="close" onClick={closeModal}>&times;</span>
            <input type="file" accept="image/*" onChange={handleImageChange} />
            {menuImage && <img src={menuImage} alt="메뉴 이미지" className="menu-image-preview" />}
            <input type="text" placeholder="메뉴 이름" value={menuName} onChange={handleMenuNameChange} />
            <input type="text" placeholder="가격" value={menuPrice} onChange={handleMenuPriceChange} />
            <input type="text" placeholder="메뉴 설명" value={menuInfo} onChange={handleMenuInfoChange} />
            <button onClick={modalMenuIndex !== null ? handleUpdateMenu : handleAddMenu}>
              {modalMenuIndex !== null ? "수정하기" : "등록하기"}
            </button>
          </div>
        </div>
      )}

      <div className="added-menus">
        {addedMenus.map((menu, index) => (
          <div key={index} className="menu-item">
            <img src={menu.image} alt="메뉴 이미지" />
            <p>메뉴 이름: {menu.name}</p>
            <p>가격: {menu.price}</p>
            <p
              className="menu-description"
              id={`menu-description-${index}`}
              onClick={() => handleMenuDescriptionToggle(index)}>
              {menu.info}
            </p>
            <div className="menu-buttons">
              <button onClick={() => handleEditMenu(index)}>수정하기</button>
              <button onClick={() => handleDeleteMenu(index)}>삭제하기</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default MenuPage;
