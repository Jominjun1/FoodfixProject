//
//  socketTestController.swift
//  foodfix
//
//  Created by rhm on 2024/04/26.
//

import Foundation
import UIKit
import Alamofire
import Starscream

class socketTestController: UIViewController, WebSocketDelegate{
    private var socket: WebSocket?
    private let socketUrl = URL(string: "ws://54.180.213.178:8080/wsk")
    
    func connect(){
        let request = URLRequest(url: socketUrl!)
        self.socket = WebSocket(request: request)
        self.socket?.delegate = self
        self.socket?.connect()
        
    }
    func disconnect(){
        self.socket?.disconnect()
    }
    
    func didReceive(event: Starscream.WebSocketEvent, client: Starscream.WebSocketClient) {
        switch event{
        case .connected(_):
            print("서버와 연결되었습니다.")
            client.write(string: read(key:"token")!)
        case .disconnected(_, _):
            print("서버와 연결이 해제되었습니다.")
        case .text(let string):
            print(string)
        case .binary(_):
            print("binary")
        case .pong(_):
            print("pong")
        case .ping(_):
            print("ping")
        case .error(_):
            print("error")
        case .viabilityChanged(_):
            print("viabilityChanged")
        case .reconnectSuggested(_):
            print("reconnectSuggested")
        case .cancelled:
            print("cancelled")
        case .peerClosed:
            print("peerClosed")
        }
    }
    
    @IBAction func callSocket(_ sender: Any) {
        let token = read(key:"token")
        connect()
 
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
}
