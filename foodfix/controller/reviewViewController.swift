//
//  reviewViewController.swift
//  tm
//
//  Created by 노현민 on 2024/02/21.
//

import Foundation
import UIKit
import Alamofire

class reviewViewController : UIViewController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let decoder = JSONDecoder()
        var url = "http://54.180.213.178:8080/user/reservations"
        let token = read(key:"token")
        
        let header: HTTPHeaders = [
            "Authorization": token!
        ]

        
        let dataRequest = AF.request(url, method: .get,
                                     encoding: JSONEncoding.default,
                                     headers: header)
        
        
        
    }
    
    
}
