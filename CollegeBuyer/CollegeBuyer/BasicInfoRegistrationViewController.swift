//
//  BasicInfoRegistrationViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 3/30/17.
//  Copyright © 2017 Milwaukee School of Engineering. All rights reserved.
//

import Foundation
import UIKit

class BasicInfoRegistrationViewController: UIViewController, UINavigationControllerDelegate {
    
    @IBOutlet weak var profilePictureImageView: UIImageView!
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var nameErrorLabel: UILabel!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var emailErrorLabel: UILabel!
    @IBOutlet weak var genderErrorLabel: UILabel!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var passwordErrorLabel: UILabel!
    @IBOutlet weak var retypePasswordTextField: UITextField!
    @IBOutlet weak var retypePasswordErrorLabel: UILabel!
    @IBOutlet weak var phoneNumberTextField: UITextField!
    @IBOutlet weak var phoneNumberErrorLabel: UILabel!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var maleButton: UIButton!
    @IBOutlet weak var femaleButton: UIButton!
    @IBOutlet weak var signUpButton: UIButton!
    
    var activeTextField: UITextField?
    var radioButtonController: SSRadioButtonsController!

    // ViewController life cycle related methods
    override func viewDidLoad() {
        super.viewDidLoad()

        registerForKeyboardNotifications()
        addDoneButtonToKeyboard()
        radioButtonController = SSRadioButtonsController(buttons: maleButton, femaleButton)
    }

    override func viewWillDisappear(_ animated: Bool) {
        view.endEditing(true)
    }
}

// IBActions
extension BasicInfoRegistrationViewController {
    @IBAction func textFieldDidBeginEditing(_ sender: UITextField) {
        activeTextField = sender
    }
    
    @IBAction func textFieldDidEndEditing(_ sender: UITextField) {
        activeTextField = nil
    }
    
