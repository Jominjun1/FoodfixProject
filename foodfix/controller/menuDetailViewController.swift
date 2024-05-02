//
//  menuDetailViewController.swift
//  foodfix
//
//  Created by rhm on 2024/04/07.
//

import Foundation
import Alamofire


struct MenuItemDTO: Codable{
    var menu_id : Int
    var menu_price : Int
    var menu_name : String
    var quantity : Int
}

class menuDetailViewController : UIViewController{
    var menuDetail: Menu?
    let encoder = JSONEncoder()
    let decoder = JSONDecoder()
    
    @IBOutlet weak var menuNameLabel: UILabel!
    @IBOutlet weak var sumPriceLabel: UILabel!
    @IBOutlet weak var menuQuantityLabel: UILabel!
    @IBOutlet weak var menuQuantityStepper: UIStepper!
    @IBOutlet weak var explanationLabel: UILabel!
    
    @IBAction func stepperValueChanged(_ sender: Any) {
        let stepperNum = Int(menuQuantityStepper.value)
        let sum = stepperNum * (menuDetail?.menu_price)!
        menuQuantityLabel.text = String(stepperNum)+"개"
        sumPriceLabel.text = String(sum)+"원"
    }
    
    @IBAction func addOrder(_ sender: Any) {
        
        var cartArray :[MenuItemDTO] = [] //장바구니
        if let savedData = UserDefaults.standard.object(forKey: "cartArray")as? Data{
            if let savedObject = try? decoder.decode([MenuItemDTO].self, from: savedData){
                cartArray = savedObject
                print(cartArray)
            }
        }// userDefaults에서 기존 장바구니 배열 가져오기
            
        var menuItem = MenuItemDTO(menu_id: (menuDetail?.menu_id)!, menu_price: (menuDetail?.menu_price)!, menu_name: (menuDetail?.menu_name)!, quantity: Int(menuQuantityStepper.value))// 선택된 메뉴
        cartArray.append(menuItem)//장바구니에 선택된 메뉴 추가
        if let encoded = try? encoder.encode(cartArray){
            UserDefaults.standard.setValue(encoded, forKey: "cartArray")
        }//userDefaults에 장바구니 집어넣기
        
        self.performSegue(withIdentifier: "unwindToStore", sender: self)//매장정보로 돌아가기
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        menuNameLabel.text = menuDetail?.menu_name
        explanationLabel.text = menuDetail?.explanation
        menuQuantityLabel.text = String(Int(menuQuantityStepper.value))+"개"
        var sumPrice = Int(menuQuantityStepper.value) * (menuDetail?.menu_price)!
        sumPriceLabel.text = String(sumPrice)+"원"
        
    }
    
}
