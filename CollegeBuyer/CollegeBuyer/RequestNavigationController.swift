//
//  RequestNagavitionController.swift
//  CollegeBuyer
//
//  Created by yipei zhu on 12/22/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import Foundation
import UIKit

class RequestNavigationController: UINavigationController {
    public func transferItemInfo(itemId: Int, originPrice: Float, sellerName: String) {
        let viewController = viewControllers[0] as! RequestPurchaseOfferViewController
        
        viewController.itemId = itemId
        viewController.originPrice = originPrice
        viewController.sellerName = sellerName
    }
}
