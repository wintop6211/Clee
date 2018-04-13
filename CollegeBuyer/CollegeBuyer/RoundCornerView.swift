//
//  RoundCornerView.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 8/24/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

@IBDesignable
class RoundCornerView: UIView {
    
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
