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

#import "AJCPSHttpControl.h"
#import "AJCPSControlPanelDevice.h"
#import "alljoyn/services_common/AJSVCConvertUtil.h"

@interface AJCPSHttpControl ()
@property ajn::services::HttpControl *handle;
@end

@implementation AJCPSHttpControl

- (id)initWithHandle:(ajn::services::HttpControl *)handle
{
    self = [super init];
    if (self) {
        self.handle = handle;
    }
    return self;
}

- (uint16_t)getInterfaceVersion
{
    return ((ajn::services::HttpControl *)self.handle)->getInterfaceVersion();
}

/**
 * Register the HttpControl BusObject
 * @param bus - bus used for registering the object
 * @return status - success/failure
 */
- (QStatus)registerObjects:(AJNBusAttachment *)bus
{
    return ((ajn::services::HttpControl *)self.handle)->registerObjects((ajn::BusAttachment *)[bus handle]);
}

/**
 * Refresh the HttpControl
 * @param bus - bus used for refreshing the object
 * @return status - success/failure
 */
- (QStatus)refreshObjects:(AJNBusAttachment *)bus
{
    return ((ajn::services::HttpControl *)self.handle)->refreshObjects((ajn::BusAttachment *)[bus handle]);
}

/**
 * Unregister the HttpControl BusObject
 * @param bus - bus used to unregister the object
 * @return status - success/failure
 */
- (QStatus)unregisterObjects:(AJNBusAttachment *)bus
{
    return ((ajn::services::HttpControl *)self.handle)->unregisterObjects((ajn::BusAttachment *)[bus handle]);
}

/**
 * Get the Device that contains this HttpControl
 * @return ControlPanelDevice
 */
- (AJCPSControlPanelDevice *)getDevice
{
    return [[AJCPSControlPanelDevice alloc] initWithHandle:((ajn::services::HttpControl *)self.handle)->getDevice()];
}

/**
 * Get the Url for the HttpControl
 * @return url
 */
- (NSString *)getUrl
{
    return [AJSVCConvertUtil convertQCCStringtoNSString:((ajn::services::HttpControl *)self.handle)->getUrl()];
}

/**
 * Get the ControlPanelMode of this HttpControl
 * @return ControlPanelMode
 */
- (AJCPSControlPanelMode)getControlPanelMode
{
    return ((ajn::services::HttpControl *)self.handle)->getControlPanelMode();
}

@end