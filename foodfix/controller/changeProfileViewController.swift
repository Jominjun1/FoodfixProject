//
//  changeProfileViewController.swift
//  tm
//
//  Created by 노현민 on 2024/02/20.
//

import Foundation
import UIKit
import Alamofire

class changeProfileViewController : UIViewController{
    
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
    
    @IBOutlet weak var nicknameTextField: UITextField!
    
    @IBOutlet weak var phoneTextField: UITextField!
    
    @IBOutlet weak var addressTextField: UITextField!
    
    
    
    @IBAction func saveProfile(_ sender: Any) {
        let url = "http://54.180.213.178:8080/user/update"
        let token = read(key:"token")
        let header: HTTPHeaders = [
            "Authorization": token!
        ]
        let params : Parameters = [
            "user_address": addressTextField.text,
            "user_phone": phoneTextField.text,
            "nickname": nicknameTextField.text
        ]
        let dataRequest = AF.request(url, method: .put,
                                     parameters: params,
                                     encoding: JSONEncoding.default,
                                     headers: header)
        dataRequest.responseData{ dataResponse in
            switch dataResponse.result {
            case .success(let success):
                
                self.showToast(message: "회원정보 수정 성공")
                let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                    
                    
                    self.performSegue(withIdentifier: "unwindToMypage", sender: self)
                }
                
                
                
                
                
            case .failure(let failure):
                self.showToast(message: "회원정보 수정 실패")
            }
        }
        
    }
    
    
    
    @IBAction func deleteUser(_ sender: Any) {
        
        let url = "http://54.180.213.178:8080/user/delete"
        let token = read(key:"token")
        let header: HTTPHeaders = [
            "Authorization": token!
        ]
        let dataRequest = AF.request(url, method: .delete,
                                     encoding: JSONEncoding.default,
                                     headers: header)
        
        dataRequest.responseData{ dataResponse in
            switch dataResponse.result {
            case .success(let success):
                
                self.showToast(message: "회원 탈퇴 성공")
                
                let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                    
                    foodfix.delete(key: "token")
                    self.performSegue(withIdentifier: "backToLogin", sender: self)
                    
                }
                
                
                
                
                
            case .failure(let failure):
                self.showToast(message: "회원 탈퇴 실패")
            }
        }
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
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
                self.nicknameTextField.text = decodedData.nickname
                self.phoneTextField.text = decodedData.user_phone
                self.addressTextField.text = decodedData.user_address
                
                
            case .failure(let failure):
                print("사용자 정보 불러오기를 실패하였습니다.")
            }
        }
        
        
        
    }
}
