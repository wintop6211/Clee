//
//  Services.swift
//  CollegeBuyer
//
//  Created by TianTuo You on 12/30/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import Foundation
import UIKit

enum ImageResolution {
    case lowRes
    case highRes
}

enum ItemServicesError: Error {
    case noMoreItem
}

class ItemServices {
    static func loadItemPictures(itemId: Int, resolutionType: ImageResolution) -> [UIImage] {
        var itemImages: [UIImage] = []
        let semaphore = DispatchSemaphore(value: 0)
        var `continue`: Bool = true
        
        var imageIndex = 0
        
        while `continue` {
            var url : String!
            var isTimeOut = true

            if resolutionType == .highRes {
                url = "/product/get/image/false/\(itemId)/\(imageIndex)"
            } else if resolutionType == .lowRes {
                url = "/product/get/image/true/\(itemId)/\(imageIndex)"
            }
            
            Server.GETService(service: url, completionHandler: {(data: Data?, response: URLResponse?, error: Error?) -> Void in
                isTimeOut = false
                    
                if data!.count != 0 {
                    imageIndex += 1
                        
                    let image = UIImage(data: data!)!
                    itemImages.append(image)
                } else {
                    `continue` = false
                }
                
                semaphore.signal()
            })
            
            _ = semaphore.wait(timeout: .distantFuture)
            
            if isTimeOut {
                itemImages.append(UIImage(named: "placeholder_image")!)
            }
        }
        
        return itemImages
    }

