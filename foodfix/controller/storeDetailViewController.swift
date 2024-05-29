//
//  storeDetailViewController.swift
//  foodfix
//
//  Created by rhm on 2024/03/29.
//

import Foundation
import Alamofire
import Kingfisher



class MenuTableViewCell : UITableViewCell{
    @IBOutlet weak var menuName: UILabel!
    @IBOutlet weak var price: UILabel!
    @IBOutlet weak var menuImage: UIImageView!
    
}


extension storeDetailViewController : UITableViewDataSource, UITableViewDelegate{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return menuArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: MenuTableViewCell = tableView.dequeueReusableCell(withIdentifier: "menuCell", for: indexPath) as! MenuTableViewCell
        
        cell.menuName.text = menuArray[indexPath.row].menu_name
        cell.price.text = String(menuArray[indexPath.row].menu_price)
        var imagePath = menuArray[indexPath.row].imagePath.split(separator: "/")
        let url = URL(string: "http://54.180.213.178:8080/images/" + imagePath.popLast()!)
        print(url!)
        cell.menuImage.kf.indicatorType = .activity
        cell.menuImage.kf.setImage(with: url!, placeholder: nil, options: [.transition(.fade(0.7))], progressBlock: nil)
        cell.layer.cornerRadius = 10
        
        
        return cell
    }
   
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "goDetail", sender: menuArray[indexPath.row])
        
        
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "goDetail"){
            let vc = segue.destination as! menuDetailViewController
            guard let data = sender as? Menu else{
                print("prepare 실패")
                return
            }
            vc.menuDetail = data
        }
        else if(segue.identifier == "goReserve"){
            let vc = segue.destination as! reserveViewController
            guard let data = sender as? Store else{
                print("prepare 실패")
                return
            }
            vc.storeData = data
        }
        else if(segue.identifier == "goCart"){
            let vc = segue.destination as!cartViewController
            guard let data = sender as? Store else{
                print("prepare 실패")
                return
            }
            vc.storeData = data
        }
    }
    
    
}

class storeDetailViewController : UIViewController{
    
    @IBAction func unwindToStore(unwindsegue: UIStoryboardSegue){}
    
    @IBOutlet weak var tableView: UITableView!
    var url = "http://54.180.213.178:8080/user/menus/"
    let decoder = JSONDecoder()
    var menuArray: [Menu] = []
    
    @IBOutlet weak var thumbnailImageView: UIImageView!
    @IBOutlet weak var testLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var storeIntroLabel: UILabel!
    @IBOutlet weak var storePhoneLabel: UILabel!
    @IBOutlet weak var storeAddressLabel: UILabel!
    var storeDetail: Store?
    
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
    
    @IBAction func goCartView(_ sender: Any) {
        if let savedData = UserDefaults.standard.object(forKey: "cartArray")as? Data{
            if(savedData.count != 2){//장바구니에 하나라도 담겨있을 때. 디코딩하지 않은 상태라 비어있는 배열이면 []2바이트
                performSegue(withIdentifier: "goCart", sender: storeDetail)
            }else{//장바구니의 항목이 0개일 때
                showToast(message: "장바구니가 비었습니다.")
                return
            }
        }else{//UserDefaults에 장바구니가 존재하지 않을 때
            showToast(message: "장바구니가 비었습니다.")
            return
        }
        
    }
    
    @IBAction func goReserveView(_ sender: Any) {
        performSegue(withIdentifier: "goReserve", sender: storeDetail)
        
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        var imagePath = storeDetail!.imagePath.split(separator: "/")
        let imageurl = URL(string: "http://54.180.213.178:8080/images/" + imagePath.popLast()!)
        thumbnailImageView.kf.indicatorType = .activity
        thumbnailImageView.kf.setImage(with: imageurl!, placeholder: nil, options: [.transition(.fade(0.7))], progressBlock: nil)
        thumbnailImageView.layer.cornerRadius = 10

        testLabel.text = storeDetail?.store_name
        titleLabel.text = storeDetail?.store_name
        storeIntroLabel.text = storeDetail?.store_intro
        storePhoneLabel.text = "T. " + (storeDetail?.store_phone)!
        storeAddressLabel.text = "주소: " + (storeDetail?.store_address)!
        url = url + String((storeDetail?.store_id)!)
        
        let dataRequest = AF.request(url,
                                     method: .get,
                                     headers: ["Accept":"application/json"])
        
        dataRequest.responseData{ dataResponse in
            switch(dataResponse.result){
            case .success(_):
                guard let decodedData = try? self.decoder.decode([Menu].self, from: dataResponse.value!) else {
                    print("json해독실패")
                    return }
                print(decodedData.count)
                self.menuArray = decodedData
                
            case .failure(_):
                print("메뉴를 불러오는 것을 실패하였습니다.")
                
            }
        }
        
        self.tableView.rowHeight = 100
        self.tableView.delegate = self
        self.tableView.dataSource = self
        let timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: false){_ in
            self.tableView.reloadData()
        }
        
        
        
    }
}
