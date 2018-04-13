//
// Created by TIANTUO YOU on 12/16/17.
// Copyright (c) 2017 CollegeBuyer. All rights reserved.
//

import Foundation
import UIKit

class Offer: NSObject {
    var id: Int
    var price: Float
    var note: String

    var buyerName: String
    var buyerProfilePic: UIImage

    init(id: Int, price: Float, note: String, buyerName: String, buyerProfilePic: UIImage) {
        self.id = id
        self.price = price
        self.note = note
        
        self.buyerName = buyerName
        self.buyerProfilePic = buyerProfilePic
    }
}
