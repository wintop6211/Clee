//
//  ItemListViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 10/28/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit
import MJRefresh

enum Category: Int {
    case book
    case beautyAndHealth
    case furniture
    case videoGame
    case electronics
    case other
}

enum BouncerState {
    case full
    case empty
}

class ItemListViewController : UIViewController {
    
    @IBOutlet weak var itemListStackView: UIStackView!
    @IBOutlet weak var itemListStackViewHeight: NSLayoutConstraint!
    @IBOutlet weak var categoryAllButton: UIButton!
    @IBOutlet weak var categoryBooksButton: UIButton!
    @IBOutlet weak var categoryBeautyButton: UIButton!
    @IBOutlet weak var categoryFurnitureButton: UIButton!
    @IBOutlet weak var categoryVideoGameButton: UIButton!
    @IBOutlet weak var categoryElectronicsButton: UIButton!
    @IBOutlet weak var itemListScrollView: UIScrollView!
    @IBOutlet weak var schoolLogoImageView: UIImageView!
    
    var items: [Item] = []
    var refreshBouncer: BouncerState = .empty
    var loadMoreBouncer: BouncerState = .empty
    var radioButtonController: SSRadioButtonsController!
    let refreshHeader = MJRefreshNormalHeader()
    let refreshFooter = MJRefreshAutoNormalFooter()
    var itemCardHeight: CGFloat!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        radioButtonController = SSRadioButtonsController(buttons: categoryAllButton, categoryBooksButton, categoryBeautyButton, categoryFurnitureButton, categoryVideoGameButton, categoryElectronicsButton)
        radioButtonController.delegate = self
        radioButtonController.pressed(categoryAllButton)
        
//        initializeMJRefresher()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        itemCardHeight = itemListStackView.bounds.width + 59
        refresh()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

// IBActions
extension ItemListViewController {
    func itemTapped(sender: TapGestureRecognizerWithItem) {
        // Get next view controller depend on screen size
        let mainStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController = mainStoryboard.instantiateViewController(withIdentifier: "ItemDetailView") as! ItemDetailViewController
        viewController.itemInfo = sender.item
        
        self.present(viewController, animated: true, completion: nil)
    }
}

// Handlers
extension ItemListViewController {
    func headerRefresh() {
        if radioButtonController.selectedButton()!.titleLabel!.text! == "All" {
            refresh()
        } else {
            var category = radioButtonController.selectedButton()!.titleLabel!.text!
            category = category.replacingOccurrences(of: " ", with: "", options: .literal, range: nil)
            refresh(category: category)
        }
        
        self.itemListScrollView.mj_header.endRefreshing()
    }
    
    func footerRefresher() {
        if radioButtonController.selectedButton()!.titleLabel!.text! == "All" {
            loadMore()
        } else {
            var category = radioButtonController.selectedButton()!.titleLabel!.text!
            category = category.replacingOccurrences(of: " ", with: "", options: .literal, range: nil)
            loadMore(category: category)
        }
        
        self.itemListScrollView.mj_footer.endRefreshing()
    }
}

// Helpers
extension ItemListViewController {
    func conditionToString(condition: Condition) -> String {
        switch condition {
        case .new: return "New"
        case .likeNew: return "Like New"
        case .good: return "Good"
        case .acceptable: return "Acceptable"
        }
    }
    
