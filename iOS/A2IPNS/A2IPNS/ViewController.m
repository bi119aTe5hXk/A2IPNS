//
//  ViewController.m
//  A2IPNS
//
//  Created by bi119aTe5hXk on 2017/04/21.
//  Copyright © 2017 bi119aTe5hXk. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (CIImage *)createQRForString:(NSString *)qrString {
    NSData *stringData = [qrString dataUsingEncoding: NSISOLatin1StringEncoding];
    
    CIFilter *qrFilter = [CIFilter filterWithName:@"CIQRCodeGenerator"];
    [qrFilter setValue:stringData forKey:@"inputMessage"];
    
    
    
    return qrFilter.outputImage;
}

-(IBAction)btnpressd:(id)sender{
    
    CIImage *image = [self createQRForString:self.textfield.text];
    CGAffineTransform transform = CGAffineTransformMakeScale(5.0f, 5.0f); // Scale by 5 times along both dimensions
    CIImage *output = [image imageByApplyingTransform: transform];
    
    UIImage *outputImage = [UIImage imageWithCIImage:output];
    self.QRimageview.image = outputImage;
}
@end
