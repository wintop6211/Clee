//
//  PostItemViewController.swift
//  CollegeBuyer
//
//  Created by TIANTUO YOU on 10/23/17.
//  Copyright © 2017 CollegeBuyer. All rights reserved.
//

import UIKit
import AVFoundation
import CoreMotion

enum DeviceOrientation {
    case portrait
    case left
    case right
}

class PostItemViewController: UIViewController {

    @IBOutlet weak var cameraView: UIView!
    @IBOutlet weak var tookPhotoImageView: UIImageView!
    @IBOutlet weak var flashView: UIView!
    @IBOutlet weak var instructionLabel: UILabel!
    
    var captureOutput: AVCapturePhotoOutput?
    var motionManager = CMMotionManager()
    var currentOrientation = DeviceOrientation.portrait
    var images: [UIImage] = []
    override var prefersStatusBarHidden : Bool {
        return true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        setUpCameraView()
        setUpCameraOrietationMonitor()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        motionManager.stopAccelerometerUpdates()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowInspectPicturesView" {
            let controller = segue.destination as! PostItemInspectPicturesViewController
            controller.images = images
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

// IBActions
extension PostItemViewController {
    @IBAction func back(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func next(_ sender: UIButton) {
        if images.count < 3 {
            self.present(UIAlertController.Factory.getInformController(message: "Need at least 3 pictures.", handler: nil), animated: true, completion: nil)
        } else {
            let mainStoryboard = UIStoryboard(name: "Main", bundle: nil)
            let viewController = mainStoryboard.instantiateViewController(withIdentifier: "PostItemDetailView") as! PostItemDetailViewController
            
            viewController.images = images
            self.navigationController!.pushViewController(viewController, animated: true)
        }
    }
    
    @IBAction func capture(_ sender: UIButton) {
        if images.count == 5 {
            self.present(UIAlertController.Factory.getInformController(message: "Can only upload up to 5 pictures.", handler: nil), animated: true, completion: nil)
        } else {
            let settings = AVCapturePhotoSettings()
            let previewPixelType = settings.__availablePreviewPhotoPixelFormatTypes.first!
            let previewFormat = [
                kCVPixelBufferPixelFormatTypeKey as String: previewPixelType,
                kCVPixelBufferWidthKey as String: 160,
                kCVPixelBufferHeightKey as String: 160
            ]
            settings.previewPhotoFormat = previewFormat
            
            captureOutput!.capturePhoto(with: settings, delegate: self)
            
            UIView.animate(withDuration: 0.2, animations: {
                self.flashView.alpha = 1
            })
            
            UIView.animate(withDuration: 0.3, animations: {
                self.flashView.alpha = 0
            })
        }
    }
}

// Helplers
extension PostItemViewController {
    func setUpCameraView() {
        let captureSession = AVCaptureSession()
        let captureDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: AVMediaType(rawValue: convertFromAVMediaType(AVMediaType.video)), position: .back)
        do {
            let input = try AVCaptureDeviceInput(device: captureDevice!)
            if captureSession.canAddInput(input) {
                captureSession.addInput(input)
                
                captureOutput = AVCapturePhotoOutput()
                if captureSession.canAddOutput(captureOutput!) {
                    captureSession.addOutput(captureOutput!)
                    
                    captureSession.startRunning()
                    
                    (captureOutput!.connections[0] ).videoOrientation =
                        AVCaptureVideoOrientation.portrait
                    
                    let previewLayer: AVCaptureVideoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
                    
                    previewLayer.videoGravity = AVLayerVideoGravity(rawValue: convertFromAVLayerVideoGravity(AVLayerVideoGravity.resizeAspectFill))
                    previewLayer.connection!.videoOrientation = AVCaptureVideoOrientation.portrait
                    
                    cameraView.layer.addSublayer(previewLayer)
                    
                    previewLayer.position = CGPoint(x: cameraView.frame.width / 2, y: cameraView.frame.height / 2)
                    previewLayer.bounds = cameraView.frame
                }
            }
        } catch {
            self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Camera can't be initialized."), animated: true, completion: nil)
        }
    }
    
    func setUpCameraOrietationMonitor() {
        motionManager.accelerometerUpdateInterval = 0.2
        motionManager.startAccelerometerUpdates(to: OperationQueue.current!, withHandler: {(data, error) in
            if let acceleration = data?.acceleration {
                if (acceleration.x >= 0.75) {
                    self.currentOrientation = DeviceOrientation.right
                } else if (acceleration.x <= -0.75) {
                    self.currentOrientation = DeviceOrientation.left
                } else if (acceleration.y <= -0.75) {
                    self.currentOrientation = DeviceOrientation.portrait
                }
            }
        })
    }
}

extension PostItemViewController: AVCapturePhotoCaptureDelegate {
    func photoOutput(_ captureOutput: AVCapturePhotoOutput, didFinishProcessingPhoto photoSampleBuffer: CMSampleBuffer?, previewPhoto previewPhotoSampleBuffer: CMSampleBuffer?, resolvedSettings: AVCaptureResolvedPhotoSettings, bracketSettings: AVCaptureBracketedStillImageSettings?, error: Error?) {
        
        // get captured image
        // Make sure we get some photo sample buffer
        guard error == nil,
            let _ = photoSampleBuffer else {
                self.present(UIAlertController.Factory.getErrorMessageAlertController(message: "Can't Capture Picture"), animated: true, completion: nil)
                return
        }
        
        // Convert photo same buffer to a jpeg image data by using // AVCapturePhotoOutput
        guard let imageData =
            AVCapturePhotoOutput.jpegPhotoDataRepresentation(forJPEGSampleBuffer: photoSampleBuffer!, previewPhotoSampleBuffer: previewPhotoSampleBuffer) else {
                return
        }
        
        // Initialise a UIImage with our image data
        let capturedImage = UIImage.init(data: imageData , scale: 1.0)
        if var image = capturedImage {
            switch currentOrientation {
            case .left:
                image = image.fixedOrientation().imageRotatedByDegrees(degrees: -90)
                break;
            case .right:
                image = image.fixedOrientation().imageRotatedByDegrees(degrees: 90)
                break;
            case .portrait:
                image = image.fixedOrientation().imageRotatedByDegrees(degrees: 0)
                break;
            }
            
            images.append(image)
            tookPhotoImageView.image = image
            if images.count < 3 {
                instructionLabel.text = "\(images.count) of 3 Taken"
            } else {
                instructionLabel.text = "Ready to Post"
            }
        }
    }
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromAVMediaType(_ input: AVMediaType) -> String {
	return input.rawValue
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromAVLayerVideoGravity(_ input: AVLayerVideoGravity) -> String {
	return input.rawValue
}
