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
    
    class Items {
        
        private var items: [Item] = []
        private let serialQueue: DispatchQueue = DispatchQueue(label: "writtingQueue")

        func append(_ item: Item) {
            serialQueue.sync {
                items.append(item)
            }
        }
        
        func getItr() -> IndexingIterator<[Item]>? {
            var returnItr: IndexingIterator<[Item]>? = nil
            serialQueue.sync {
                returnItr = items.makeIterator()
            }
            return returnItr
        }
        
        func removeAll() {
            serialQueue.sync {
                items.removeAll()
            }
        }
        
        func count() -> Int {
            var count = 0
            serialQueue.sync {
                count = items.count
            }
            return count
        }
    }
    
    var items = Items()
    
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
        
        refresh(finishHandler: {_ in})
        
        initializeMJRefresher()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        itemCardHeight = itemListStackView.bounds.width + 59
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
            refresh {
                DispatchQueue.main.async {
                    self.itemListScrollView.mj_header.endRefreshing()
                }
            }
        } else {
            var category = radioButtonController.selectedButton()!.titleLabel!.text!
            category = category.replacingOccurrences(of: " ", with: "", options: .literal, range: nil)
            refresh(category: category)
        }
    }
    
    func footerRefresher() {
        if radioButtonController.selectedButton()!.titleLabel!.text! == "All" {
            loadMore {
                DispatchQueue.main.async {
                    self.itemListScrollView.mj_footer.endRefreshing()
                }
            }
        } else {
            var category = radioButtonController.selectedButton()!.titleLabel!.text!
            category = category.replacingOccurrences(of: " ", with: "", options: .literal, range: nil)
            loadMore(category: category)
        }
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
    
    func refresh(finishHandler: @escaping () -> ()) {
        // The asyc here is for making sure the UI is not freezed
        DispatchQueue.global(qos: .userInitiated).async(execute: {
            if self.refreshBouncer == .empty {
                self.refreshBouncer = .full
                // Refresh the session for storing the loading index
                var isErrorHappened = false
                do {
                    _ = try ItemServices.refreshLoading(serverInternalErrorHandler: {
                        _ in
                        
                        isErrorHappened = true
                    }, networkErrorHandler: {
                        _ in

                        isErrorHappened = true
                    })
                } catch {
                    self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Time Out"), animated: true, completion: nil)
                    isErrorHappened = true;
                }
                
                // Terminate the action if some errors happen
                if isErrorHappened {
                    finishHandler()
                    return
                }
                
                // Starts updating the UI and the item list
                
                // First, clear the item list on UI
                DispatchQueue.main.sync(execute: {
                    self.clearItemList()
                })
                
                // Remove all items from the item information array
                self.items.removeAll()
                
                // Load item info from the server
                // Parse the information and display the information on UI
                let semaphore = DispatchSemaphore(value: 0)
                var tmpCounter = 0
                let itemNum = 5
                for _ in 0..<itemNum {
                    // Send the request, and handle each item independently
                    ItemServices.loadItemInfo(itemHandler: { (_ item: Item) in
                        // Move the update UI action to the main thread
                        DispatchQueue.main.sync(execute: {
                            self.items.append(item)
                            self.displayItem(item: item)
                            tmpCounter += 1
                            if (tmpCounter == itemNum) {
                                // If all item has been received
                                // Release the lock
                                semaphore.signal()
                            }
                        })
                    }, noItemHandler: {
                        _ in
                        semaphore.signal()
                    }, errorHandler: {
                        _ in
                        semaphore.signal()
                    })
                }
                
                // Keep waiting untile the item batch has been received
                semaphore.wait()
                finishHandler()
                self.refreshBouncer = .empty
            }
        })
    }
    
    func loadMore(finishHandler: @escaping () -> ()) {
        DispatchQueue.global(qos: .userInitiated).async {
            let semaphore = DispatchSemaphore(value: 0)
            var tmpCounter = 0
            let itemNum = 5
            for _ in 0..<itemNum {
                ItemServices.loadItemInfo(itemHandler: { (_ item: Item) in
                    DispatchQueue.main.async(execute: {
                        self.items.append(item)
                        self.displayItem(item: item)
                        tmpCounter += 1
                        if (tmpCounter == itemNum) {
                            semaphore.signal()
                        }
                    })
                }, noItemHandler: {
                    _ in
                    semaphore.signal()
                }, errorHandler: {
                    _ in
                    semaphore.signal()
                })
            }
            
            semaphore.wait()
            finishHandler()
        }
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
        itemCard.viewLabel.text = "\(item.views) views"
        loadSellerProfilePicture(for: itemCard, item: item)
        itemCard.addConstraint(itemCard.heightAnchor.constraint(equalToConstant: CGFloat(itemCardHeight)))
        loadingItemPictures(for: itemCard, item: item)
        itemListStackViewHeight.constant += itemCardHeight
        itemCard.addGestureRecognizer(tapGestureRecognizer)
        if items.count() > 1 {
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
                refresh {
                    self.itemListScrollView.mj_header.endRefreshing()
                }
            } else {
                var categoryTitle =  selectedButton!.titleLabel!.text!
                categoryTitle = categoryTitle.replacingOccurrences(of: " ", with: "", options: .literal, range: nil)
                refresh(category: categoryTitle)
            }
        }
    }
}
