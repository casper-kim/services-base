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

#include "MyDeviceCurrentHumidityStringProperty.h"
#include "ControlPanelProvided.h"


namespace ajn {
namespace services {

MyDeviceCurrentHumidityStringProperty::MyDeviceCurrentHumidityStringProperty(qcc::String name, Widget* rootWidget, PropertyType propertyType) :
    Property(name, rootWidget, propertyType)
{

}

MyDeviceCurrentHumidityStringProperty::~MyDeviceCurrentHumidityStringProperty()
{

}

QStatus MyDeviceCurrentHumidityStringProperty::setValue(const char* value)
{
    setCurrentHumidityString(value);
    return ER_OK;
}

} /* namespace services */
} /* namespace ajn */