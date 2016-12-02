/******************************************************************************
 *  * 
 *    Copyright (c) 2016 Open Connectivity Foundation and AllJoyn Open
 *    Source Project Contributors and others.
 *    
 *    All rights reserved. This program and the accompanying materials are
 *    made available under the terms of the Apache License, Version 2.0
 *    which accompanies this distribution, and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0

 ******************************************************************************/

#import <Foundation/Foundation.h>
#import "AJOBEnums.h"

@interface AJOBScanInfo : NSObject

@property(strong, readonly, nonatomic) NSString *ssid;
@property(readonly, nonatomic) AJOBAuthType authType;

- (id) initWithSSID:(NSString *)ssid AuthType:(AJOBAuthType)authType;

@end