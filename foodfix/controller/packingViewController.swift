//
//  packingViewController.swift
//  foodfix
//
//  Created by rhm on 2024/05/02.
//

import Foundation
import UIKit
import Alamofire

class packingViewController: UIViewController{
    
    
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
        
        dataRequest.responseData{ dataResponse in
            switch dataResponse.result {
            case .success(let success):
                guard let decodedData = try? decoder.decode([ReserveOrder].self, from: dataResponse.value!) else {
                    print("Json 해독 실패")
                    return }
                //self.orderArray = decodedData
                
            case .failure(let failure):
                print("예약 내역을 불러오는데 실패하였습니다.")
            }
        }
    }
}
