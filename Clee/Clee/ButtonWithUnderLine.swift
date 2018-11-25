//
//  ButtonWithUnderLine.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 6/30/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

@IBDesignable
class ButtonWithUnderLine: UIButton {
    
    @IBInspectable public var lineColor: UIColor = UIColor.gray {
        didSet {
            setNeedsDisplay()
        }
    }
    
    @IBInspectable public var lineWidth: CGFloat = 1 {
        didSet {
            setNeedsDisplay()
        }
    }
    
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        let bezirePath = UIBezierPath()
        lineColor.setStroke()
        bezirePath.lineWidth = lineWidth
        
        bezirePath.move(to: CGPoint(x: 0, y: self.frame.height))
        bezirePath.addLine(to: CGPoint(x: self.frame.width, y: self.frame.height))
        
        bezirePath.stroke()
    }
}
