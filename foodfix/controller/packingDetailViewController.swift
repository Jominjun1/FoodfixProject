//
//  packingDetailViewController.swift
//  foodfix
//
//  Created by rhm on 2024/05/28.
//

import Foundation
import Alamofire


class packingDetailViewController: UIViewController{
    @IBOutlet weak var packingIdLabel: UILabel!
    @IBOutlet weak var packingDateLabel: UILabel!
    @IBOutlet weak var paymentTypeLabel: UILabel!
    @IBOutlet weak var packingStatusLabel: UILabel!
    @IBOutlet weak var menuNameLabel: UILabel!
    @IBOutlet weak var menuPriceLabel: UILabel!
    @IBOutlet weak var totalSumLabel: UILabel!
    @IBOutlet weak var userCommentLabel: UILabel!
    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var storePhoneLabel: UILabel!
    @IBOutlet weak var orderView: UIView!
    @IBOutlet weak var storeView: UIView!
    var orderData : PackingOrder?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        orderView.layer.cornerRadius = 10
        storeView.layer.cornerRadius = 10
        packingIdLabel.text = String((orderData?.packing_id)!)
        packingDateLabel.text = (orderData?.packing_date)! + " " + (orderData?.packing_time)!
        switch((orderData?.payment_type)!){
        case "0":
            paymentTypeLabel.text = "앱에서 결제"
        case "1":
            paymentTypeLabel.text = "방문해서 결제"
        default:
            paymentTypeLabel.text = "error"
        }
        switch((orderData?.packing_status)!){
        case "0":
            packingStatusLabel.text = "주문중"
        case "1":
            packingStatusLabel.text = "주문접수"
        case "2":
            packingStatusLabel.text = "주문취소"
        case "3":
            packingStatusLabel.text = "주문완료"
        default:
            packingStatusLabel.text = "error"
        }
        var totalsum = 0
        var menuName = ""
        var menuPrice = ""
        menuNameLabel.numberOfLines = (orderData?.menuItemDTOList)!.count
        menuPriceLabel.numberOfLines = (orderData?.menuItemDTOList)!.count
        for menu in (orderData?.menuItemDTOList)!{
            var currentMenuName = ""
            
            if(menuName != ""){
                currentMenuName = "\n" + menu.menu_name + " * " + String(menu.quantity) + "ea"
            }else{
                currentMenuName = menu.menu_name + " * " + String(menu.quantity) + "ea"
            }
            menuName += currentMenuName
            
            if(menuPrice != ""){
                menuPrice += "\n"
            }
            menuPrice += String(menu.totalPrice) + "원"
            
            totalsum += menu.totalPrice
        }
        menuNameLabel.text = menuName
        menuPriceLabel.text = menuPrice
        totalSumLabel.text = "합계금액: " + String(totalsum) + "원"
        userCommentLabel.text = (orderData?.user_comments)!
        storeNameLabel.text = (orderData?.store_name)!
        storePhoneLabel.text = (orderData?.store_phone)!
        
        
    }
    @IBAction func cancelOrderPacking(_ sender: Any) {
        var url = "http://54.180.213.178:8080/user/cancelp/" + String((orderData?.packing_id)!)
        print(url)
        let token = read(key:"token")
        
        let header: HTTPHeaders = [
            "Authorization": token!
        ]
        let dataRequest = AF.request(url, method: .delete,
                                     encoding: JSONEncoding.default,
                                     headers: header)
        
        dataRequest.responseData{ dataResponse in
            dump(dataResponse)
            switch dataResponse.result {
            case .success(let success):
                guard let statusCode = dataResponse.response?.statusCode else{return}
                if(statusCode == 200){//취소 성공
                    self.showToast(message: "포장 취소에 성공했습니다.")
                    let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                        self.performSegue(withIdentifier: "unwindToMain", sender: self)
                    }
                }else{//취소 실패
                    self.showToast(message: "포장 취소에 실패했습니다.")
                }
            case .failure(let failure):
                print("서버와의 연결에 실패했습니다.")
            }
        }
    }
    func showToast(message : String, font: UIFont = UIFont.systemFont(ofSize: 14.0)) {
        let toastLabel = UILabel(frame: CGRect(x: self.view.frame.size.width/2 - 150, y: self.view.frame.size.height-100, width: 300, height: 35))
        toastLabel.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        toastLabel.textColor = UIColor.white
        toastLabel.font = font
        toastLabel.textAlignment = .center;
        toastLabel.text = message
        toastLabel.alpha = 1.0
        toastLabel.layer.cornerRadius = 10;
        toastLabel.clipsToBounds  =  true
        self.view.addSubview(toastLabel)
        UIView.animate(withDuration: 4.0, delay: 0.1, options: .curveEaseOut, animations: {
            toastLabel.alpha = 0.0
        }, completion: {(isCompleted) in
            toastLabel.removeFromSuperview()
        })
    }
}
