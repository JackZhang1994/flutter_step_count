//
//  StepCountViewController.h
//  Runner
//
//  Created by 王帅宇 on 2019/8/6.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^StepBlock)(NSString *step);

@interface StepCountUtils : NSObject

@property(nonatomic, copy) StepBlock stepBlock;
@property(nonatomic, copy) NSString *step;

- (void)getStepCountWithReslut:(StepBlock) stepBlock;
@end

NS_ASSUME_NONNULL_END
