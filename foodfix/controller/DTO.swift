//
//  DTO.swift
//  foodfix
//
//  Created by rhm on 2024/05/07.
//

import Foundation

class User: Codable{
    var user_id : String!
    var user_phone : String!
    var user_name : String!
    var user_pw : String!
    var user_address : String!
    var nickname : String!
    var gender : String!
    
}

struct Store :Codable{
    var store_id :Int
    var store_address: String
    var store_phone: String
    var store_intro: String
    var imagePath :String
    var store_name :String
    var store_category :String
    var openTime :String
    var closeTime :String
}


struct Menu :Codable{
    var menu_id : Int
    var menu_name : String
    var explanation : String
    var menu_price : Int
    var imagePath : String
}

struct MenuItemDTO: Codable{
    var menu_id : Int
    var menu_price : Int
    var menu_name : String
    var quantity : Int
}

struct ReserveOrder :Codable{
    var user_id :String
    var user_phone :String
    var user_comments :String
    var reservation_date :String
    var reservation_time :String
    var num_people :Int
    var store_phone :String
    var store_name :String
    var store_id :Int
    var reservation_id :Int
    var reservation_status :String //0: 예약대기 1: 예약성공 2: 예약취소 3:예약완료
}

struct PackingOrder : Codable{
    var user_id: String
    var user_phone: String
    var user_comments: String
    var packing_date: String
    var packing_time: String
    var payment_type: String //0: 앱결제 1: 방문결제
    var store_phone: String
    var store_name: String
    var store_id: Int
    var minimumTime: Int?
    var menuItemDTOList : [Menus]
    var packing_id: Int
    var packing_status: String //0: 주문중 1: 주문접수 2: 주문취소 3: 주문완료
}
struct StorePacking: Codable{
    var store_id: Int
    var store_name: String
    var store_address: String
    var store_category: String
    var store_phone: String
    var res_status: String
    var store_intro: String
    var minimumTime: Int
    var res_max: Int
    var openTime: String
    var closeTime: String
    var reservationCancel: String
    
}

struct Menus: Codable{
    var menu_id : Int
    var menu_price : Int
    var totalPrice: Int
    var menu_name : String
    var quantity : Int
}
