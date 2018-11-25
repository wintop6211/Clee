//
//  TabViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 5/29/17.
//  Copyright © 2017 Milwaukee School of Engineering. All rights reserved.
//

import UIKit

class TabViewController: UIViewController {
    
    @IBOutlet weak var contentView: UIView!
    @IBOutlet var tabs: [UIButton]!
    @IBOutlet var tabTitles: [UILabel]!

    var viewControllers: [UIViewController?]!
    var lastSelectedTagIndex: Int = 0

    override func viewDidLoad() {
        super.viewDidLoad()


        let mainStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let itemListViewController: UIViewController = mainStoryboard.instantiateViewController(withIdentifier: "ItemListView")
        let userCenterViewController: UIViewController = mainStoryboard.instantiateViewController(withIdentifier: "UserCenterView")

        viewControllers = [itemListViewController, nil, userCenterViewController]
        
        for tab in tabs {
            tab.imageView!.contentMode = .scaleAspectFit
            if tab.tag == lastSelectedTagIndex {
                tab.isSelected = true
            }
        }
        
        for tabTitle in tabTitles {
            if tabTitle.tag == lastSelectedTagIndex {
                tabTitle.textColor = Colors.mainThemeRed
            }
        }
        
        let viewController = viewControllers[lastSelectedTagIndex]!
        addChild(viewController)
        viewController.view.frame = contentView.bounds
        contentView.addSubview(viewController.view)
        viewController.didMove(toParent: self)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func tabPressed(_ sender: UIButton) {
        guard let viewController = viewControllers[sender.tag] else { return }
        
        for tab in tabs {
            if tab.tag == lastSelectedTagIndex {
                tab.isSelected = false
            }
        }
        
        for tabTitle in tabTitles {
            if tabTitle.tag == lastSelectedTagIndex {
                tabTitle.textColor = UIColor(red: 66/255, green: 66/255, blue: 66/255, alpha: 1.0)
            }
        }
        
        let previousVC = viewControllers[lastSelectedTagIndex]!
        previousVC.willMove(toParent: nil)
        previousVC.view.removeFromSuperview()
        previousVC.removeFromParent()
        
        lastSelectedTagIndex = sender.tag
        sender.isSelected = true
        
        for tabTitle in tabTitles {
            if tabTitle.tag == sender.tag {
                tabTitle.textColor = UIColor(red: 255/255, green: 52/255, blue: 80/255, alpha: 1.0)
            }
        }
        
        addChild(viewController)
        viewController.view.frame = contentView.bounds
        contentView.addSubview(viewController.view)
        viewController.didMove(toParent: self)
    }
}
