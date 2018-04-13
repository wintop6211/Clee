//
//  AddPictureButton.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 6/30/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

@IBDesignable
class CircularAddButton: UIButton {
    
    @IBInspectable public var fillColor: UIColor = Colors.mainThemeRed {
        didSet {
            setNeedsDisplay()
        }
    }
    
    @IBInspectable public var addSignColor: UIColor = UIColor.white {
        didSet {
            setNeedsDisplay()
        }
    }
    
    @IBInspectable public var lineWidth: CGFloat = 2 {
        didSet {
            setNeedsDisplay()
        }
    }

    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        var bezierPath = UIBezierPath(arcCenter: CGPoint(x: self.frame.width / 2,y: self.frame.height / 2), radius: CGFloat(self.frame.width / 2 - 2), startAngle: CGFloat(0), endAngle:CGFloat(Double.pi * 2), clockwise: true)
        
        let lineLength = self.frame.width * 0.6
        let bound = (self.frame.width - lineLength) / 2
        
        fillColor.setStroke()
        fillColor.setFill()
        
        bezierPath.stroke()
        bezierPath.fill()
        
        bezierPath = UIBezierPath()
        bezierPath.lineWidth = lineWidth
        addSignColor.setStroke()
        
        bezierPath.move(to: CGPoint(x: bound, y: self.frame.height / 2))
        bezierPath.addLine(to: CGPoint(x: self.frame.width - bound, y: self.frame.height / 2))
        
        bezierPath.stroke()
        
        bezierPath.move(to: CGPoint(x: self.frame.width / 2, y: bound))
        bezierPath.addLine(to: CGPoint(x: self.frame.width / 2, y: self.frame.height - bound))
        
        bezierPath.stroke()
    }
    
}
