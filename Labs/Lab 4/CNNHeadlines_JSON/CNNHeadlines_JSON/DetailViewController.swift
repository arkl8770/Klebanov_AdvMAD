//
//  DetailViewController.swift
//  CNNHeadlines_JSON
//
//  Created by Ari Klebanov on 3/4/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit
import WebKit

class DetailViewController: UIViewController, WKNavigationDelegate {

    @IBOutlet weak var webView: WKWebView!
    @IBOutlet weak var webSpinner: UIActivityIndicatorView!


    func configureView() {
        // Update the user interface for the detail item.
        if let url = detailItem {
            if url != "null" {
                loadWebPage(url)
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        configureView()
        webView.navigationDelegate = self
    }

    var detailItem: String?
    
    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        webSpinner.startAnimating()
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        webSpinner.stopAnimating()
    }
    
    func loadWebPage(_ urlString: String) {
        let url = URL(string: urlString)
        let request = URLRequest(url: url!)
        webView.load(request)
    }


}

