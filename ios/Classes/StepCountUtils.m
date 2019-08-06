//
//  StepCountViewController.m
//  Runner
//
//  Created by 王帅宇 on 2019/8/6.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

#import "StepCountViewController.h"
#import <HealthKit/HealthKit.h>

@interface StepCountViewController ()
@property (nonatomic,strong) HKHealthStore *healthStore;
@end

@implementation StepCountViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
}

- (void)getStepCountWithReslut:(StepBlock) stepBlock {
    self.stepBlock = stepBlock;
    // 判断设备是否支持获取步数
    if ([HKHealthStore isHealthDataAvailable]) {
        
        self.healthStore = [[HKHealthStore alloc]init];
        
        //获取权限
        HKObjectType *setpCount = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierStepCount];
        
        NSSet *healthSet = [NSSet setWithObjects:setpCount, nil];
        
        //健康中获取权限
        [self.healthStore requestAuthorizationToShareTypes:nil readTypes:healthSet completion:^(BOOL success, NSError * _Nullable error) {
            if (success) {
                //权限获取成功 调用获取步数的方法
                [self getData];
            }else{
                NSLog(@"获取步数权限失败");
            }
        }];
        
        
    }
    
}

- (void)getData {
    
    //查询采样信息
    HKSampleType *sampleType = [HKQuantityType quantityTypeForIdentifier:HKQuantityTypeIdentifierStepCount];
    //NSSortDescriptor来告诉healthStore怎么样将结果排序
    NSSortDescriptor *start = [NSSortDescriptor sortDescriptorWithKey:HKSampleSortIdentifierStartDate ascending:NO];
    NSSortDescriptor *end = [NSSortDescriptor sortDescriptorWithKey:HKSampleSortIdentifierEndDate ascending:NO];
    //获取当前时间
    NSDate *now = [NSDate date];
    NSCalendar *calender = [NSCalendar currentCalendar];
    NSUInteger unitFlags = NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond;
    NSDateComponents *dateComponent = [calender components:unitFlags fromDate:now];
    int hour = (int)[dateComponent hour];
    int minute = (int)[dateComponent minute];
    int second = (int)[dateComponent second];
    NSDate *nowDay = [NSDate dateWithTimeIntervalSinceNow:  - (hour*3600 + minute * 60 + second) ];
    //时间结果与想象中不同是因为它显示的是0区
    NSLog(@"今天%@",nowDay);
    NSDate *nextDay = [NSDate dateWithTimeIntervalSinceNow:  - (hour*3600 + minute * 60 + second)  + 86400];
    NSLog(@"明天%@",nextDay);
    NSPredicate *predicate = [HKQuery predicateForSamplesWithStartDate:nowDay endDate:nextDay options:(HKQueryOptionNone)];
    
    /*查询的基类是HKQuery，这是一个抽象类，能够实现每一种查询目标，这里我们需要查询的步数是一个HKSample类所以对应的查询类是HKSampleQuery。下面的limit参数传1表示查询最近一条数据，查询多条数据只要设置limit的参数值就可以了*/
    
    HKSampleQuery *sampleQuery = [[HKSampleQuery alloc]initWithSampleType:sampleType predicate:predicate limit:0 sortDescriptors:@[start,end] resultsHandler:^(HKSampleQuery * _Nonnull query, NSArray<__kindof HKSample *> * _Nullable results, NSError * _Nullable error) {
        //设置一个int型变量来作为步数统计
        int allStepCount = 0;
        for (int i = 0; i < results.count; i ++) {
            //把结果转换为字符串类型
            HKQuantitySample *result = results[i];
            HKQuantity *quantity = result.quantity;
            NSMutableString *stepCount = (NSMutableString *)quantity;
            NSString *stepStr =[ NSString stringWithFormat:@"%@",stepCount];
            //获取51 count此类字符串前面的数字
            NSString *str = [stepStr componentsSeparatedByString:@" "][0];
            int stepNum = [str intValue];
            NSLog(@"%d",stepNum);
            //把一天中所有时间段中的步数加到一起
            allStepCount = allStepCount + stepNum;
            
        }
        if (self.stepBlock) {
            NSLog(@"%d",allStepCount);
            self.stepBlock([NSString stringWithFormat:@"%d",allStepCount]);
        }
    }];
   
    //执行查询
    [self.healthStore executeQuery:sampleQuery];
    
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
