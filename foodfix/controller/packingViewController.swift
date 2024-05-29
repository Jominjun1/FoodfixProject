//
//  packingViewController.swift
//  foodfix
//
//  Created by rhm on 2024/05/02.
//

import Foundation
import UIKit
import Alamofire

class PackingOrderTableCell :UITableViewCell{
    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var menuLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var packingDateLabel: UILabel!
    @IBOutlet weak var packingTimeLabel: UILabel!
}
extension packingViewController: UITableViewDataSource, UITableViewDelegate{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return packingOrderArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: PackingOrderTableCell = tableView.dequeueReusableCell(withIdentifier: "packingOrderCell", for: indexPath) as! PackingOrderTableCell
        
        cell.storeNameLabel.text = packingOrderArray[indexPath.row].store_name
        var sumPrice = 0
        for menu in packingOrderArray[indexPath.row].menuItemDTOList{
            sumPrice += menu.totalPrice
        }
        cell.priceLabel.text = "합계 금액: " + String(sumPrice) + "원"
        
        cell.menuLabel.text = packingOrderArray[indexPath.row].menuItemDTOList[0].menu_name + " 외 " +  String(packingOrderArray[indexPath.row].menuItemDTOList.count-1) + "건"
        cell.packingDateLabel.text = packingOrderArray[indexPath.row].packing_date
        cell.packingTimeLabel.text = packingOrderArray[indexPath.row].packing_time
        cell.layer.cornerRadius = 10
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "goDetailPacking", sender: packingOrderArray[indexPath.row])
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "goDetailPacking"){
            let vc = segue.destination as! packingDetailViewController
            guard let data = sender as? PackingOrder else{
                print("prepare 실패")
                return
            }
            vc.orderData = data
        }
    }
    
    
}
class packingViewController: UIViewController{
    @IBAction func unwindToPacking(unwindsegue: UIStoryboardSegue){}
    var packingOrderArray: [PackingOrder] = []
    
    @IBOutlet weak var packingOrderTable: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        packingOrderTable.delegate = self
        packingOrderTable.dataSource = self
        packingOrderTable.rowHeight = 130
        
        let decoder = JSONDecoder()
        var url = "http://54.180.213.178:8080/user/packings"
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
                guard let decodedData = try? decoder.decode([PackingOrder].self, from: dataResponse.value!) else {
                    print("Json 해독 실패")
                    return }
                self.packingOrderArray = decodedData.reversed() //최근순으로 출력하기 위해 순서 반전
                print(self.packingOrderArray)
                let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){_ in
                    self.packingOrderTable.reloadData()
                }
                
            case .failure(let failure):
                print("포장 주문 내역을 불러오는데 실패하였습니다.")
            }
        }
        
    }
}
