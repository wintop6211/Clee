//
//  Services.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 12/30/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import Foundation

class ItemServices {
    
    static func loadItemPictures(id: Int) -> [UIImage] {
        let semaphore = DispatchSemaphore(value: 0)
        var continueProcess: Bool = true
        
        var imageIndex = 0
        var itemPictures = [UIImage]()
        
        while continueProcess {
            let url = "/ProductServices/getItemImage/\(id)/\(imageIndex)"
            
            Server.GETRequest(accessPoint: url, acceptJson: false, handler: {(data: Data?, response: URLResponse?, error: Error?) -> Void in
                if error != nil{
                    print(error!.localizedDescription)
                    continueProcess = false
                } else {
                    if data != nil {
                        if data!.count != 0 {
                            imageIndex += 1
                            
                            itemPictures.append(UIImage(data: data!)!)
                        } else {
                            continueProcess = false
                        }
                    } else {
                        continueProcess = false
                    }
                }
                
                semaphore.signal()
            })
            
            _ = semaphore.wait(timeout: .distantFuture)
        }
        
        return itemPictures
    }
    
    static func loadProfilePic(id: Int) -> UIImage {
        let semaphore = DispatchSemaphore(value: 0)
        var profilePic : UIImage?
        Server.GETRequest(accessPoint: "/UserServices/getProfilePic/\(id)", acceptJson: false, handler: {
            (data: Data?, response: URLResponse?, error: Error?) -> Void in
            
            if data != nil {
                profilePic = UIImage(data: data!)
            }
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if profilePic == nil {
            return UIImage(named: "blank_profile_picture_placeholder")!
        }
        return profilePic!
    }

}
