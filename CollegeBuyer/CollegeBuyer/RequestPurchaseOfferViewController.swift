//
//  RequestPurchaseOfferViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 12/9/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

class RequestPurchaseOfferViewController: UIViewController {
    
    @IBOutlet weak var offerPriceTextField: CurrencyTextField!
    
    var itemId: Int!
    var originPrice: Float!
    var sellerName: String!
    
    override var prefersStatusBarHidden : Bool {
        return true
    }
    
    // Life Cycle Methods
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if self.offerPriceTextField.text!.isEmpty {
            self.offerPriceTextField.text = "$\(originPrice)"
        }
    }
    
    // Other View Controller Related Methods
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // IBActions
    @IBAction func close(_ sender: UIButton) {
        self.navigationController!.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func next(_ sender: UIButton) {
        let mainStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController = mainStoryboard.instantiateViewController(withIdentifier: "RequestPurchaseNoteView") as! RequestPurchaseNoteViewController
        
        viewController.itemId = itemId
        viewController.offerPrice = offerPriceTextField.currencyValue
        viewController.sellerName = sellerName
        
        self.navigationController!.pushViewController(viewController, animated: true)
    }
}
