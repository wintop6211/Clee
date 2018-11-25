//
//  AppDelegate.swift
//  CollegeBuyer
//
//  Created by Yahui Liang on 6/10/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    /*func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let tokenParts = deviceToken.map{ data -> String in
            return String.init(format: "%02.2hhx", data)
        }
        let token = tokenParts.joined()
        print("Device Token: \(token)")
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        
        print("Failed to regsiter: \(error)")
    }*/
    
    
    
    // Helpers
    
    func registerForPushNotifications() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge], completionHandler: {
            (granted, error) in
            
            guard granted else { return }
            self.getNotificationSettings()
        })
    }
    
    func getNotificationSettings() {
        UNUserNotificationCenter.current().getNotificationSettings(completionHandler: {
            (settings) in
            
            print("Notifcation settings: \(settings)")
            guard settings.authorizationStatus == .authorized else { return }
            UIApplication.shared.registerForRemoteNotifications()
        })
    }
    
    // Can also be used for iPhone 5s, SE
    class func isIPhone5TypeScreen () -> Bool{
        return max(UIScreen.main.bounds.width, UIScreen.main.bounds.height) == 568.0
    }
    
    // Can also be used for iPhone 6s, 7
    class func isIPhone6TypeScreen () -> Bool {
        return max(UIScreen.main.bounds.width, UIScreen.main.bounds.height) == 667.0
    }
    
    // Can also be used for iPhone 6s Plus, 7 Plus
    class func isIPhone6PlusTypeScreen () -> Bool {
        return max(UIScreen.main.bounds.width, UIScreen.main.bounds.height) == 736.0
    }
}

