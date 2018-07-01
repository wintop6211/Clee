//
//  UserServices.swift
//  CollegeBuyer
//
//  Created by TianTuo You on 12/30/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import Foundation
import UIKit

enum Gender: Int {
    case male
    case female
}

class UserServices {
    static func getProfilePicture(userId: Int) -> UIImage {
        let semaphore = DispatchSemaphore(value: 0)
        var profilePic : UIImage?
        let service = "/UserServices/getProfilePic/\(userId)"

        Server.GETService(service: service, completionHandler: {
            (data: Data?, response: URLResponse?, error: Error?) -> Void in
                
            profilePic = UIImage(data: data!)
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if profilePic == nil {
            return UIImage(named: "blank_profile_picture_placeholder")!
        }
        
        return profilePic!
    }
    
    static func login(emailAddress: String, password: String, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws -> Bool {
        let semaphore = DispatchSemaphore(value: 0)
        var isLoginCompleted = false
        var isTimeOut = true
        
        Server.POSTService(service: "/user/login", params: ["emailAddress" : emailAddress, "password" : password], successHandler: {
            (successResponse: AnyObject) in
            
            isLoginCompleted = true
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
        
        return isLoginCompleted
    }
    
    static func signUp(name: String, emailAddress: String, password: String, gender: Gender, phoneNumber: String, profilePic: UIImage, schoolName: String, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        let params = ["name" : name,
                      "emailAddress" : emailAddress,
                      "password" : password,
                      "gender" : "\(gender.rawValue)",
                      "phone" : phoneNumber,
                      "schoolName" : schoolName]
        
        let profilePicData = UIImageJPEGRepresentation(profilePic, 1)!
        
        Server.MultipartPOSTService(service: "/user/new", params: params, data: profilePicData, name: "profilePicture", mimeType: "image/jpeg", successHandler: {
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
    
    static func checkIfEmailAddressExisted(emailAddress: String, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws -> Bool {
        let semaphore = DispatchSemaphore(value: 0)
        var isEmailExisted = false
        var isTimeOut = true
        
        Server.POSTService(service: "/user/email/check", params: ["emailAddress" : emailAddress], successHandler: {
            (successResponse: AnyObject) in
            
            let response = successResponse as! String
            
            if response == "True" {
                isEmailExisted = true
            } else if response == "False" {
                isEmailExisted = false
            }
            isTimeOut = false
            
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in // Server will never response fail response
            
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
        
        return isEmailExisted
    }
    
    static func checkIfPhoneNumberExisted(phoneNumber: String, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws -> Bool {
        let semaphore = DispatchSemaphore(value: 0)
        var isPhoneNumberExisted = false
        var isTimeOut = true
        
        Server.POSTService(service: "/user/phone/check", params: ["phone" : phoneNumber], successHandler: {
            (successResponse: AnyObject) in
            
            let response = successResponse as! String
            
            if response == "True" {
                isPhoneNumberExisted = true
            } else if response == "False" {
                isPhoneNumberExisted = false
            }
            isTimeOut = false
            
            semaphore.signal()
        }, failHandler: {
            (failResponse: AnyObject) in // Server will never response fail response
            
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
        
        return isPhoneNumberExisted
    }
    
    static func update(gender: Gender, userId: Int, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.POSTService(service: "/user/change/gender", params: ["gender" : "\(gender.rawValue)"], successHandler: {
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
    
    static func update(phoneNumber: String, userId: Int, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.POSTService(service: "/user/change/phone", params: ["phone" : phoneNumber], successHandler: {
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
    
    static func update(profilePicture: UIImage, userId: Int, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        let profilePictureData = UIImagePNGRepresentation(profilePicture)!
        
        Server.MultipartPOSTService(service: "/user/image/upload", params: [ : ], data: profilePictureData, name: "profilePicture", mimeType: "image/jpeg", successHandler: {
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
    
    static func update(name: String, userId: Int, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.POSTService(service: "/user/change/name", params: ["name" : name], successHandler: {
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
    
    static func sendPasswordResetEmail(emailAddress: String, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) throws {
        let semaphore = DispatchSemaphore(value: 0)
        var isTimeOut = true
        
        Server.POSTService(service: "/user/password/forgot", params: ["emailAddress" : emailAddress], successHandler: {
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
    
    static func getSchoolLogo() -> UIImage {
        let semaphore = DispatchSemaphore(value: 0)
        var schoolLogo : UIImage?
        Server.GETService(service: "/user/get/school/image", completionHandler: {
            (data: Data?, response: URLResponse?, error: Error?) -> Void in
                
            schoolLogo = UIImage(data: data!)
            semaphore.signal()
        })
        
        _ = semaphore.wait(timeout: .distantFuture)
        
        if schoolLogo == nil {
            return UIImage(named: "")!
        }
        
        return schoolLogo!
    }
}
