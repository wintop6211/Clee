//
//  ItemDetailViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 10/26/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit
import MessageUI

class ItemDetailViewController: UIViewController {

    @IBOutlet weak var picturesScrollView: UIScrollView!
    @IBOutlet weak var pageControl: UIPageControl!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var conditionLabel: UILabel!
    @IBOutlet weak var descriptionTextView: UITextView!
    @IBOutlet weak var sellerNameLabel: UILabel!
    @IBOutlet weak var sellerProfilePicImageView: UIImageView!
    @IBOutlet weak var sellerRatingLabel: UILabel!
    
    var itemInfo: Item!
    let imageScrollViewLeftRightMargin: CGFloat = 15
    let imageScrollViewTopDownMargin: CGFloat = 15
    let closingGestureSensitivity = 80
    var pictureResolution = ImageResolution.lowRes

    override var prefersStatusBarHidden : Bool {
        return true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        displayItemInfo()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        displayPictures()
        if pictureResolution == .lowRes {
            upgradeToHighResPictures()
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // Jump to Request Item UIs
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowRequestPurchaseView" {
            let viewController = segue.destination as! RequestNavigationController
            viewController.transferItemInfo(itemId: itemInfo.id, originPrice: itemInfo.price, sellerName: itemInfo.sellerName)
        }
    }
}

// IBActions
extension ItemDetailViewController {
    @IBAction func close(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func chat(_ sender: UIButton) {
        if MFMailComposeViewController.canSendMail() {
            let composeVC = MFMailComposeViewController()
            composeVC.mailComposeDelegate = self
            composeVC.setToRecipients([itemInfo.sellerEmail!])
            composeVC.setSubject("[CollegeBuyer] Hi! I'm Interest in \(titleLabel.text!)")
            self.present(composeVC, animated: true, completion: nil)
        } else {
            self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Mail service is not avaliable"), animated: true, completion: nil)
        }
    }
    
    @IBAction func report(_ sender: UIButton) {
        self.present(UIAlertController.Factory.getInformController(message: "Report have been sent, we will review it as soon as we can.", handler: nil), animated: true, completion: nil)
    }
}

extension ItemDetailViewController {
    func imageTapped(sender: UITapGestureRecognizer) {
        let mainStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController = mainStoryboard.instantiateViewController(withIdentifier: "ItemDetailInspectPictureView") as! ItemDetailInspectPictureViewController
        
        viewController.images = itemInfo.pictures
        
        self.present(viewController, animated: true, completion: nil)
    }
}

// Helpers
extension ItemDetailViewController {
    func translateConditionToString(condition: Condition) -> String {
        switch condition {
            case .new: return "New"
            case .likeNew: return "Like New"
            case .good: return "Good"
            case .acceptable: return "Acceptable"
         }
    }
    
    func displayItemInfo() {
        self.titleLabel.text = itemInfo.name
        self.conditionLabel.text = translateConditionToString(condition: itemInfo.condition)
        self.priceLabel.text = "$\(String(itemInfo.price))"
        self.descriptionTextView.text = itemInfo.description
        self.sellerNameLabel.text = itemInfo.sellerName
        self.sellerProfilePicImageView.image = itemInfo.sellerProfilePic
        self.sellerProfilePicImageView.setNeedsDisplay()
        self.pageControl.numberOfPages = itemInfo.pictures.count
    }
    
    func displayPictures() {
        var numOfPhotosLoaded: CGFloat = 0
        for picture in itemInfo.pictures {
            let imageView = RoundCornerImageView(image: picture.roundedImage)
            imageView.roundDegree = 7
            imageView.clipsToBounds = true
            let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.imageTapped(sender:)))
            imageView.isUserInteractionEnabled = true
            imageView.addGestureRecognizer(tapGestureRecognizer)
            
            imageView.contentMode = .scaleAspectFill
            imageView.frame = CGRect(x: picturesScrollView.frame.width * numOfPhotosLoaded + imageScrollViewLeftRightMargin, y: 0 + imageScrollViewTopDownMargin, width: picturesScrollView.frame.width - 2 * imageScrollViewLeftRightMargin, height: picturesScrollView.frame.height - 2 * imageScrollViewTopDownMargin)
            
            picturesScrollView.contentSize = CGSize(width: picturesScrollView.frame.width * (numOfPhotosLoaded + 1), height: picturesScrollView.frame.height)
            picturesScrollView.addSubview(imageView)
            
            numOfPhotosLoaded += 1
        }
    }
    
    func refreshPictures() {
        for subview in self.picturesScrollView.subviews {
            subview.removeFromSuperview()
        }
        pageControl.currentPage = 0
        displayPictures()
    }
    
    func upgradeToHighResPictures() {
        DispatchQueue.global(qos: .userInitiated).async(execute: {
            self.itemInfo.pictures = ItemServices.loadItemPictures(itemId: self.itemInfo.id, resolutionType: .highRes)
            
            self.pictureResolution = .highRes
            
            DispatchQueue.main.async(execute: {
                self.refreshPictures()
            })
        })
    }
}

extension ItemDetailViewController: MFMailComposeViewControllerDelegate {
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
    }
}

extension ItemDetailViewController: UIScrollViewDelegate {
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let currentPage = Int(scrollView.contentOffset.x / scrollView.frame.width)
        pageControl.currentPage = currentPage
    }
}
