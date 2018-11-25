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
    
    @IBInspectable public var lineColor: UIColor = Colors.mainThemeRed {
        didSet {
            setNeedsDisplay()
        }
    }
    @IBInspectable public var lineWidth: CGFloat = 3 {
        didSet {
            setNeedsDisplay()
        }
    }
    
    override func awakeFromNib() {
        self.setTitle("", for: .normal)
    }

    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        let bezierPath = UIBezierPath()
        lineColor.setStroke()
        bezierPath.lineWidth = lineWidth
        
        bezierPath.move(to: CGPoint(x: 0, y: 0))
        bezierPath.addLine(to: CGPoint(x: self.frame.width, y: self.frame.height))
        bezierPath.stroke()
        
        bezierPath.move(to: CGPoint(x: self.frame.width, y: 0))
        bezierPath.addLine(to: CGPoint(x: 0, y: self.frame.height))
        bezierPath.stroke()
    }
}
