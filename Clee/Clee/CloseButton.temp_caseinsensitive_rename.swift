//
//  CrossButton.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 8/15/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

@IBDesignable
class CloseButton: UIButton {
    
    @IBInspectable public var lineColor: UIColor = UIColor.white {
        didSet {
            setNeedsDisplay()
        }
    }
    
    @IBInspectable public var lineWidth: CGFloat = 3 {
        didSet {
            setNeedsDisplay()
        }
    }
    
    
    
    // Life Cycle Methods
    
    override func awakeFromNib() {
        self.setTitle("", for: .normal)
    }

    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
        let beizerPath = UIBezierPath()
        lineColor.setStroke()
        beizerPath.lineWidth = lineWidth
        
        // Draw line
        beizerPath.move(to: CGPoint(x: 0, y: 0))
        beizerPath.addLine(to: CGPoint(x: self.frame.width, y: self.frame.height))
        beizerPath.stroke()
        
        beizerPath.move(to: CGPoint(x: self.frame.width, y: 0))
        beizerPath.addLine(to: CGPoint(x: 0, y: self.frame.height))
        beizerPath.stroke()
    }
}
