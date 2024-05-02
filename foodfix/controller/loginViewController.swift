//
//  loginViewController.swift
//  foodfix
//
//  Created by rhm on 2024/02/28.
//

import Foundation
import UIKit
import Alamofire

func create(key: String, token: String){
    let query: NSDictionary = [
        kSecClass: kSecClassGenericPassword,
        kSecAttrAccount: key,
        kSecValueData: token.data(using: .utf8, allowLossyConversion: false) as Any
    ]
    SecItemDelete(query)
    
    let status = SecItemAdd(query,nil)
    assert(status == noErr, "failed to save token")
    
}
func read(key: String) -> String? {
    let query: NSDictionary = [
        kSecClass: kSecClassGenericPassword,
        kSecAttrAccount: key,
        kSecReturnData: kCFBooleanTrue as Any,  // CFData 타입으로 불러오라는 의미
        kSecMatchLimit: kSecMatchLimitOne       // 중복되는 경우, 하나의 값만 불러오라는 의미
    ]
    
    // READ
    var dataTypeRef: AnyObject?
    let status = SecItemCopyMatching(query, &dataTypeRef)
    
    if status == errSecSuccess {
        let retrievedData = dataTypeRef as! Data
        let value = String(data: retrievedData, encoding: String.Encoding.utf8)
        return value
    } else {
        print("failed to loading, status code = \(status)")
        return nil
    }
}
func delete(key: String) {
    let query: NSDictionary = [
        kSecClass: kSecClassGenericPassword,
        kSecAttrAccount: key
    ]
    let status = SecItemDelete(query)
    assert(status == noErr, "failed to delete the value, status code = \(status)")
}

struct loginResponse: Codable
{
    var token:String!
    var message:String!
}


class loginViewController : UIViewController{
    @IBAction func unwindToLogin(unwindsegue: UIStoryboardSegue){}
    
    
    let jsonDecoder = JSONDecoder()
    @IBOutlet weak var idTextField: UITextField!
    @IBOutlet weak var pwTextField: UITextField!
    
    
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
    
    func goMainView(){
        guard let viewcontroller = self.storyboard?.instantiateViewController(withIdentifier: "ViewController") else{ return }
        viewcontroller.modalPresentationStyle = .fullScreen
        viewcontroller.modalTransitionStyle = .coverVertical
        self.present(viewcontroller, animated: true)
    }
    
    @IBAction func didTabLoginButton(_ sender: Any) {
        
        guard let id = idTextField.text, !id.isEmpty else{return}
        guard let pw = pwTextField.text, !pw.isEmpty else{return}
        let decoder = JSONDecoder()
        
        let url = "http://54.180.213.178:8080/user/login"
        let params : Parameters = [
            "user_id":id,
            "user_pw":pw
        ]
        let headers: HTTPHeaders = [
            "Content-Type": "application/json"
        ]
        let dataRequest = AF.request(url,
                   method: .post,
                   parameters: params,
                   encoding: JSONEncoding.default,
                   headers: ["Content-Type":"application/json", "Accept":"application/json"])
        
        
        dataRequest.responseData{ dataResponse in
            switch dataResponse.result {
            case .success(let success):
                guard let decodedData = try? decoder.decode(loginResponse.self, from: dataResponse.value!) else { return }
                if(decodedData.token != nil){ //토큰이 존재함
                    print(decodedData.token!)
                    create(key: "token", token: decodedData.token)
                    self.showToast(message: "로그인 성공")
                    let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){ timer in
                        self.goMainView()
                        
                    }
                    
                }
                else{ //토큰이 존재하지 않음
                    self.showToast(message: "아이디나 비밀번호가 일치하지 않습니다")
                }
                
                
                
            case .failure(let failure):
                self.showToast(message: "로그인 실패")
            }
            
            
        }
        
        
    }
    
    
}