    func initializeMJRefresher() {
        refreshHeader.setRefreshingTarget(self, refreshingAction: #selector(self.headerRefresh))
        itemListScrollView.mj_header = refreshHeader
        
        refreshFooter.setRefreshingTarget(self, refreshingAction: #selector(self.footerRefresher))
        itemListScrollView.mj_footer = refreshFooter
    }
    
    func refresh() {
        DispatchQueue.global(qos: .userInitiated).async(execute: {
            if self.refreshBouncer == .empty {
                self.refreshBouncer = .full
                
                do {
                    var isErrorHappened = false
                    _ = try ItemServices.refreshLoading(serverInternalErrorHandler: {
                        _ in
                        
                        isErrorHappened = true
                    }, networkErrorHandler: {
                        _ in

                        isErrorHappened = true
                    })
                    
                    if isErrorHappened { return }
                } catch {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: nil)
                    return
                }
                
                DispatchQueue.main.sync(execute: {
                    self.clearItemList()
                })
                self.items.removeAll()
                
                let dispatchSemaphore = DispatchSemaphore(value: 1)
                
                for _ in 0..<5 {
                    DispatchQueue.global(qos: .userInitiated).async {
                        do {
                            let item = try ItemServices.loadItemInfo()!
                            
                            dispatchSemaphore.wait()
                            self.items.append(item)
                            DispatchQueue.main.sync(execute: {
                                self.displayItem(item: self.items.last!)
                            })
                            dispatchSemaphore.signal()
                        } catch { return }
                    }
                }
                
                self.refreshBouncer = .empty
            }
        })
    }
    
    func loadMore() {
//        DispatchQueue.global(qos: .userInitiated).async(execute: {
//            if self.loadMoreLock == 0 {
//                self.loadMoreLock = 1
//
//                for _ in 0..<5 {
//                    if let itemInfo = ItemServices.loadItemInfo() {
//                        self.itemInfos.append(itemInfo)
//                        DispatchQueue.main.sync(execute: {
//                            self.displayItem(itemInfo: itemInfo)
//                        })
//                    } else { break }
//                }
//
//                self.loadMoreLock = 0
//            }
//        })
    }
    
    func refresh(category: String) {
//        DispatchQueue.global(qos: .userInitiated).async(execute: {
//            if self.refreshLock == 0 {
//                self.refreshLock = 1
//
//                do {
//                    var isErrorHappened = false
//                    _ = try ItemServices.refreshLoading(serverInternalErrorHandler: {
//                        _ in
//
//                        isErrorHappened = true
//                    }, networkErrorHandler: {
//                        _ in
//
//                        isErrorHappened = true
//                    })
//
//                    if isErrorHappened { return }
//                } catch {
//                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: nil)
//                    return
//                }
//
//                DispatchQueue.main.sync(execute: {
//                    self.clearItemList()
//                })
//                self.itemInfos.removeAll()
//
//                for _ in 0..<5 {
//                    if let itemInfo = ItemServices.loadItemInfoByCategory(category: category) {
//                        self.itemInfos.append(itemInfo)
//                        DispatchQueue.main.sync(execute: {
//                            self.displayItem(itemInfo: itemInfo)
//                        })
//                    } else { break }
//                }
//
//                self.refreshLock = 0
//            }
//        })
    }
    
    func loadMore(category: String) {
//        DispatchQueue.global(qos: .userInitiated).async(execute: {
//            if self.loadMoreLock == 0 {
//                self.loadMoreLock = 1
//
//                for _ in 0..<5 {
//                    if let itemInfo = ItemServices.loadItemInfoByCategory(category: category) {
//                        self.itemInfos.append(itemInfo)
//                        DispatchQueue.main.sync(execute: {
//                            self.displayItem(itemInfo: itemInfo)
//                        })
//                    } else { break }
//                }
//
//                self.loadMoreLock = 0
//            }
//        })
    }
    
    func clearItemList() {
        for subview in itemListStackView.arrangedSubviews{
            itemListStackView.removeArrangedSubview(subview)
            subview.removeFromSuperview()
        }
        
        itemListStackViewHeight.constant = 0
    }

    func loadSellerProfilePicture(for itemCard: ItemCard, item: Item) {
        DispatchQueue.global(qos: .userInitiated).async(execute: {
            let profilePicture = UserServices.getProfilePicture(userId: item.sellerId)

            DispatchQueue.main.async(execute: {
                itemCard.profilePictureImageView.image = profilePicture
            })
        })
    }
    
    func loadingItemPictures(for itemCard: ItemCard, item: Item) {
        DispatchQueue.global(qos: .userInitiated).async(execute: {
            item.pictures = ItemServices.loadItemPictures(itemId: item.id, resolutionType: .lowRes)
            DispatchQueue.main.async(execute: {
                for picture in item.pictures {
                    itemCard.imageView.image = picture
                    break
                }
            })
        })
    }
    
    func displayItem(item: Item) {
        let itemCard = ItemCard(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
        let tapGestureRecognizer = TapGestureRecognizerWithItem(target: self, action: #selector(self.itemTapped(sender:)))
        tapGestureRecognizer.item = item
        itemCard.itemNameLabel.text = item.name
        itemCard.conditionLabel.text = "\(conditionToString(condition: item.condition))"
        itemCard.priceLabel.text = "$\(item.price)"
//        loadSellerProfilePicture(for: itemCard, item: item)
        itemCard.addConstraint(itemCard.heightAnchor.constraint(equalToConstant: CGFloat(itemCardHeight)))
        loadingItemPictures(for: itemCard, item: item)
        itemListStackViewHeight.constant += itemCardHeight
        itemCard.addGestureRecognizer(tapGestureRecognizer)
        if items.count > 1 {
            let separationLine = UIView()
            separationLine.addConstraint(separationLine.heightAnchor.constraint(equalToConstant: 0.5))
            separationLine.backgroundColor = UIColor(red: 196/255, green: 196/255, blue: 196/255, alpha: 1)
            itemListStackViewHeight.constant += 1
            itemListStackViewHeight.constant += itemListStackView.spacing * 2
            itemListStackView.addArrangedSubview(separationLine)
            itemListStackView.addArrangedSubview(itemCard)
        } else { itemListStackView.addArrangedSubview(itemCard) }
    }
}

extension ItemListViewController: SSRadioButtonControllerDelegate {
    @objc func didSelectButton(selectedButton: UIButton?) {
        if selectedButton != nil {
            if selectedButton!.titleLabel!.text! == "All" {
                refresh()
            } else {
                var categoryTitle =  selectedButton!.titleLabel!.text!
                categoryTitle = categoryTitle.replacingOccurrences(of: " ", with: "", options: .literal, range: nil)
                refresh(category: categoryTitle)
            }
        }
    }
}
