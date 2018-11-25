//
//  ForgotPasswordViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 1/3/18.
//  Copyright © 2018 CollegeBuyer. All rights reserved.
//

import UIKit

class ForgotPasswordViewController: UIViewController {
    @IBOutlet weak var emailAddressTextField: UITextField!
    @IBOutlet weak var notificationLabel: UILabel!
    @IBOutlet weak var confirmButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        emailAddressTextField.becomeFirstResponder()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // IBActions
    @IBAction func close(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func confirm(_ sender: UIButton) {
        self.confirmButton.setTitle("Processing...", for: .normal)
        self.confirmButton.isUserInteractionEnabled = false
        
        DispatchQueue.global(qos: .userInitiated).async {
            let resetConfirmButtonState = {
                DispatchQueue.main.async {
                    self.confirmButton.setTitle("Resend", for: .normal)
                    self.confirmButton.isUserInteractionEnabled = true
                }
            }
            
            let displayServerInternalErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Server Internal Error"), animated: true, completion: resetConfirmButtonState)
                }
            }
            
            let displayNetworkErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Network Error"), animated: true, completion: resetConfirmButtonState)
                }
            }
            
            let displayTimeoutErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: resetConfirmButtonState)
                }
            }
            
            var isEmailExisted = false
            
            do {
                var isErrorHappened = false
                isEmailExisted = try UserServices.checkIfEmailAddressExisted(emailAddress: self.emailAddressTextField.text!, serverInternalErrorHandler: {
                    _ in
                    
                    displayServerInternalErrorAlert()
                    isErrorHappened = true
                }, networkErrorHandler: {
                    _ in
                    
                    displayNetworkErrorAlert()
                    isErrorHappened = true
                })
                
                if isErrorHappened { return }
            } catch {
                displayTimeoutErrorAlert()
                return
            }
            
            if isEmailExisted {
                do {
                    var isErrorHappened = false
                    try UserServices.sendPasswordResetEmail(emailAddress: self.emailAddressTextField.text!, serverInternalErrorHandler: {
                        _ in
                        
                        displayServerInternalErrorAlert()
                        isErrorHappened = true
                    }, networkErrorHandler: {
                        _ in
                        
                        displayNetworkErrorAlert()
                        isErrorHappened = true
                    })
                    
                    if isErrorHappened { return }
                } catch {
                    displayTimeoutErrorAlert()
                    return
                }
                
                DispatchQueue.main.async {
                    self.notificationLabel.text = "Please check your mail box for password reset email"
                }
                resetConfirmButtonState()
            } else {
                DispatchQueue.main.async {
                    self.notificationLabel.text = "Email doesn't exist"
                    self.confirmButton.setTitle("Confirm", for: .normal)
                    self.confirmButton.isUserInteractionEnabled = true
                }
            }
        }
    }
}
