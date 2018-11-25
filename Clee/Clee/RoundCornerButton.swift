//
//  RoundCornerButton.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 7/23/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

@IBDesignable
class RoundCornerButton: UIButton {
    
    @IBInspectable public var roundDegree: CGFloat = 14.0 {
        didSet {
            self.layer.cornerRadius = roundDegree
        }
    }
    
    override func awakeFromNib() {
        self.layer.cornerRadius = roundDegree
    }
}
