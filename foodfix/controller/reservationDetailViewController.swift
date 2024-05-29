//
//  reservationDetailViewController.swift
//  foodfix
//
//  Created by rhm on 2024/04/20.
//

import Foundation
import Alamofire


class reservationDetailViewController : UIViewController{
    var orderData : ReserveOrder?
    
    @IBOutlet weak var reserveIdLabel: UILabel!
    @IBOutlet weak var reserveTimeLabel: UILabel!
    @IBOutlet weak var numPeopleLabel: UILabel!
    @IBOutlet weak var userCommentLabel: UILabel!
    @IBOutlet weak var reservationStatusLabel: UILabel!
    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var storePhoneLabel: UILabel!
    @IBOutlet weak var orderView: UIView!
    @IBOutlet weak var commentView: UIView!
    @IBOutlet weak var storeView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        orderView.layer.cornerRadius = 10
        commentView.layer.cornerRadius = 10
        storeView.layer.cornerRadius = 10
        reserveIdLabel.text = String((orderData?.reservation_id)!)
        reserveTimeLabel.text = (orderData?.reservation_date)! + " " + (orderData?.reservation_time)!
        numPeopleLabel.text = String((orderData?.num_people)!) + "명"
        userCommentLabel.text = (orderData?.user_comments)!
        storeNameLabel.text = (orderData?.store_name)
        storePhoneLabel.text = (orderData?.store_phone)
        switch((orderData?.reservation_status)!){
        case "0":
            reservationStatusLabel.text = "예약대기"
        case "1":
            reservationStatusLabel.text = "예약성공"
        case "2":
            reservationStatusLabel.text = "예약취소"
        case "3":
            reservationStatusLabel.text = "예약완료"
        default:
            reservationStatusLabel.text = "error"
        }
    }
    
    @IBAction func cancelReserve(_ sender: Any) {
        var url = "http://54.180.213.178:8080/user/cancelr/" + String((orderData?.reservation_id)!)
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
                    self.showToast(message: "예약 취소에 성공했습니다.")
                    let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                        self.performSegue(withIdentifier: "unwindToMain", sender: self)
                    }
                }else{//취소 실패
                    self.showToast(message: "예약 취소에 실패했습니다.")
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
