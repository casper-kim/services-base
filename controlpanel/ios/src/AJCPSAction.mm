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

#import "AJCPSAction.h"
#import "alljoyn/controlpanel/Action.h"
#import "alljoyn/about/AJNConvertUtil.h"
#import "AJCPSControlPanelDevice.h"

@interface AJCPSAction ()

@end


@implementation AJCPSAction

- (id)initWithHandle:(ajn::services::Action *)handle
{
	self = [super initWithHandle:handle];
	if (self) {
	}
	return self;
}

- (QStatus)executeAction
{
	return ((ajn::services::Action *)self.handle)->executeAction();
}

@end