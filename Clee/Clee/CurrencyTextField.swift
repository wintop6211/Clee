//
//  CurrencyTextField.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 9/2/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

@IBDesignable
class CurrencyTextField: TextFieldWithUnderLine {
    
    var currencyValue: Float {
        get {
            var price = self.text!
            let index = price.index(price.startIndex, offsetBy: 1)
            price = price.substring(from: index)
            
            return Float(price)!
        }
    }
    
    override func awakeFromNib() {
        self.delegate = self
    }
}

extension CurrencyTextField : UITextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.text = "$"
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        // Automatically set empty imput to "$0.00"
        if textField.text! == "$" {
            textField.text = "$0.00"
        }
        if textField.text!.last == "." {
            textField.text = textField.text! + "00"
        }
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField.text == "$" && range.location == 0 {
            return false
        }
        
        if textField.text!.substring(to: textField.text!.index(textField.text!.startIndex, offsetBy: 1)) != "$" {
            textField.text = "$\(textField.text!)"
        }
        
        return true
    }
}


