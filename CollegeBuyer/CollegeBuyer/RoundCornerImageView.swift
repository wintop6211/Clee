//
//  RoundCornerImageView.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 10/24/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

@IBDesignable
class RoundCornerImageView: UIImageView {
    
    @IBInspectable public var roundDegree: CGFloat = 10 {
        didSet {
            self.layer.cornerRadius = roundDegree
        }
    }
    
    override func awakeFromNib() {
        self.layer.cornerRadius = roundDegree
        self.clipsToBounds = true
    }
}
