//
//  PostItemDescriptionViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 10/23/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

class PostItemDescriptionViewController: UIViewController {

    @IBOutlet weak var descriptionTextView: UITextView!
    @IBOutlet weak var instructionLabel: UILabel!
    
    var name: String!
    var price: Float!
    var condition: Condition!
    var category: Category!
    var images: [UIImage]!
    override var prefersStatusBarHidden : Bool {
        return true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        descriptionTextView.becomeFirstResponder()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func back(_ sender: UIButton) {
        self.navigationController!.popViewController(animated: true)
    }
    
    @IBAction func post(_ sender: UIButton) {
        DispatchQueue.global(qos: .userInitiated).async {
            let resetInstructionLabel = {
                DispatchQueue.main.async {
                    self.instructionLabel.text = "Description"
                }
            }
            
            let displaySuccessAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getInformController(message: "Item Posted", handler: {_ in }), animated: true, completion: resetInstructionLabel)
                }
            }
            
            let displayServerInternalErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Server Internal Error"), animated: true, completion: resetInstructionLabel)
                }
            }
            
            let displayNetworkErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Network Error"), animated: true, completion: resetInstructionLabel)
                }
            }
            
            let displayTimeoutErrorAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: resetInstructionLabel)
                }
            }
            
            DispatchQueue.main.async {
                self.instructionLabel.text = "Uploading..."
            }
            
            do {
                var isErrorHappened = false
                
                try ItemServices.postItemInfo(name: self.name, price: self.price, condition: self.condition, category: self.category, description: self.descriptionTextView.text, serverInternalErrorHandler: {
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
            
            self.postItemPictures()
            displaySuccessAlert()
        }
    }
    
    func postItemPictures() {
        let resetInstructionLabel = {
            DispatchQueue.main.async {
                self.instructionLabel.text = "Description"
            }
        }
        
        let displayServerInternalErrorAlert = {
            DispatchQueue.main.async {
                self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Server Internal Error"), animated: true, completion: resetInstructionLabel)
            }
        }
        
        let displayNetworkErrorAlert = {
            DispatchQueue.main.async {
                self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Network Error"), animated: true, completion: resetInstructionLabel)
            }
        }
        
        let displayTimeoutErrorAlert = {
            DispatchQueue.main.async {
                self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: resetInstructionLabel)
            }
        }
        
        do {
            var isErrorHappened = false
            
            try ItemServices.postItemPictures(pictures: images, serverInternalErrorHandler: {
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
    }
}
