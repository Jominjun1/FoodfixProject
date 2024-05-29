//
//  reservationViewController.swift
//  tm
//
//  Created by 노현민 on 2024/02/22.
//

import Foundation
import UIKit
import Alamofire



class reserveOrderCell: UITableViewCell{
    @IBOutlet weak var storeName: UILabel!
    @IBOutlet weak var orderDate: UILabel!
    @IBOutlet weak var numPeople: UILabel!
    
}
extension reservationViewController : UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return orderArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: reserveOrderCell = reserveOrderTable.dequeueReusableCell(
            withIdentifier: "reserveOrderCell", for: indexPath
        ) as! reserveOrderCell
        cell.storeName.text = orderArray[indexPath.row].store_name
        cell.orderDate.text = "예약일시: " + orderArray[indexPath.row].reservation_date + " " + orderArray[indexPath.row].reservation_time
        cell.numPeople.text = "예약인원: " + String(orderArray[indexPath.row].num_people) + "명"
        cell.layer.cornerRadius = 10
        return cell
        
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "goDetailReserve", sender: orderArray[indexPath.row])
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "goDetailReserve"){
            let vc = segue.destination as! reservationDetailViewController
            guard let data = sender as? ReserveOrder else{
                print("prepare 실패")
                return
            }
            vc.orderData = data
        }
    }
    
}

class reservationViewController : UIViewController, UITableViewDelegate{
    @IBAction func unwindToReservation(unwindsegue: UIStoryboardSegue){}
    
    @IBOutlet weak var reserveOrderTable: UITableView!
    var orderArray :[ReserveOrder] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        reserveOrderTable.delegate = self
        reserveOrderTable.dataSource = self
        self.reserveOrderTable.rowHeight = 130
        
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
                self.orderArray = decodedData.reversed()
                print(decodedData)
                let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){_ in
                    self.reserveOrderTable.reloadData()
                }
            case .failure(let failure):
                print("예약 내역을 불러오는데 실패하였습니다.")
            }
        }
        
        
        
        
    }
}
