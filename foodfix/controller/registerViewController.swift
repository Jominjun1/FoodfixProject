//
//  registerViewController.swift
//  foodfix
//
//  Created by rhm on 2024/02/29.
//

import Foundation
import UIKit
import Alamofire





class registerViewController : UIViewController{
    
    func shakeTextField(textField: UITextField) -> Void{
        UIView.animate(withDuration: 0.2, animations: {
            textField.frame.origin.x -= 10
        }, completion: { _ in
            UIView.animate(withDuration: 0.2, animations: {
                textField.frame.origin.x += 20
            }, completion: { _ in
                UIView.animate(withDuration: 0.2, animations: {
                    textField.frame.origin.x -= 10
                })
            })
        })
    }
    
    func showToast(message : String, font: UIFont = UIFont.systemFont(ofSize: 14.0)) {
            let toastLabel = UILabel(frame: CGRect(x: self.view.frame.size.width/2 - 75, y: self.view.frame.size.height-100, width: 150, height: 35))
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
    
    @IBOutlet weak var idTextField: UITextField!
    @IBOutlet weak var pwTextField: UITextField!
    @IBOutlet weak var pw2TextField: UITextField!
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var nicknameTextField: UITextField!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var addressTextField: UITextField!
    @IBOutlet weak var phoneTextField: UITextField!

    @IBOutlet weak var genderSegment: UISegmentedControl!
    
    
    
    
    
    func emptyAlert(){
        showToast(message: "모든 항목을 입력하세요.")
    }
    

    @IBAction func doneRegister(_ sender: Any) {
        guard let user_id = idTextField.text, !user_id.isEmpty else{emptyAlert();return}
        guard let user_pw = pwTextField.text, !user_pw.isEmpty else{emptyAlert();return}
        guard let pw_repeat = pw2TextField.text, !pw_repeat.isEmpty else{emptyAlert();return}
        if(user_pw != pw_repeat){
            shakeTextField(textField: pwTextField)
            shakeTextField(textField: pw2TextField)
            showToast(message: "비밀번호가 일치하지 않습니다")
            return
        }
        guard let user_phone = phoneTextField.text, !user_phone.isEmpty else{emptyAlert();return}
        guard let user_name = nameTextField.text, !user_name.isEmpty else{emptyAlert();return}
        guard let user_address = addressTextField.text, !user_address.isEmpty else{emptyAlert();return}
        guard let nickname = nicknameTextField.text, !nickname.isEmpty else{emptyAlert();return}
        var gender = "0"
        switch genderSegment.selectedSegmentIndex{
        case 0:
            gender = "0"
        case 1:
            gender = "1"
        default: return
            
        }
        
        
        
        let url = "http://54.180.213.178:8080/user/signup"
        let params: Parameters = [
            "user_id":user_id,
            "user_phone":user_phone,
            "user_name":user_name,
            "user_pw":user_pw,
            "user_address":user_address,
            "nickname":nickname,
            "gender":gender
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
                    self.showToast(message: "중복된 id입니다.")
                    return
                }
                self.showToast(message: "회원가입 성공")
                let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                    self.performSegue(withIdentifier: "unwindToMain", sender: self)
                    
                }
                
            case .failure:
                print("fail")
                self.showToast(message: "회원가입 실패")
                return
                
            }
        }
    }
    
    
    
    
    
}
