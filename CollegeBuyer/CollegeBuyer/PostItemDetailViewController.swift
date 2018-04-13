//
//  PostItemDetailViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 10/23/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

class PostItemDetailViewController: UIViewController {
    
    @IBOutlet weak var priceTextField: CurrencyTextField!
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var nameErrorLabel: UILabel!
    
    @IBOutlet weak var newConditionButton: UIButton!
    @IBOutlet weak var likeNewConditionButton: UIButton!
    @IBOutlet weak var goodConditionButton: UIButton!
    @IBOutlet weak var acceptableConditionButton: UIButton!
    
    @IBOutlet weak var otherCategoryButton: UIButton!
    @IBOutlet weak var booksCategoryButton: UIButton!
    @IBOutlet weak var electronicsCategoryButton: UIButton!
    @IBOutlet weak var videoGamesCategoryButton: UIButton!
    @IBOutlet weak var furnitureCategoryButton: UIButton!
    @IBOutlet weak var beautyAndHealthCategoryButton: UIButton!
    
    var categoryRadioButtonsController: SSRadioButtonsController!
    var conditionRadioButtonsController: SSRadioButtonsController!
    var images: [UIImage]!
    override var prefersStatusBarHidden : Bool {
        return true
    }
    
    // Life Cycle Methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        nameTextField.becomeFirstResponder()
        
        conditionRadioButtonsController = SSRadioButtonsController(buttons: newConditionButton, likeNewConditionButton, goodConditionButton, acceptableConditionButton)
        conditionRadioButtonsController.pressed(newConditionButton)
        
        categoryRadioButtonsController = SSRadioButtonsController(buttons: otherCategoryButton, booksCategoryButton, electronicsCategoryButton, videoGamesCategoryButton, furnitureCategoryButton, beautyAndHealthCategoryButton)
        categoryRadioButtonsController.pressed(otherCategoryButton)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // IBActions
    
    @IBAction func back(_ sender: UIButton) {
        self.navigationController!.popViewController(animated: true)
    }
    
    @IBAction func next(_ sender: UIButton) {
        if nameTextField.text!.isEmpty {
            nameErrorLabel.text = " Need to be filled"
            nameTextField.becomeFirstResponder()
        } else {
            // Get next view controller
            let mainStoryboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController = mainStoryboard.instantiateViewController(withIdentifier: "PostItemDescriptionView") as! PostItemDescriptionViewController
            
            viewController.name = nameTextField.text!
            viewController.price = priceTextField.currencyValue
            viewController.condition = getCondition()
            viewController.category = getCategory()
            viewController.images = images
            
            nameErrorLabel.text = ""
            
            self.navigationController!.pushViewController(viewController, animated: true)
        }
    }
    
    func getCondition() -> Condition {
        if newConditionButton.isSelected {
            return Condition.new
        } else if  likeNewConditionButton.isSelected {
            return Condition.likeNew
        } else if  goodConditionButton.isSelected {
            return Condition.good
        } else if acceptableConditionButton.isSelected {
            return Condition.acceptable
        }
        
        return Condition.new
    }
    
    func getCategory() -> Category {
        if videoGamesCategoryButton.isSelected {
            return Category.videoGame
        } else if booksCategoryButton.isSelected {
            return Category.book
        } else if electronicsCategoryButton.isSelected {
            return Category.electronics
        } else if furnitureCategoryButton.isSelected {
            return Category.furniture
        } else if beautyAndHealthCategoryButton.isSelected {
            return Category.beautyAndHealth
        } else if otherCategoryButton.isSelected {
            return Category.other
        }
        
        return Category.book
    }
}
