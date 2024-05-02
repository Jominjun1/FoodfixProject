//
//  MapViewController.swift
//  tm
//
//  Created by 노현민 on 2024/02/19.
//

import Foundation
import UIKit

class mapViewController : UIViewController{
    
    @IBAction func onClick(_ sender: Any) {
        let vcName = self.storyboard?.instantiateViewController(withIdentifier: "ViewController")
        vcName?.modalPresentationStyle = .fullScreen
        vcName?.modalTransitionStyle = .crossDissolve
        self.present(vcName!, animated: true, completion: nil)
        
    }
    
}
