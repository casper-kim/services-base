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

/**
 * This class is experimental, and as such has not been tested.
 * Please help make it more robust by contributing fixes if you find issues
 **/

#import <Foundation/Foundation.h>
#import "AJTMTimeServiceDateTime.h"
#import "AJTMTimeServiceServerTimerAdapter.h"

@interface AJTMTimeServiceServerTimerBase : NSObject

-(id)init;

-(void)registerAdapter:(AJTMTimeServiceServerTimerAdapter*) adapter;

/**
 * Releases object resources
 */
-(void)releaseObject;

/**
 * Returns object path of this Clock
 *
 * @return Returns object path of this Clock
 */
-(NSString*)objectPath;

/**
 * Emit the signal when the Timer is reached
 */
-(QStatus)TimerEvent;

/**
 * Emit the signal when run state changed
 */
-(QStatus)RunStateChanged:(bool) isRunning;

-(id)getHandle;
@end