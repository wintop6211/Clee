//
//  RequestPurchaseNoteViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 12/9/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

class RequestPurchaseNoteViewController: UIViewController {

    @IBOutlet weak var noteTextView: UITextView!
    @IBOutlet weak var instructionLabel: UILabel!
    
    var itemId: Int!
    var sellerName: String!
    var offerPrice: Float!
    
    override var prefersStatusBarHidden : Bool {
        return true
    }

    // UIViewContrller Life Cycle Methods
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    // Other View Controller Related Methods
    override func viewWillAppear(_ animated: Bool) {
        noteTextView.becomeFirstResponder()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    // IBActions
    @IBAction func back(_ sender: UIButton) {
        self.navigationController!.popViewController(animated: true)
    }
    
    @IBAction func sendRequest(_ sender: UIButton) {
        DispatchQueue.global(qos: .userInitiated).async {
            let resetInstructionLabel = {
                DispatchQueue.main.async {
                    self.instructionLabel.text = "Note"
                }
            }
            
            let displaySuccessAlert = {
                DispatchQueue.main.async {
                    self.present(UIAlertController.Factory.getInformController(message: "Offer Been Sent"), animated: true, completion: resetInstructionLabel)
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
                self.instructionLabel.text = "Sending.."
            }
            
            do {
                var isErrorHappened = false
                _ = try ItemServices.sendPurchaseRequest(itemId: self.itemId, offerPrice: self.offerPrice, note: self.noteTextView.text, serverInternalErrorHandler: {
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
