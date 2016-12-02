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

#include "AJTMTimeServiceTimerBusObjAdapter.h"
#import "AJNBusAttachment.h"
#import "AJNInterfaceDescription.h"


AJTMTimeServiceTimerBusObjAdapter::AJTMTimeServiceTimerBusObjAdapter(id<AJTMTimeServiceTimerBusObj> timeServiceTimerBusObj) : TimeServiceTimerBusObj("")
{
    handle = timeServiceTimerBusObj;
}

AJTMTimeServiceTimerBusObjAdapter::AJTMTimeServiceTimerBusObjAdapter(id<AJTMTimeServiceTimerBusObj> timeServiceTimerBusObj, qcc::String const& objectPath) : TimeServiceTimerBusObj(objectPath)
{
    handle = timeServiceTimerBusObj;
}

AJTMTimeServiceTimerBusObjAdapter::~AJTMTimeServiceTimerBusObjAdapter()
{
}

QStatus AJTMTimeServiceTimerBusObjAdapter::createCustomInterfaceHook(ajn::BusAttachment* bus)
{
    AJNBusAttachment *ajnBus = [[AJNBusAttachment alloc]initWithHandle:bus];
    return [handle createCustomInterfaceHook:ajnBus];
}

QStatus AJTMTimeServiceTimerBusObjAdapter::addTimerInterface(const ajn::InterfaceDescription& iface)
{
    AJNInterfaceDescription *ajnInt = [[AJNInterfaceDescription alloc]initWithHandle:(AJNHandle)&iface];
    return [handle addTimerInterface:ajnInt];
}

QStatus AJTMTimeServiceTimerBusObjAdapter::Get(const char* interfaceName, const char* propName, ajn::MsgArg& msgArg)
{
    AJNMessageArgument *ajnMsg = [[AJNMessageArgument alloc] initWithHandle:(AJNHandle)&msgArg];

    return [handle get:[NSString stringWithUTF8String:interfaceName] propName:[NSString stringWithUTF8String:propName] msgArg:ajnMsg];
}

QStatus AJTMTimeServiceTimerBusObjAdapter::Set(const char* interfaceName, const char* propName, ajn::MsgArg& msgArg)
{
    AJNMessageArgument *ajnMsg = [[AJNMessageArgument alloc] initWithHandle:(AJNHandle)&msgArg];

    return [handle set:[NSString stringWithUTF8String:interfaceName] propName:[NSString stringWithUTF8String:propName] msgArg:ajnMsg];
}