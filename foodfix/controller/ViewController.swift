//
//  ViewController.swift
//  foodfix
//
//  Created by rhm on 2024/02/28.
//

import UIKit
import Alamofire
import Kingfisher


struct Store :Codable{
    var store_id :Int
    var store_name :String
    var imagePath :String
    var store_category :String
    var openTime :String
    var closeTime :String

}

class StoreTableViewCell: UITableViewCell{
    @IBOutlet weak var thumbnailImageView: UIImageView!
    @IBOutlet weak var titleLabel:UILabel!
    @IBOutlet weak var timeLabel: UILabel!
}
extension ViewController: UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return storeArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: StoreTableViewCell = tableView.dequeueReusableCell(withIdentifier: "storeCell", for: indexPath) as! StoreTableViewCell
        
        //cell.thumbnailImageView.image = UIImage(named: "") //수정할것
        cell.titleLabel.text = storeArray[indexPath.row].store_name
        let url = URL(string: "http://54.180.213.178:8080/images"+storeArray[indexPath.row].imagePath)
        print(url)
        cell.thumbnailImageView.kf.indicatorType = .activity
        cell.thumbnailImageView.kf.setImage(with: url!, placeholder: nil, options: [.transition(.fade(0.7))], progressBlock: nil)
        let time = "open: "+storeArray[indexPath.row].openTime + " \nclose: " + storeArray[indexPath.row].closeTime
        cell.timeLabel.text = time
        
        return cell
        
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "goDetail", sender: storeArray[indexPath.row])
        
        
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "goDetail"){
            let vc = segue.destination as! storeDetailViewController
            guard let data = sender as? Store else{
                print("prepare 실패")
                return
            }
            vc.storeDetail = data
        }
    }
    
}

class ViewController: UIViewController, UITableViewDelegate{
    @IBAction func unwindToMain(unwindsegue: UIStoryboardSegue){}
    
    var searchMod = "포장"
    var category = "한식"
    var storeArray: [Store] = []
    
    var url = "http://54.180.213.178:8080/store"
    let decoder = JSONDecoder()
    
    
    @IBOutlet weak var tableView: UITableView!
    
    
    func doSearch(searchMod:String, category:String){
        switch(searchMod){
        case "포장":
            url = "http://54.180.213.178:8080/store/packable"
        case "예약":
            url = "http://54.180.213.178:8080/store/reservable"
        default:
            print("유효하지 않은 검색모드")
            return
        }
        switch(category){
        case "한식":
            url = url+"?store_category=한식"
        case "양식":
            url = url+"?store_category=양식"
        case "치킨":
            url = url+"?store_category=치킨"
        default:
            print("유효하지 않은 카테고리")
            return
        }

        let dataRequest = AF.request(url,
                   method: .get,
                   headers: ["Accept":"application/json"])
        dataRequest.responseData{ dataResponse in
            switch(dataResponse.result){
            case .success(_):
                guard let decodedData = try? self.decoder.decode([Store].self, from: dataResponse.value!) else {
                    print("json해독실패")
                    return }
                self.storeArray = decodedData
                
            case .failure(_):
                print("검색에 실패하였습니다.")
                
            }
            
            
        }
        let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){_ in
            self.tableView.reloadData()
        }
        
    }

    
    
    
    @IBAction func selectTakeout(_ sender: Any) {
        searchMod = "포장"
        doSearch(searchMod: searchMod, category: category)
    }
    @IBAction func selectReserve(_ sender: Any) {
        searchMod = "예약"
        doSearch(searchMod: searchMod, category: category)
    }
    @IBAction func selectKoreanfood(_ sender: Any) {
        category = "한식"
        doSearch(searchMod: searchMod, category: category)
    }
    @IBAction func selectWesternfood(_ sender: Any) {
        category = "양식"
        doSearch(searchMod: searchMod, category: category)
    }
    @IBAction func selectChicken(_ sender: Any) {
        category = "치킨"
        doSearch(searchMod: searchMod, category: category)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        let store = Store(store_id: 1000, store_name: "test", imagePath: "testurl", store_category: "양식", openTime: "11:00:00", closeTime: "22:00:00")
        /*storeArray.append(store)
        storeArray.append(store)
        storeArray.append(store)
        storeArray.append(store)*/

        searchMod = "포장"
        category = "양식"

        doSearch(searchMod: searchMod, category: category)
        self.tableView.rowHeight = 130
        self.tableView.delegate = self
        self.tableView.dataSource = self
        let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){_ in
            self.tableView.reloadData()
        }
        
    }


}

