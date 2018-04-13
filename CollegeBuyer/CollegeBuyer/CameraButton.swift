//
//  CameraButton.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 8/15/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

// Unused class, might be useful in future

import UIKit

@IBDesignable
class CameraButton: UIButton {
    
    @IBInspectable public var outerCircleColor: UIColor = UIColor.white {
        didSet {
            setNeedsDisplay()
        }
    }
    
    @IBInspectable public var innerCircleColor: UIColor = Colors.mainThemeRed {
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
        var bezierPath = UIBezierPath(arcCenter: CGPoint(x: self.frame.width / 2,y: self.frame.height / 2), radius: CGFloat(self.frame.width / 2 - 2), startAngle: CGFloat(0), endAngle:CGFloat(Double.pi * 2), clockwise: true)
        
        outerCircleColor.setStroke()
        outerCircleColor.setFill()
        
        bezierPath.stroke()
        bezierPath.fill()
        
        bezierPath = UIBezierPath(arcCenter: CGPoint(x: self.frame.width / 2,y: self.frame.height / 2), radius: CGFloat(self.frame.width / 2 - self.frame.width * 0.15), startAngle: CGFloat(0), endAngle:CGFloat(Double.pi * 2), clockwise: true)
        
        innerCircleColor.setStroke()
        innerCircleColor.setFill()
        
        bezierPath.stroke()
        bezierPath.fill()
    }
}
