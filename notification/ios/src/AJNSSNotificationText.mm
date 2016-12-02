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

#import "AJNSNotificationText.h"
#import "alljoyn/services_common/AJSVCConvertUtil.h"

@interface AJNSNotificationText ()
/** cpp notificationText Handler*/
@property (nonatomic) ajn::services::NotificationText *handle;
@end

@implementation AJNSNotificationText

- (AJNSNotificationText *)initWithLang:(NSString *)language andText:(NSString *)text
{
    self = [super init];
    if (self) {
        self.handle = new ajn::services::NotificationText([AJSVCConvertUtil convertNSStringToQCCString:language], [AJSVCConvertUtil convertNSStringToQCCString:text]);
    }
    return self;
}

/**
 * Set Language for Notification
 * @param language
 */
- (void)setLanguage:(NSString *)language
{
    self.handle->setLanguage([AJSVCConvertUtil convertNSStringToQCCString:language]);
}

/**
 * Get Language for Notification
 * @return language
 */
- (NSString *)getLanguage
{
    return [AJSVCConvertUtil convertQCCStringtoNSString:self.handle->getLanguage()];
}

/**
 * Set Text for Notification
 * @param text the text to set
 */
- (void)setText:(NSString *)text
{
    self.handle->setText([AJSVCConvertUtil convertNSStringToQCCString:text]);
}

/**
 * Get Text for Notification
 * @return text
 */
- (NSString *)getText
{
    return [AJSVCConvertUtil convertQCCStringtoNSString:(self.handle->getText())];
}

@end