    static func loadItemInfo() throws -> Item? {
        let semaphore = DispatchSemaphore(value: 0)
        var itemInfo: Item?
        var noMoreItem = false

        Server.GETService(service: "/product/load", successHandler: {
            (successResponse: AnyObject) in
                
            itemInfo = parseContactableItemInfo(data: successResponse)
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in
            
            noMoreItem = true
            semaphore.signal()
        }, serverInternalErrorHandler: {
            (error: ServerError) in
            
            semaphore.signal()
        }, networkErrorHandler: {
            (error: Error) in
            
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if noMoreItem {
            throw ItemServicesError.noMoreItem
        }
        
        return itemInfo
    }

    static func loadItemInfoByCategory(category: String) throws -> Item? {
        let semaphore = DispatchSemaphore(value: 0)
        var itemInfo: Item?
        var noMoreItem = false
        
        Server.GETService(service: "/product/load/\(category)", successHandler: {
            (successResponse: AnyObject) in
                
            itemInfo = parseContactableItemInfo(data: successResponse)
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in
            
            noMoreItem = true
            semaphore.signal()
        }, serverInternalErrorHandler: {
            (error: Error) in
            
            semaphore.signal()
        }, networkErrorHandler: {
            (error: Error) in
            
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if noMoreItem {
            throw ItemServicesError.noMoreItem
        }
        
        return itemInfo
    }

    static func refreshLoading(serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.GETService(service: "/product/refresh/load", successHandler: {
            (successResponse: AnyObject) in
            
            isTimeOut = false
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in
            
            isTimeOut = false
            semaphore.signal()
        }, serverInternalErrorHandler: {
            (error: ServerError) in
            
            isTimeOut = false
            serverInternalErrorHandler(error)
            semaphore.signal()
        }, networkErrorHandler: {
            (error: Error) in
            
            isTimeOut = false
            networkErrorHandler(error)
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if isTimeOut {
            throw ServerError.timeout
        }
    }

    static func loadSellingItem() throws -> Item? {
        let semaphore = DispatchSemaphore(value: 0)
        var itemInfo: Item?
        var noMoreItem = false
        
        Server.GETService(service: "/product/load/selling", successHandler: {
            (successResponse: AnyObject) in
                
            itemInfo = parseContactableItemInfo(data: successResponse)
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in
            
            noMoreItem = true
            semaphore.signal()
        }, serverInternalErrorHandler: {
            (error: Error) in
            
            semaphore.signal()
        }, networkErrorHandler: {
            (error: Error) in
            
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if noMoreItem {
            throw ItemServicesError.noMoreItem
        }
        
        return itemInfo
    }
    
    static func refreshLoadingSellingItems(serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.GETService(service: "/product/refresh/load/selling", successHandler: {
            (successResponse: AnyObject) in
            
            isTimeOut = false
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in
            
            isTimeOut = false
            semaphore.signal()
        }, serverInternalErrorHandler: {
            (error: ServerError) in
            
            isTimeOut = false
            serverInternalErrorHandler(error)
            semaphore.signal()
        }, networkErrorHandler: {
            (error: Error) in
            
            isTimeOut = false
            networkErrorHandler(error)
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if isTimeOut {
            throw ServerError.timeout
        }
    }
    
    static func acceptOffer(offerId: Int, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.POSTService(service: "/product/request/confirm", params: ["requestId" : String(describing: offerId)], successHandler: {
            (successResponse: AnyObject) in

            isTimeOut = false
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in
            
            isTimeOut = false
            semaphore.signal()
        }, serverInternalErrorHandler: {
            (error: ServerError) in
            
            isTimeOut = false
            serverInternalErrorHandler(error)
            semaphore.signal()
        }, networkErrorHandler: {
            (error: Error) in
            
            isTimeOut = false
            networkErrorHandler(error)
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if isTimeOut {
            throw ServerError.timeout
        }
    }
    
    static func sendPurchaseRequest(itemId: Int, offerPrice: Float, note: String, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.POSTService(service: "/product/request/send", params: ["itemId" : String(describing: itemId), "offerPrice" : String(describing: offerPrice), "note" : note], successHandler: {
            (successResponse: AnyObject) in

            isTimeOut = false
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in
            
            isTimeOut = false
            semaphore.signal()
        }, serverInternalErrorHandler: {
            (error: ServerError) in
            
            isTimeOut = false
            serverInternalErrorHandler(error)
            semaphore.signal()
        }, networkErrorHandler: {
            (error: Error) in
            
            isTimeOut = false
            networkErrorHandler(error)
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if isTimeOut {
            throw ServerError.timeout
        }
    }
    
    static func postItemInfo(name: String, price: Float, condition: Condition, category: Category, description: String, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.POSTService(service: "/product/post", params: ["name" : name, "price" : "\(price)", "description": description, "condition" : "\(condition.rawValue)", "category" : "\(category.rawValue)"], successHandler: {
            (successResponse: AnyObject) in
            
            isTimeOut = false
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in
            
            isTimeOut = false
            semaphore.signal()
        }, serverInternalErrorHandler: {
            (error: ServerError) in
            
            isTimeOut = false
            serverInternalErrorHandler(error)
            semaphore.signal()
        }, networkErrorHandler: {
            (error: Error) in
            
            isTimeOut = false
            networkErrorHandler(error)
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if isTimeOut {
            throw ServerError.timeout
        }
    }
    
    static func loadPostsCoverPictures() {
        
    }
    
    static func postItemPictures(pictures: [UIImage], serverInternalErrorHandler: @escaping (Error) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        var pictureIndex = 0
        var isServerInternalErrorHappened = false
        var serverInternalError: Error!
        var isNetworkErrorHappened = false
        var networkError: Error!
        
        for picture in pictures {
                var params: [String : String] = [:]
                params["imageIndex"] = "\(pictureIndex)"
                pictureIndex += 1
            
            Server.MultipartPOSTService(service: "/product/post/image", params: params, data: UIImageJPEGRepresentation(picture, 1.0)!, name: "itemImage", mimeType: "jpeg/image", successHandler: {
                (failResponse: AnyObject) in
                
                isTimeOut = false
                semaphore.signal()
            }, failHandler: {
                (failResponse: AnyObject) in
                
                isTimeOut = false
                semaphore.signal()
            }, serverInternalErrorHandler: {
                (error: Error) in
                
                isServerInternalErrorHappened = true
                serverInternalError = error
                isTimeOut = false
                semaphore.signal()
            }, networkErrorHandler: {
                (error: Error) in
                
                isNetworkErrorHappened = true
                networkError = error
                isTimeOut = false
                semaphore.signal()
            })
        }
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if isServerInternalErrorHappened { serverInternalErrorHandler(serverInternalError) }
        if isNetworkErrorHappened { networkErrorHandler(networkError) }
    }
}

// Helper
extension ItemServices {
    fileprivate static func parseContactableItemInfo(data: AnyObject) -> Item {
        let itemInfo = data as! [String : AnyObject]
        
        let id = itemInfo["id"] as! Int
        let name = itemInfo["name"] as! String
        let condition = Condition(rawValue: itemInfo["condition"] as! Int)!
        let price = Float(itemInfo["price"] as! NSNumber)
        let description = itemInfo["description"] as! String
        
        let sellerInfo = itemInfo["seller"] as! [String : AnyObject]
        
        let sellerId = sellerInfo["id"] as! Int
        let sellerName = sellerInfo["name"] as! String
        let sellerSchool = sellerInfo["school"] as! String
        let sellerEmail = sellerInfo["email"] as! String
        
        let sellerProfilePic = UserServices.getProfilePicture(userId: sellerId)
        
        
        return Item(id: id, name: name, condition: condition, images: [], price: price, description: description, sellerId: sellerId, sellerName: sellerName, sellerSchool: sellerSchool, sellerProfilePic: sellerProfilePic, sellerEmail: sellerEmail, offers: nil)
    }
    
    fileprivate static func parseOfferedItemInfo(data: AnyObject) -> Item {
        let itemInfo = data as! [String : AnyObject]
        
        let id = itemInfo["id"] as! Int
        let name = itemInfo["name"] as! String
        let condition = Condition(rawValue: itemInfo["condition"] as! Int)!
        let price = itemInfo["price"] as! Float
        let description = itemInfo["description"] as! String
        
        let sellerInfo = itemInfo["seller"] as! [String : AnyObject]
        
        let sellerId = sellerInfo["id"] as! Int
        let sellerName = sellerInfo["name"] as! String
        let sellerSchool = sellerInfo["school"] as! String
        let sellerEmail = sellerInfo["email"] as! String
        
        let offersInfo = itemInfo["offers"] as! [[String : AnyObject]]
        
        var offers: [Offer] = []
        
        for offerInfo in offersInfo {
            let offerId = offerInfo["id"] as! Int
            let offerPrice = offerInfo["price"] as! Float
            let note = offerInfo["note"] as! String
            
            let buyer = offerInfo["buyer"] as! [String : AnyObject]
            
            let buyerId = buyer["id"] as! Int
            let buyerName = buyer["name"] as! String
            
            let buyerProfilePic = UserServices.getProfilePicture(userId: buyerId)
            
            offers.append(Offer(id: offerId, price: offerPrice, note: note, buyerName: buyerName, buyerProfilePic: buyerProfilePic))
        }
        
        let sellerProfilePic = UserServices.getProfilePicture(userId: sellerId)
        
        return Item(id: id, name: name, condition: condition, images: [], price: price, description: description, sellerId: sellerId, sellerName: sellerName, sellerSchool: sellerSchool, sellerProfilePic: sellerProfilePic, sellerEmail: sellerEmail, offers: offers)
    }
    
    fileprivate static func parseRequestInfo() {
        
    }
}
