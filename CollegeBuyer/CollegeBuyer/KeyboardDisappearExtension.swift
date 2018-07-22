//
//  KeyboardDisappearExtension.swift
//  CollegeBuyer
//
//  Created by Yahui Liang on 7/8/18.
//  Copyright © 2018 CollegeBuyer. All rights reserved.
//

import UIKit

extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}
