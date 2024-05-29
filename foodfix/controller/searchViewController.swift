//
//  searchViewController.swift
//  foodfix
//
//  Created by rhm on 2024/05/28.
//

import Foundation
import Alamofire

class SearchTableViewCell :UITableViewCell{
    @IBOutlet weak var thumbnailImageView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var timeLabel: UILabel!
}

extension searchViewController: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return storeArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: SearchTableViewCell = tableView.dequeueReusableCell(withIdentifier: "storeCell", for: indexPath) as! SearchTableViewCell
        
        cell.titleLabel.text = storeArray[indexPath.row].store_name
        var imagePath = storeArray[indexPath.row].imagePath.split(separator: "/")
        let url = URL(string: "http://54.180.213.178:8080/images/" + imagePath.popLast()!)
        cell.thumbnailImageView.kf.indicatorType = .activity
        cell.thumbnailImageView.kf.setImage(with: url!, placeholder: nil, options: [.transition(.fade(0.7))], progressBlock: nil)
        let time = "open: "+storeArray[indexPath.row].openTime + " \nclose: " + storeArray[indexPath.row].closeTime
        cell.timeLabel.text = time
        cell.layer.cornerRadius = 10
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


class searchViewController: UIViewController{
    @IBOutlet weak var searchBar: UITextField!
    @IBOutlet weak var searchMod: UISegmentedControl! //0: 매장이름 1:메뉴이름
    @IBOutlet weak var orderMod: UISegmentedControl! //0: 포장 1: 예약
    @IBOutlet weak var storeTable: UITableView!
    var storeArray: [Store] = []
    @IBOutlet weak var searchView: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        storeTable.dataSource = self
        storeTable.delegate = self
        storeTable.rowHeight = 300
        searchView.layer.cornerRadius = 10
    }
    @IBAction func doSearch(_ sender: Any) {
        var url = ""
        let decoder = JSONDecoder()
        
        switch(orderMod.selectedSegmentIndex){
        case 0:
            url = "http://54.180.213.178:8080/store/packable"
        case 1:
            url = "http://54.180.213.178:8080/store/reservable"
        default:
            print("유효하지 않은 주문 모드")
            return
        }
        switch(searchMod.selectedSegmentIndex){
        case 0:
            url += "?store_name="
        case 1:
            url += "?menu_name="
        default:
            print("유효하지 않은 검색 모드")
        }
        guard let search_content = searchBar.text, !search_content.isEmpty else{
            showToast(message: "검색어를 입력해주세요.")
            return
        }
        url += search_content
        print(url)
        
        let dataRequest = AF.request(url,
                   method: .get,
                   headers: ["Accept":"application/json"])
        dataRequest.responseData{ dataResponse in
            switch(dataResponse.result){
            case .success(_):
                guard let decodedData = try? decoder.decode([Store].self, from: dataResponse.value!) else {
                    print("json해독실패")
                    return }
                self.storeArray = decodedData
                
            case .failure(_):
                print("검색에 실패하였습니다.")
            }
            let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){_ in
                self.storeTable.reloadData()
            }
            
        }
        
        
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
}
