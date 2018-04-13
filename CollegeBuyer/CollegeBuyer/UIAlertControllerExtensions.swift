//
//  UIAlertControllerExtensions.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 3/23/18.
//  Copyright © 2018 CollegeBuyer. All rights reserved.
//

import Foundation
import UIKit

extension UIAlertController {
    class Factory {
        static func getErrorMessageAlertController(message: String) ->  UIAlertController {
            let alertController = UIAlertController(title: "Opps!", message: message, preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
            
            return alertController
        }
        
        static func getInformController(message: String) ->  UIAlertController {
            let alertController = UIAlertController(title: nil, message: message, preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
            
            return alertController
        }
    }
}
