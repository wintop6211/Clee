//
//  StringExtensions.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 3/23/18.
//  Copyright © 2018 CollegeBuyer. All rights reserved.
//

import Foundation

extension String {
    // Check if string is empty
    var isBlank: Bool {
        get {
            let trimmed = trimmingCharacters(in: CharacterSet.whitespaces)
            return trimmed.isEmpty
        }
    }
    
    // Validate .edu email, rule - "Any amount of characters before the '@', any amount of characters after the '@' and before the '.', edu after the '.')"
    var isValidEduEmail: Bool {
        do {
            // TODO: Change validation so that only .edu email address can be accepted
            let regex = try NSRegularExpression(pattern: "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.edu", options: .caseInsensitive)
            
            return regex.firstMatch(in: self, options: NSRegularExpression.MatchingOptions(rawValue: 0), range: NSMakeRange(0, self.characters.count)) != nil
        } catch {
            return false
        }
    }
    
    // Validate phone number, rules - "Exact 10 numbers from 0 to 9"
    var isPhoneNumber: Bool {
        do {
            let regex = try NSRegularExpression(pattern: "[0-9]{10}", options: .caseInsensitive)
            
            return regex.firstMatch(in: self, options: NSRegularExpression.MatchingOptions(rawValue: 0), range: NSMakeRange(0, self.characters.count)) != nil
        } catch {
            return false
        }
    }
    
    // Validate password, rule - "At least 3 lower cased english characters, at least 1 upper cased english characters, at least 2 numbers, at least 8 characters long"
    var isValidPassword: Bool {
        do {
            let regex = try NSRegularExpression(pattern: "^(?=.*[A-Z])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8,}$", options: .caseInsensitive)
            
            if regex.firstMatch(in: self, options: NSRegularExpression.MatchingOptions(rawValue: 0), range: NSMakeRange(0, self.characters.count)) != nil {
                if self.characters.count >= 6 && self.characters.count <= 20 {
                    return true
                } else {
                    return false
                }
            } else {
                return false
            }
        } catch {
            return false
        }
    }
}
