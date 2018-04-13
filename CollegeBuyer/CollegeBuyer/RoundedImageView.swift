//
//  RoundedImageView.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 9/22/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

@IBDesignable
class RoundedImageView: UIImageView {
    
    @IBInspectable public var roundDegree: CGFloat = 6.0 {
        didSet {
            self.layer.cornerRadius = roundDegree
        }
    }
    
    // Life Cycle Methods
    
    override func awakeFromNib() {
        self.layer.cornerRadius = roundDegree
    }

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
