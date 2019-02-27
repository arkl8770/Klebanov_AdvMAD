//
//  DetailViewController.swift
//  RocknRoll_SplitView
//
//  Created by Ari Klebanov on 2/26/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit
import WebKit

class DetailViewController: UIViewController, WKNavigationDelegate {

    @IBOutlet weak var detailDescriptionLabel: UILabel!
    @IBOutlet weak var webView: WKWebView!
    @IBOutlet weak var webSpinner: UIActivityIndicatorView!
    
    func configureView() {
        // Update the user interface for the detail item.
        if let detail = detailItem {
            if let label = detailDescriptionLabel {
                label.text = detail.description
                loadWebPage(detail.description)
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        configureView()
        webView.navigationDelegate = self
    }

    var detailItem: String? {
        didSet {
            // Update the view.
            configureView()
        }
    }
    
    func loadWebPage(_ urlString: String) {
        let myUrl = URL(string: urlString)
        let request = URLRequest(url: myUrl!)
        webView.load(request)
    }
    
    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        webSpinner.startAnimating()
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        webSpinner.stopAnimating()
    }


}

