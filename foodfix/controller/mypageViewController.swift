//
//  mypageViewController.swift
//  tm
//
//  Created by 노현민 on 2024/02/20.
//

import Foundation
import UIKit
import Alamofire



class mypageViewController : UIViewController{
    @IBAction func unwindToMypage(unwindsegue: UIStoryboardSegue){}
    @IBOutlet weak var userNicknameLabel: UILabel!
    
    @IBAction func logout(_ sender: Any) {
        
        let url = "http://54.180.213.178:8080/user/logout"
        let token = read(key:"token")
        
        let header: HTTPHeaders = [
            "Authorization": token!
        ]

        let dataRequest = AF.request(url, method: .post,
                                     encoding: JSONEncoding.default,
                                     headers: header)
        
        dataRequest.responseData{ dataResponse in
            switch dataResponse.result {
            case .success(let success):
                foodfix.delete(key: "token")
                
                let vcName = self.storyboard?.instantiateViewController(withIdentifier: "loginViewController")
                vcName?.modalPresentationStyle = .fullScreen
                vcName?.modalTransitionStyle = .crossDissolve
                self.present(vcName!, animated: true, completion: nil)
                
                
            case .failure(let failure):
                print("사용자 정보 불러오기를 실패하였습니다.")
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
                self.userNicknameLabel.text = decodedData.nickname
                
                
            case .failure(let failure):
                print("사용자 정보 불러오기를 실패하였습니다.")
            }
            
            
            
        }
        
    }
    
}
