//
//  changePwViewController.swift
//  foodfix
//
//  Created by rhm on 2024/03/24.
//

import Foundation
import UIKit
import Alamofire


class changePwViewController : UIViewController{
    func showToast(message : String, font: UIFont = UIFont.systemFont(ofSize: 14.0)) {
            let toastLabel = UILabel(frame: CGRect(x: self.view.frame.size.width/2 - 75, y: self.view.frame.size.height-100, width: 300, height: 35))
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
    
    
    @IBOutlet weak var currentPwTextField: UITextField!
    
    @IBOutlet weak var changePwTextField: UITextField!
    
    @IBOutlet weak var checkPwTextField: UITextField!
    
    @IBAction func changePassword(_ sender: Any) {
        guard let currentPw = currentPwTextField.text, !currentPw.isEmpty else{
            showToast(message: "모든 항목을 입력하세요.")
            return}
        guard let changePw = changePwTextField.text, !changePw.isEmpty else{
            showToast(message: "모든 항목을 입력하세요.")
            return}
        guard let checkPw = checkPwTextField.text, !checkPw.isEmpty else{
            showToast(message: "모든 항목을 입력하세요.")
            return}
        print(currentPw + changePw + checkPw)
        
        if(changePw != checkPw){
            showToast(message: "변경할 비밀번호가 일치하지 않습니다.")
            return
        }
        
        let decoder = JSONDecoder()
        
        var url = "http://54.180.213.178:8080/user/profile"
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
                if(self.currentPwTextField.text == decodedData.user_pw){
                    url = "http://54.180.213.178:8080/user/update"
                    let params : Parameters = [
                        "user_pw": self.changePwTextField.text
                    ]
                    let dataRequest2 = AF.request(url, method: .put,
                                                  parameters: params,
                                                  encoding: JSONEncoding.default,
                                                  headers: header)
                    dataRequest2.responseData{ dataResponse in
                        switch dataResponse.result{
                        case .success(let success):
                            
                            self.showToast(message: "비밀번호 변경에 성공하였습니다.")
                            let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                                
                                
                                let vcName = self.storyboard?.instantiateViewController(withIdentifier: "mypageViewController")
                                vcName?.modalPresentationStyle = .fullScreen
                                vcName?.modalTransitionStyle = .crossDissolve
                                self.present(vcName!, animated: true, completion: nil)
                            }
                            
                            
                            
                            
                        case .failure(let failure):
                            self.showToast(message: "비밀번호 변경이 실패하였습니다.")
                            return
                        }
                        
                    }
                    
                    
                }
                else{
                    self.showToast(message: "현재 비밀번호가 일치하지 않습니다.")
                    return
                }
                
                
            case .failure(let failure):
                print("사용자 정보 불러오기를 실패하였습니다.")
            }
        }
        
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        
        
        
        
    }
    
}
