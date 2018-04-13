//
// Created by TIANTUO YOU on 1/10/18.
// Copyright (c) 2018 CollegeBuyer. All rights reserved.
//

import UIKit

enum Condition: Int {
    case new
    case likeNew
    case good
    case acceptable
}

class Item {
    var id: Int
    var name: String
    var pictures: [UIImage]
    var price: Float
    var condition: Condition
    var description: String

    var sellerId: Int
    var sellerName: String
    var sellerSchool: String
    var sellerProfilePic: UIImage
    var sellerEmail: String?
    var offers: [Offer]?

    init(id: Int, name: String, condition: Condition, images: [UIImage], price: Float, description: String, sellerId: Int, sellerName: String, sellerSchool: String, sellerProfilePic: UIImage, sellerEmail: String?, offers: [Offer]?) {
        self.id = id
        self.name = name
        self.condition = condition
        self.pictures = images
        self.price = price
        self.description = description

        self.sellerId = sellerId
        self.sellerName = sellerName
        self.sellerSchool = sellerSchool
        self.sellerProfilePic = sellerProfilePic
        self.sellerEmail = sellerEmail
        self.offers = offers
    }
}
