//
//  SSRadioButton.swift
//  CollegeBuyer
//
//  Created by yipei zhu on 8/20/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import Foundation
import UIKit

@IBDesignable
class RadioButton: UIButton {
    @IBInspectable public var corderRadius: CGFloat = 6.0 {
        didSet {
            self.layer.cornerRadius = corderRadius
        }
    }
    @IBInspectable public var borderWidth: CGFloat = 2 {
        didSet {
            self.layer.borderWidth = borderWidth
        }
    }
    @IBInspectable public var borderColor: UIColor = UIColor.clear {
        didSet {
            self.layer.borderColor = borderColor.cgColor
        }
    }
    @IBInspectable public var selectedBackgroundColor: UIColor = UIColor.clear
    @IBInspectable public var unSelectedBackgroundColor: UIColor = UIColor.clear
    @IBInspectable public var selectedTextColor: UIColor = UIColor.black
    @IBInspectable public var unSelectedTextColor: UIColor = UIColor.black
    
    override var isSelected: Bool {
        didSet {
            if isSelected {
                self.backgroundColor = selectedBackgroundColor
            } else {
                self.backgroundColor = unSelectedBackgroundColor
            }
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.layer.cornerRadius = corderRadius
        self.layer.borderWidth = borderWidth
        self.layer.borderColor = borderColor.cgColor
        
        self.setTitleColor(unSelectedTextColor, for: .normal)
        self.setTitleColor(selectedTextColor, for: .selected)
        
        self.backgroundColor = self.unSelectedBackgroundColor
    }
}