    @IBAction func close(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func chooseProfilePicture(_ sender: UIButton) {
        let imagePickerController = UIImagePickerController()
        imagePickerController.allowsEditing = true
        imagePickerController.delegate = self
        
        let actionSheet = UIAlertController(title: "Choose a Picture", message: nil, preferredStyle: .actionSheet)
        if UIImagePickerController.isSourceTypeAvailable(.camera) {
            actionSheet.addAction(UIAlertAction(title: "Camera", style: .default, handler: {
                (action: UIAlertAction) in
                
                imagePickerController.sourceType = .camera
                self.present(imagePickerController, animated: true, completion: nil)
            }))
        }
        
        actionSheet.addAction(UIAlertAction(title: "Photo Gallery", style: .default, handler: {
            (action: UIAlertAction) in
            
            imagePickerController.sourceType = .photoLibrary
            self.present(imagePickerController, animated: true, completion: nil)
        }))
        
        actionSheet.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        activeTextField?.resignFirstResponder()
        self.present(actionSheet, animated: true, completion: nil)
    }
    
    @IBAction func signUp(_ sender: UIButton) {
        signUpButton.setTitle("Signing Up...", for: .normal)
        signUpButton.isUserInteractionEnabled = false
        
        let checkRegistrationInfoIntegrityResult = checkRegistrationInfoIntegrity()
        
        if checkRegistrationInfoIntegrityResult == true {
            var gender: Gender!
            if maleButton.isSelected { gender = .male } else if femaleButton.isSelected { gender = .female }
            
            DispatchQueue.global(qos: .userInitiated).async {
                let resetSignupButtonState = {
                    DispatchQueue.main.async {
                        self.signUpButton.setTitle("Sign Up", for: .normal)
                        self.signUpButton.isUserInteractionEnabled = true
                    }
                }
                
                let displaySuccessAlert = {
                    DispatchQueue.main.async {
                        self.present(UIAlertController.Factory.getInformController(message: "Signed up! Check your mailbox and activate your account.", handler: {_ in self.dismiss(animated: true, completion: nil)}), animated: true, completion: resetSignupButtonState)
                    }
                }
                
                let displayServerInternalErrorAlert = {
                    DispatchQueue.main.async {
                        self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Server Internal Error"), animated: true, completion: resetSignupButtonState)
                    }
                }
                
                let displayNetworkErrorAlert = {
                    DispatchQueue.main.async {
                        self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Network Error"), animated: true, completion: resetSignupButtonState)
                    }
                }
                
                let displayTimeoutErrorAlert = {
                    DispatchQueue.main.async {
                        self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: resetSignupButtonState)
                    }
                }
                
                do {
                    var isErrorHappened = false
                    try UserServices.signUp(name: self.nameTextField.text!, emailAddress: self.emailTextField.text!, password: self.passwordTextField.text!, gender: gender, phoneNumber: self.phoneNumberTextField.text!, profilePic: self.profilePictureImageView.image!, schoolName: "Milwaukee School of Engineering", serverInternalErrorHandler: {
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
                    displaySuccessAlert()
                }
            }
        }
    }
}

// Handlers
extension BasicInfoRegistrationViewController {
    func doneClicked() {
        view.endEditing(true)
    }
    
    func keyboardWillShow(notification: NSNotification) {
        var info = notification.userInfo!
        let keyboardSize = (info[UIKeyboardFrameBeginUserInfoKey] as! NSValue).cgRectValue.size
        let contentInsets = UIEdgeInsetsMake(0.0, 0.0, keyboardSize.height, 0.0)
        
        self.scrollView.contentInset = contentInsets
        self.scrollView.scrollIndicatorInsets = contentInsets
    }
    
    func keyboardWillHide(notification: NSNotification) {
        self.view.endEditing(true)
        self.scrollView.contentInset = UIEdgeInsets.zero
        self.scrollView.scrollIndicatorInsets = UIEdgeInsets.zero
    }
}

// Helpers
extension BasicInfoRegistrationViewController {
    func registerForKeyboardNotifications() {
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(notification:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    func clearAllErrorLabel() {
        nameErrorLabel.text = ""
        emailErrorLabel.text = ""
        passwordErrorLabel.text = ""
        retypePasswordErrorLabel.text = ""
        phoneNumberErrorLabel.text = ""
    }
    
    func addDoneButtonToKeyboard() {
        let toolBar = UIToolbar()
        toolBar.sizeToFit()
        
        let doneButton = UIBarButtonItem(barButtonSystemItem: .done, target: self, action: #selector(self.doneClicked))
        
        toolBar.setItems([doneButton], animated: false)
        
        nameTextField.inputAccessoryView = toolBar
        emailTextField.inputAccessoryView = toolBar
        passwordTextField.inputAccessoryView = toolBar
        retypePasswordTextField.inputAccessoryView = toolBar
        phoneNumberTextField.inputAccessoryView = toolBar
    }
    
    func checkIfNecessaryFieldsFilled(fieldsAndErrorLabels: [UITextField : UILabel]) -> Bool {
        var result = true
        
        for fieldAndErrorLabel in fieldsAndErrorLabels {
            if fieldAndErrorLabel.key.text!.isBlank {
                fieldAndErrorLabel.value.text = "Need to be Filled"
                
                result = false
            }
        }
        return result
    }
    
    func checkRegistrationInfoIntegrity() -> Bool {
        clearAllErrorLabel()
        
        let resetSignupButtonState = {
            self.signUpButton.setTitle("Sign Up", for: .normal)
            self.signUpButton.isUserInteractionEnabled = true
        }
        
        let filledCheckResult = checkIfNecessaryFieldsFilled(fieldsAndErrorLabels: [nameTextField : nameErrorLabel, emailTextField : emailErrorLabel, passwordTextField : passwordErrorLabel, retypePasswordTextField : retypePasswordErrorLabel, phoneNumberTextField : phoneNumberErrorLabel])
        if filledCheckResult == false {
            resetSignupButtonState()
            return filledCheckResult
        }
        
        if !emailTextField.text!.isValidEduEmail {
            resetSignupButtonState()
            emailErrorLabel.text = "(Email address isn't valid)"
            return false
        }
        
        var checkIfEmailAddressExistedResult = false
        
        do {
            var isErrorHappened = false
            checkIfEmailAddressExistedResult = try UserServices.checkIfEmailAddressExisted(emailAddress: emailTextField.text!, serverInternalErrorHandler: {
                _ in
                
                self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Server Internal Error"), animated: true, completion: nil)
                isErrorHappened = true
            }, networkErrorHandler: {
                _ in

                self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Network Error"), animated: true, completion: nil)
                isErrorHappened = true
            })
            
            if isErrorHappened {
                resetSignupButtonState()
                return false
            }
        } catch {
            self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: nil)
            resetSignupButtonState()
            return false
        }
        
        if checkIfEmailAddressExistedResult == true {
            resetSignupButtonState()
            emailErrorLabel.text = "(Email already been used)"
            return false
        }
        
        if passwordTextField.text! != retypePasswordTextField.text! {
            resetSignupButtonState()
            passwordErrorLabel.text = "(Password isn't consistent)"
            return false
        }
        
        if !passwordTextField.text!.isValidPassword {
            resetSignupButtonState()
            passwordErrorLabel.text = "(Password doesn't meet security requirement)"
            return false
        }
        
        if !phoneNumberTextField.text!.isPhoneNumber {
            resetSignupButtonState()
            phoneNumberErrorLabel.text = "(Phone number isn't valid)"
            return false
        }
        
        var checkIfPhoneNumberExistedResult = false
        
        do {
            var isErrorHappened = false
            checkIfPhoneNumberExistedResult = try UserServices.checkIfPhoneNumberExisted(phoneNumber: phoneNumberTextField.text!, serverInternalErrorHandler: {
                _ in
                
                self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Server Internal Error"), animated: true, completion: nil)
                isErrorHappened = true
            }, networkErrorHandler: {
                _ in

                self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Network Error"), animated: true, completion: nil)
                isErrorHappened = true
            })
            
            if isErrorHappened {
                resetSignupButtonState()
                return false
            }
        } catch {
            self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: nil)
            resetSignupButtonState()
            return false
        }
        
        if checkIfPhoneNumberExistedResult == true {
            resetSignupButtonState()
            phoneNumberErrorLabel.text = "(Phone number already been used)"
            return false
        }
        
        return true
    }
}

extension BasicInfoRegistrationViewController: UIImagePickerControllerDelegate {
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        if let image = info[UIImagePickerControllerEditedImage] as? UIImage {
            profilePictureImageView.image = image
        }
        
        picker.dismiss(animated: true)
    }
}

extension BasicInfoRegistrationViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField.returnKeyType == .next {
            self.view.viewWithTag(textField.tag + 1)!.becomeFirstResponder()
        }
        
        if textField.returnKeyType == .done {
            activeTextField!.resignFirstResponder();
        }
        
        return true
    }
}
