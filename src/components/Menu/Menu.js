import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Menu.css';
import { useNavigate } from 'react-router-dom';

const Menu = () => {
    const [menuName, setMenuName] = useState('');
    const [menuPrice, setMenuPrice] = useState('');
    const [menuDescription, setMenuDescription] = useState('');
    const [menuImage, setMenuImage] = useState(null);
    const [menuImagePreview, setMenuImagePreview] = useState('');
    const [editMenuImagePreview, setEditMenuImagePreview] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    const noImage = '/images/no_image.png';

    const [storeInfo, setStoreInfo] = useState(null);
    const [menuList, setMenuList] = useState([]);
    const [editMenu, setEditMenu] = useState(null);
    const [imageFile, setImageFile] = useState(null);

    const navigate = useNavigate();
    const handleHomeClick = () => {
        navigate('/');
    }

    const handleMenuNameChange = (e) => setMenuName(e.target.value);
    const handleMenuPriceChange = (e) => setMenuPrice(e.target.value);
    const handleMenuDescriptionChange = (e) => setMenuDescription(e.target.value);
    const handleImageChange = (e) => {
        const file = e.target.files[0];
        setMenuImage(file);
        const reader = new FileReader();
        reader.onloadend = () => {
            setMenuImagePreview(reader.result);
        };
        reader.readAsDataURL(file);
    };

    const openModal = () => {
        setIsModalOpen(true);
        setMenuName('');
        setMenuPrice('');
        setMenuDescription('');
        setMenuImage(null);
        setMenuImagePreview('');
    };
    
    const closeModal = () => {
        setIsModalOpen(false);
        setMenuImagePreview('');
    };

    const handleSubmit = async () => {
        const formData = new FormData();
        formData.append('imageFile', menuImage);
        formData.append('menu_name', menuName);
        formData.append('menu_price', menuPrice);
        formData.append('explanation', menuDescription);

        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/admin/newmenu`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${token}`
                }
            });
            console.log('Success:', response.data);
            alert('메뉴가 등록되었습니다.');
            closeModal(); 
            fetchMenuData();
        } catch (error) {
            console.error('Error:', error);
            alert('에러가 발생했습니다. 다시 시도해주세요.');
        }
    };

    const fetchMenuData = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/menus`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            const menuDataWithImages = await Promise.all(response.data.map(async (menu) => {
                const imageName = menu.imagePath ? menu.imagePath.split('/').pop() : null;
                const imageUrl = imageName ? `http://54.180.213.178:8080/images/${imageName}` : noImage;
                return { ...menu, menu_image: imageUrl };
            }));
            setMenuList(menuDataWithImages);
        } catch (error) {
            console.error('메뉴 정보를 불러오는 중 에러:', error);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = sessionStorage.getItem('token');
                const response = await axios.get(`${process.env.REACT_APP_SERVER_URL}/admin/store`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setStoreInfo(response.data);
            } catch (error) {
                console.error('에러:', error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        if (storeInfo) {
            fetchMenuData();
        }
    }, [storeInfo]);

    const handleEdit = (menuId) => {
        const editMenu = menuList.find(menu => menu.menu_id === menuId);
        setEditMenu(editMenu);
        setEditMenuImagePreview(''); 
    };

    const handleUpdate = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const formData = new FormData();
            formData.append('imageFile', imageFile); 
            Object.keys(editMenu).forEach(key => formData.append(key, editMenu[key])); 

            await axios.put(`${process.env.REACT_APP_SERVER_URL}/admin/updatemenu`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'multipart/form-data'
                }
            });
            console.log('Update menu:', editMenu.menu_id);
            fetchMenuData();
            setEditMenu(null);
        } catch (error) {
            console.error('메뉴 수정 중 에러:', error);
        }
    };

    const handleImageEdit = (event) => {
        const file = event.target.files[0];
        setImageFile(file); 
    
        const reader = new FileReader();
        reader.onloadend = () => {
            setEditMenuImagePreview(reader.result);
        };
        reader.readAsDataURL(file);
    };
    

    const handleDelete = async (menu_id) => {
        try {
            const token = sessionStorage.getItem('token');
            await axios.delete(`${process.env.REACT_APP_SERVER_URL}/admin/deletemenu/${menu_id}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            console.log('Delete menu:', menu_id);
            fetchMenuData();
        } catch (error) {
            console.error('메뉴 삭제 중 에러:', error);
        }
    };

    const toggleDescription = (menuId) => {
        const updatedMenuList = menuList.map(menu => {
            if (menu.menu_id === menuId) {
                return { ...menu, showFullDescription: !menu.showFullDescription };
            }
            return menu;
        });
        setMenuList(updatedMenuList);
    };

    if (!storeInfo) {
        return <div>로딩 중...</div>;
    }

    return (
        <div>
            <div className="menu-page-header">
                <div className="menu-top-logo">
                    <img src='/images/logo.png' alt="푸드픽스 로고" onClick={handleHomeClick} />
                </div>
                <div className="menu-page-title">
                    <span>메뉴 관리</span>
                </div>
                <div className="menu-add-button">
                    <button className="add-button" onClick={openModal}>+</button>
                </div>
            </div>

            {isModalOpen && (
                <div className="menu-modal">
                    <div className="menu-modal-content">
                        <span className="close" onClick={closeModal}>&times;</span>
                        <input type="file" accept="image/*" onChange={handleImageChange} />
                        {menuImagePreview && <img src={menuImagePreview} alt="메뉴 이미지" className="menu-image-preview" />}
                        <input type="text" placeholder="메뉴 이름" value={menuName} onChange={handleMenuNameChange} />
                        <input type="text" placeholder="가격" value={menuPrice} onChange={handleMenuPriceChange} />
                        <input type="text" placeholder="메뉴 설명" value={menuDescription} onChange={handleMenuDescriptionChange} />
                        <button onClick={handleSubmit}>등록하기</button>
                    </div>
                </div>
            )}

            <div className="menu-info-view">
                <div className="menu-info-container">
                    {menuList.map(menu => (
                        <div className="menu-items" key={menu.menu_id}>
                            <img src={menu.menu_image} alt={menu.menu_name} />
                            <p className="menu-name">{menu.menu_name} ({menu.menu_id})</p>
                            <p className="menu-price">가격: {menu.menu_price}</p>
                            <p className="menu-description" onClick={() => toggleDescription(menu.menu_id)}>
                                {menu.explanation.length > 10 && !menu.showFullDescription ? (
                                    <>
                                        {menu.explanation.substring(0, 10)}...
                                    </>
                                ) : (
                                    menu.explanation
                                )}
                            </p>
                            <div className="menu-buttons">
                                <button onClick={() => handleEdit(menu.menu_id)}>수정하기</button>
                                <button onClick={() => handleDelete(menu.menu_id)}>삭제하기</button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            {editMenu && (
                <div className="menu-modal">
                    <div className="menu-modal-content">
                        <span className="close" onClick={() => setEditMenu(null)}>&times;</span>
                        <input type="file" accept="image/*" onChange={handleImageEdit} />
                        {editMenuImagePreview && <img src={editMenuImagePreview} alt="미리보기" className="menu-image-preview" />}
                        <input type="text" placeholder="메뉴 이름" value={editMenu.menu_name} onChange={e => setEditMenu({ ...editMenu, menu_name: e.target.value })} />
                        <input type="text" placeholder="가격" value={editMenu.menu_price} onChange={e => setEditMenu({ ...editMenu, menu_price: e.target.value })} />
                        <input type="text" placeholder="메뉴 설명" value={editMenu.explanation} onChange={e => setEditMenu({ ...editMenu, explanation: e.target.value })} />
                        <button onClick={handleUpdate}>수정 완료</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Menu;

