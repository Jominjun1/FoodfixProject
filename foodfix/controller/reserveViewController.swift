//
//  reserveViewController.swift
//  foodfix
//
//  Created by rhm on 2024/04/13.
//

import Foundation
import Alamofire
import Starscream



extension Date{
    func toString() -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm"
        dateFormatter.timeZone = TimeZone(identifier: "GMT")
        return dateFormatter.string(from: self)
    }
}


class reserveViewController : UIViewController, WebSocketDelegate{
    
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
            print("서버와 연결되었습니다.")
            client.write(string: (userData?.user_id)!)
            print((userData?.user_id)!)
        case .disconnected(_, _):
            print("서버와 연결이 해제되었습니다.")
        case .text(let string):
            print(string)
        case .binary(_):
            print("binary")
        case .pong(_):
            print("pong")
        case .ping(_):
            print("ping")
        case .error(_):
            print("error")
        case .viabilityChanged(_):
            print("viabilityChanged")
        case .reconnectSuggested(_):
            print("reconnectSuggested")
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
    
    var userData : User?
    var storeData : Store?
    var today = Date()
    let timezone = TimeZone.autoupdatingCurrent
    
    @IBOutlet weak var datePicker: UIDatePicker!
    @IBOutlet weak var commentTextField: UITextField!
    @IBOutlet weak var peopleCntStepper: UIStepper!
    @IBOutlet weak var peopleCntLabel: UILabel!
    
    @IBOutlet weak var reserveButton: UIButton!
    @IBOutlet weak var timeView: UIView!
    
    @IBOutlet weak var countView: UIView!
    
    @IBOutlet weak var commentView: UIView!
    
    @IBAction func stepperValueChanged(_ sender: Any) {
        let stepperNum = Int(peopleCntStepper.value)
        peopleCntLabel.text = "예약 인원수 : " + String(stepperNum) + "명"
        
    }
    
    @IBAction func doReserve(_ sender: Any) {
        
        let selected = datePicker.date.addingTimeInterval(TimeInterval(timezone.secondsFromGMT(for: today)))
        let selectedDate = selected.toString().split(separator: " ")
        
        let url = "http://54.180.213.178:8080/order/reservation"
        
        let params: Parameters = [
            "user_id": (userData?.user_id)!,
            "user_phone": (userData?.user_phone)!,
            "user_comments": commentTextField.text!,
            "reservation_date": String(selectedDate[0]),
            "reservation_time": String(selectedDate[1]),
            "num_people": Int(peopleCntStepper.value),
            "store_id": (storeData?.store_id)!
        ]
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
                guard let value = DataResponse.value else{return}
                print(statusCode)
                print(value)
                if(statusCode == 500){
                    self.showToast(message: "http 500 error")
                    return
                } else if(statusCode == 400){
                    self.showToast(message: "http 400 ")
                    return
                }
                self.showToast(message: "예약주문 성공")
                let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                    self.disconnect()
                    self.performSegue(withIdentifier: "unwindToStore", sender: self)
                    
                }
                
            case .failure:
                print("fail")
                self.showToast(message: "예약 실패")
                return
                
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        datePicker.minimumDate = Date() + 7200 //현재 시간보다 7200초 이후, 2시간 뒤부터만 선택 가능하게
        peopleCntLabel.text = "예약 인원수 : " + "1" + "명"
        timeView.layer.cornerRadius = 10
        timeView.layer.masksToBounds = true
        countView.layer.cornerRadius = 10
        countView.layer.masksToBounds = true
        commentView.layer.cornerRadius = 10
        commentView.layer.masksToBounds = true
        reserveButton.layer.cornerRadius = 10
        
        let decoder = JSONDecoder()
        
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
                guard let decodedData = try? decoder.decode(User.self, from: dataResponse.value!) else { return }
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
