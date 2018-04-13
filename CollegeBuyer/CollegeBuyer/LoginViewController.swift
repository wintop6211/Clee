//
//  LoginViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 4/17/17.
//  Copyright © 2017 Milwaukee School of Engineering. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController {
    @IBOutlet weak var emailAddressTextField: UITextField!
    @IBOutlet weak var emailAddressErrorLabel: UILabel!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var passwordErrorLabel: UILabel!
    @IBOutlet weak var loginButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        addDoneButtonToKeyboard()
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

// IBActions
extension LoginViewController {
    @IBAction func login(_ sender: UIButton) {
        self.loginButton.setTitle("Logging In...", for: .normal)
        self.loginButton.isUserInteractionEnabled = false
        
        let isLoginInfoIntegral = self.checkLoginInfoIntegrity()
        
        DispatchQueue.global(qos: .userInitiated).async {
            let resetLoginButtonState = {
                DispatchQueue.main.async {
                    self.loginButton.setTitle("Login", for: .normal)
                    self.loginButton.isUserInteractionEnabled = true
                }
            }
            
            let displayServerInternalErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Server Internal Error"), animated: true, completion: resetLoginButtonState)
                }
            }
            
            let displayNetworkErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Network Error"), animated: true, completion: resetLoginButtonState)
                }
            }
            
            let displayTimeoutErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: resetLoginButtonState)
                }
            }
            
            if isLoginInfoIntegral {
                var isLoggedIn = false
                do {
                    var isErrorHappened = false
                    
                    isLoggedIn = try UserServices.login(emailAddress: self.emailAddressTextField.text!, password: self.passwordTextField.text!, serverInternalErrorHandler: {
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
                
                if isLoggedIn {
                    let mainStoryboard = UIStoryboard(name: "Main", bundle: nil)
                    let vc = mainStoryboard.instantiateViewController(withIdentifier: "TabView")
                    DispatchQueue.main.async {
                        self.present(vc, animated: true, completion: nil)
                    }
                } else {
                    DispatchQueue.main.async {
                        self.present(UIAlertController.Factory.getInformController(message: "Email or password is incorrect"), animated: true, completion: resetLoginButtonState)
                    }
                }
            } else { resetLoginButtonState() }
        }
    }
}

// Handlers
extension LoginViewController {
    func keyboardDoneClicked() {
        view.endEditing(true)
    }
}

// Helpers
extension LoginViewController {
    func addDoneButtonToKeyboard() {
        let toolBar = UIToolbar()
        toolBar.sizeToFit()
        
        let doneButton = UIBarButtonItem(barButtonSystemItem: .done, target: self, action: #selector(self.keyboardDoneClicked))
        
        toolBar.setItems([doneButton], animated: false)
        
        emailAddressTextField.inputAccessoryView = toolBar
        passwordTextField.inputAccessoryView = toolBar
    }
    
    func checkLoginInfoIntegrity() -> Bool {
        clearAllErrorLabel()
        
        var result = true
        
        if emailAddressTextField.text!.isEmpty {
            emailAddressErrorLabel.text = " Need to be filled"
            emailAddressTextField.becomeFirstResponder()
            result = false
        }
        if passwordTextField.text!.isEmpty {
            passwordErrorLabel.text = " Need to be filled"
            if !emailAddressTextField.text!.isEmpty { passwordTextField.becomeFirstResponder() }
            result = false
        }
        
        return result
    }
    
    func clearAllErrorLabel() {
        emailAddressErrorLabel.text = ""
        passwordErrorLabel.text = ""
    }
}

extension LoginViewController : UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField.returnKeyType == .next {
            self.view.viewWithTag(textField.tag + 1)!.becomeFirstResponder()
        }
        
        if textField.returnKeyType == .done {
            self.keyboardDoneClicked()
        }
        
        return true
    }
}
