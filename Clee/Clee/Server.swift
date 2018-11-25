//
//  Server.swift
//  CollegeBuyer
//
//  Created by TianTuo You on 6/25/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import Foundation
import UIKit

enum ServerError: Error {
    case otherError(Data?)
    case timeout
}

class Server {
    static let ip = "localhost"
    static let port = "8080"
    // Do not use this root if web services are local
//    static let root = "CollegeBuyerServer-1.0-SNAPSHOT"
    
    static func getSFFormatHandler(successHandler: @escaping (AnyObject) -> Void, failHandler: @escaping (AnyObject) -> Void, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) -> (Data?, URLResponse?, Error?) -> Void {
        return {
            (data: Data?, response: URLResponse?, error: Error?) -> Void in
            guard data != nil else {
                serverInternalErrorHandler(ServerError.otherError(data))
                return
            }
            
            guard let parsedJson = (try? JSONSerialization.jsonObject(with: data!, options: [])) as? [String : AnyObject] else {
                serverInternalErrorHandler(ServerError.otherError(data!))
                return
            }
            
            if error != nil {
                networkErrorHandler(error!)
            }
            
            if let successResponse = parsedJson["Success"] {
                successHandler(successResponse)
            } else if let failResponse = parsedJson["Fail"] {
                failHandler(failResponse)
            }
        }
    }

    static func GETService(service: String, completionHandler: @escaping (Data?, URLResponse?, Error?) -> Void) {
        
        let url = URL(string: "http://\(Server.ip):\(Server.port)/services\(service)")
        
        var request = URLRequest(url: url!)
        request.httpMethod = "GET"
        
        let task = URLSession(configuration: .default).dataTask(with: request, completionHandler: completionHandler)
        
        // Send request
        task.resume()
    }
    
    static func GETService(service: String, successHandler: @escaping (AnyObject) -> Void, failHandler: @escaping (AnyObject) -> Void, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) {
        
        GETService(service: service, completionHandler: getSFFormatHandler(successHandler: successHandler, failHandler: failHandler, serverInternalErrorHandler: serverInternalErrorHandler, networkErrorHandler: networkErrorHandler))
    }
    
    static func POSTService(service: String, params: [String : String], completionHandler: @escaping (Data?, URLResponse?, Error?) -> Void) {
        let url = URL(string: "http://\(Server.ip):\(Server.port)/services\(service)")
        
        var request = URLRequest(url: url!)
        request.httpMethod = "POST"
        
        let postPrametersString = generatePOSTParametersString(params: params)
        request.httpBody = postPrametersString.data(using: .utf8)
        
        let task = URLSession(configuration: .default).dataTask(with: request, completionHandler: completionHandler)
        
        // Send request
        task.resume()
    }
    
    static func POSTService(service: String, params: [String : String], successHandler: @escaping (AnyObject) -> Void, failHandler: @escaping (AnyObject) -> Void, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) {
        
        POSTService(service: service, params: params, completionHandler: getSFFormatHandler(successHandler: successHandler, failHandler: failHandler, serverInternalErrorHandler: serverInternalErrorHandler, networkErrorHandler: networkErrorHandler))
    }
    
    static func MultipartPOSTService(service: String, params: [String : String], data: Data, name: String, mimeType: String, completionHandler: @escaping (Data?, URLResponse?, Error?) -> Void) {
        let url = URL(string: "http://\(Server.ip):\(Server.port)/services\(service)")
        
        var request = URLRequest(url: url!)
        request.httpMethod = "POST"
        let boundary = "Boundary-\(NSUUID().uuidString)"
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        request.httpBody = createBody(parameters: params,
                                      boundary: boundary,
                                      data: data,
                                      mimeType: mimeType,
                                      name: name)
        
        let task = URLSession(configuration: .default).dataTask(with: request, completionHandler: completionHandler)
        
        // Send request
        task.resume()
    }
    
    static func MultipartPOSTService(service: String, params: [String : String], data: Data, name: String, mimeType: String, successHandler: @escaping (AnyObject) -> Void, failHandler: @escaping (AnyObject) -> Void, serverInternalErrorHandler: @escaping (ServerError) -> Void, networkErrorHandler: @escaping (Error) -> Void) {
        
        MultipartPOSTService(service: service, params: params, data: data, name: name, mimeType: mimeType, completionHandler: getSFFormatHandler(successHandler: successHandler, failHandler: failHandler, serverInternalErrorHandler: serverInternalErrorHandler, networkErrorHandler: networkErrorHandler))
    }
}

// Helpers
extension Server {
    static fileprivate func generatePOSTParametersString(params: [String : String]) -> String {
        var result = ""
        
        var index = 0
        for param in params {
            if index == params.count - 1 {
                result += "\(param.key)=\(param.value)"
            } else {
                result += "\(param.key)=\(param.value)&"
            }
            index += 1
        }
        
        return result
    }
    
    static fileprivate func createBody(parameters: [String : String], boundary: String, data: Data, mimeType: String, name: String) -> Data {
        let body = NSMutableData()
        let boundaryPrefix = "--\(boundary)\r\n"
        
        for (key, value) in parameters {
            body.appendString(boundaryPrefix)
            body.appendString("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
            body.appendString("\(value)\r\n")
        }
        
        body.appendString(boundaryPrefix)
        body.appendString("Content-Disposition: form-data; name=\"\(name)\";\r\n")
        body.appendString("Content-Type: \(mimeType)\r\n\r\n")
        body.append(data)
        body.appendString("\r\n")
        body.appendString("--".appending(boundary.appending("--")))
        
        return body as Data
    }
}

class NetworkConfig {
    static let requestTimeout: UInt64 = 60000
    static let optionalRequestTimeout: UInt64 = 30000
}

extension NSMutableData {
    func appendString(_ string: String) {
        guard let data = string.data(using: String.Encoding.utf8, allowLossyConversion: false) else { return }
        append(data)
    }
}
