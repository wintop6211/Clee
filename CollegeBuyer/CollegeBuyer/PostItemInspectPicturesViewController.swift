//
//  PostItemInspectPhotosViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 10/24/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit

class PostItemInspectPicturesViewController: UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var pageControl: UIPageControl!
    
    let imageScrollViewLeftRightMargin: CGFloat = 0
    let imageScrollViewTopDownMargin: CGFloat = 0
    
    let closingGestureSensitivity = 80
    
    var images: [UIImage] = []
    override var prefersStatusBarHidden : Bool {
        return true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.pageControl.numberOfPages = images.count
        
        let panGestureRecognizer = UIPanGestureRecognizer(target: self, action: #selector(self.viewDragged(sender:)))
        self.view.addGestureRecognizer(panGestureRecognizer)
        //self.scrollView.panGestureRecognizer.require(toFail: panGestureRecognizer)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        loadPhotosInScrollView()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // IBActions
    
    @IBAction func close(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    // Handlers
    
    func viewDragged(sender: UIPanGestureRecognizer) {
        if (sender.translation(in: self.view).y > CGFloat(closingGestureSensitivity)) {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    // Helplers
    
    private func loadPhotosInScrollView() {
        var numOfPhotosLoaded: CGFloat = 0
        for picture in images {
            let imageView = UIImageView(image: picture)
            
            imageView.contentMode = .scaleAspectFit
            imageView.frame = CGRect(x: scrollView.frame.width * numOfPhotosLoaded + imageScrollViewLeftRightMargin, y: 0 + imageScrollViewTopDownMargin, width: scrollView.frame.width - 2 * imageScrollViewLeftRightMargin, height: scrollView.frame.height - 2 * imageScrollViewTopDownMargin)
            
            scrollView.contentSize = CGSize(width: scrollView.frame.width * (numOfPhotosLoaded + 1), height: scrollView.frame.height)
            scrollView.addSubview(imageView)
            
            numOfPhotosLoaded += 1
        }
    }
    
    // UIScrollViewDelegate Methods
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let currentPage = Int(scrollView.contentOffset.x / scrollView.frame.width)
        pageControl.currentPage = currentPage
    }
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
}
