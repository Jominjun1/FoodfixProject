//
//  reservationDetailViewController.swift
//  foodfix
//
//  Created by rhm on 2024/04/20.
//

import Foundation
import Alamofire


class reservationDetailViewController : UIViewController{
    var orderData : ReserveOrder?
    
    @IBOutlet weak var reserveIdLabel: UILabel!
    @IBOutlet weak var reserveTimeLabel: UILabel!
    @IBOutlet weak var numPeopleLabel: UILabel!
    @IBOutlet weak var userCommentLabel: UILabel!
    @IBOutlet weak var reservationStatusLabel: UILabel!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        reserveIdLabel.text = String((orderData?.reservation_id)!)
        reserveTimeLabel.text = (orderData?.reservation_date)! + " " + (orderData?.reservation_time)!
        numPeopleLabel.text = String((orderData?.num_people)!)
        userCommentLabel.text = (orderData?.user_comments)!
        reservationStatusLabel.text = (orderData?.reservation_status)!
        
    }
    
}
