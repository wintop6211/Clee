//
//  ItemComponent.swift
//  UserEditInfoPage
//
//  Created by Yahui Liang on 3/23/18.
//  Copyright © 2018 Yahui Liang. All rights reserved.
//

import UIKit

class ItemCard: UIView {
    
    @IBOutlet var containerView: UIView!
    @IBOutlet weak var profilePictureImageView: UIImageView!
    @IBOutlet weak var itemNameLabel: UILabel!
    @IBOutlet weak var conditionLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var viewLabel: UILabel!
    
    var detailButtonHandler: (() -> ())?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required public init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        commonInit()
    }
    
    @IBAction func showMoreDetail(_ sender: RoundCornerButton) {
        guard let handler = detailButtonHandler else { return }
        handler()
    }
    
    private func commonInit() {
        Bundle.main.loadNibNamed("ItemCard", owner: self, options: nil)
        addSubview(containerView)
        containerView.frame = bounds
        containerView.autoresizingMask = [.flexibleHeight, .flexibleWidth]
        
    }
}
