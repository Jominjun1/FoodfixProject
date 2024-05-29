//
//  cartViewController.swift
//  foodfix
//
//  Created by rhm on 2024/04/22.
//

import Foundation
import UIKit
import Alamofire
import Starscream

class CartTableViewCell : UITableViewCell{
    

    @IBOutlet weak var sumPriceLabel: UILabel!
    @IBOutlet weak var menuQuantityLabel: UILabel!
    @IBOutlet weak var menunameLabel: UILabel!
    @IBOutlet weak var buttonDelete: UIButton!
}

extension cartViewController : UITableViewDataSource, UITableViewDelegate{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return cartArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: CartTableViewCell = tableView.dequeueReusableCell(withIdentifier: "cartCell", for: indexPath) as! CartTableViewCell
        
        cell.menunameLabel.text = cartArray[indexPath.row].menu_name
        cell.menuQuantityLabel.text = String(cartArray[indexPath.row].quantity)+"개"
        cell.sumPriceLabel.text = String(cartArray[indexPath.row].menu_price*cartArray[indexPath.row].quantity) + "원"
        cell.buttonDelete.tag = indexPath.row

        return cell
    }
    func tableView(_ tableView:UITableView, editingStyleForRowAt indexPath: IndexPath) -> UITableViewCell.EditingStyle{
        return .delete
    }
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath){// 장바구니 삭제 동작
        tableView.beginUpdates()
        cartArray.remove(at: indexPath.row) //배열에서 메뉴 삭제
        if let encoded = try? encoder.encode(cartArray){
            UserDefaults.standard.setValue(encoded, forKey: "cartArray")
        } //userdefaults에 삭제된 배열로 갱신하기
        tableView.deleteRows(at: [indexPath], with: .fade) //테이블에서 해당 셀 삭제
        tableView.endUpdates()
        calcSum()
    }
    func tableView(_ tableView: UITableView, titleForDeleteConfirmationButtonForRowAt indexPath: IndexPath) -> String? {
        return "삭제"
    }
    
}

class cartViewController : UIViewController, WebSocketDelegate{
    let decoder = JSONDecoder()
    let encoder = JSONEncoder()
    var userData : User?
    var storeData: Store?
    var cartArray: [MenuItemDTO] = []
    var today = Date()
    let timezone = TimeZone.autoupdatingCurrent
    
    @IBOutlet weak var cartTableView: UITableView!
    @IBOutlet weak var totalSumLabel: UILabel!
    @IBOutlet weak var commentTextField: UITextField!
    
    private var socket: WebSocket?
    private var socketUrl = URL(string: "ws://54.180.213.178:8080/wsk")
    
    func connect(){
        let request = URLRequest(url: socketUrl!)
        self.socket = WebSocket(request: request)
        self.socket?.delegate = self
        self.socket?.connect()
        
    }
    func disconnect(){
        self.socket?.disconnect()
    }
    func didReceive(event: Starscream.WebSocketEvent, client: Starscream.WebSocketClient) {
        switch event{
        case .connected(_):
            client.write(string: (userData?.user_id)!)
            print((userData?.user_id)!)
            print("서버와 연결되었습니다.")
        case .disconnected(_, _):
            print("서버와 연결이 해제되었습니다.")
        case .text(let string):
            print(string)
        case .binary(let data):
            print("binary")
            print(data)
        case .pong(let data):
            print("pong")
            print(data)
        case .ping(let data):
            print("ping")
            print(data)
        case .error(let error):
            print("error")
            print(error)
        case .viabilityChanged(let bool):
            print("viabilityChanged")
            print(bool)
        case .reconnectSuggested(let bool):
            print("reconnectSuggested")
            print(bool)
        case .cancelled:
            print("cancelled")
        case .peerClosed:
            print("peerClosed")
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
    func calcSum(){
        var sum = 0
        for cart in cartArray{
            sum += cart.menu_price*cart.quantity
        }
        totalSumLabel.text = "합계금액: "+String(sum)+"원"
    }
    func sendOrder(paymentType:String){
        let url = "http://54.180.213.178:8080/order/packing"
        let selected = today.addingTimeInterval(TimeInterval(timezone.secondsFromGMT(for: today)))
        
        let selectedDate = selected.toString().split(separator: " ")
        var menuItemDTOList:Array = [Dictionary<String, Any>]()
        
        for menuItem in cartArray{
            var dict:[String:Any] = [
                "menu_id":menuItem.menu_id,
                "menu_price":menuItem.menu_price,
                "menu_name":menuItem.menu_name,
                "quantity":menuItem.quantity
            ]
            menuItemDTOList.append(dict)
        }
        
        let params: Parameters = [
            "user_id":(userData?.user_id)!,
            "user_phone":(userData?.user_phone)!,
            "user_comments":commentTextField.text!,
            "packing_date": String(selectedDate[0]),
            "packing_time": String(selectedDate[1]),
            "payment_type": paymentType, //0:앱결제 1:방문결제
            "store_id": (storeData?.store_id)!,
            "menuItemDTOList": menuItemDTOList
        ]
        print(params)
        let header: HTTPHeaders = [
            "Content-Type": "application/json"
        ]
        
        let dataRequest = AF.request(url, method: .post,
                   parameters: params,
                   encoding: JSONEncoding.default,
                   headers: header)
        
        dataRequest.responseData{ DataResponse in
            
            dump(DataResponse)
            
            switch DataResponse.result{
            case .success:
                guard let statusCode = DataResponse.response?.statusCode else{return}
                if(statusCode == 500){
                    self.showToast(message: "http 500 error")
                    return
                } else if(statusCode == 400){
                    self.showToast(message: "http 400 ")
                    return
                }else if(statusCode == 200){
                    self.showToast(message: "포장 주문에 성공하였습니다.")
                    UserDefaults.standard.removeObject(forKey: "cartArray")
                    let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                        self.performSegue(withIdentifier: "unwindToMain", sender: self)
                    }
                    
                }
                
                
            case .failure:
                self.showToast(message: "포장주문 실패")
                return
                
            }
        }
    }
    
    @IBAction func doOrderPacking(_ sender: Any) {
        sendOrder(paymentType: "0")
    }
    @IBAction func doOrderPackingVisit(_ sender: Any) {
        sendOrder(paymentType: "1")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let savedData = UserDefaults.standard.object(forKey: "cartArray")as? Data{
            if let savedObject = try? decoder.decode([MenuItemDTO].self, from: savedData){
                cartArray = savedObject
            }
        }// userDefaults에서 기존 장바구니 배열 가져오기
        
        cartTableView.delegate = self
        cartTableView.dataSource = self
        calcSum()
        
        let url = "http://54.180.213.178:8080/user/profile"
        let token = read(key:"token")
        let header: HTTPHeaders = [
            "Authorization": token!
        ]
        let dataRequest = AF.request(url, method: .get,
                                     encoding: JSONEncoding.default,
                                     headers: header)
        dataRequest.responseData{ dataResponse in
            switch dataResponse.result {
            case .success(let success):
                guard let decodedData = try? self.decoder.decode(User.self, from: dataResponse.value!) else { return }
                self.userData = decodedData
                self.socketUrl = URL(string: "ws://54.180.213.178:8080/wsk"+"?user_id="+String((self.userData?.user_id)!))
                print(self.socketUrl)
                self.connect()
            case .failure(let failure):
                print("사용자 정보 불러오기를 실패하였습니다.")
            }
        }
        
        
    }
}
