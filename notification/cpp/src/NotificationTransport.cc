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

#include <alljoyn/notification/NotificationService.h>
#include "NotificationTransport.h"
#include "NotificationConstants.h"
#include <alljoyn/notification/LogModule.h>

using namespace ajn;
using namespace services;
using namespace nsConsts;
using namespace qcc;

NotificationTransport::NotificationTransport(ajn::BusAttachment* bus,
                                             qcc::String const& servicePath, QStatus& status, String const& interfaceName) :
    BusObject(servicePath.c_str()), m_SignalMethod(0)
{
    InterfaceDescription* intf = NULL;
    status = bus->CreateInterface(interfaceName.c_str(), intf);

    if (status == ER_OK) {
        intf->AddSignal(AJ_SIGNAL_METHOD.c_str(), AJ_NOTIFY_PARAMS.c_str(), AJ_NOTIFY_PARAM_NAMES.c_str(), 0);
        intf->AddProperty(AJ_PROPERTY_VERSION.c_str(), AJPARAM_UINT16.c_str(), PROP_ACCESS_READ);
        intf->Activate();
    } else if (status == ER_BUS_IFACE_ALREADY_EXISTS) {
        intf = (InterfaceDescription*) bus->GetInterface(interfaceName.c_str());
        if (!intf) {
            status = ER_BUS_UNKNOWN_INTERFACE;
            QCC_LogError(status, ("Could not get interface"));
            return;
        }
    } else {
        QCC_LogError(status, ("Could not create interface"));
        return;
    }

    status = AddInterface(*intf, ANNOUNCED);
    if (status != ER_OK) {
        QCC_LogError(status, ("Could not add interface"));
        return;
    }

    // Get the signal method for future use
    m_SignalMethod = intf->GetMember(AJ_SIGNAL_METHOD.c_str());
}

NotificationTransport::~NotificationTransport()
{

}

QStatus NotificationTransport::Get(const char* ifcName, const char* propName, MsgArg& val)
{
    QCC_DbgTrace(("Get property was called"));

    if (0 != strcmp(AJ_PROPERTY_VERSION.c_str(), propName)) {
        QCC_LogError(ER_BUS_NO_SUCH_PROPERTY, ("Called for property different than version."));
        return ER_BUS_NO_SUCH_PROPERTY;
    }

    val.typeId = ALLJOYN_UINT16;
    val.v_uint16 = NotificationService::getVersion();
    return ER_OK;
}

QStatus NotificationTransport::Set(const char* ifcName, const char* propName, MsgArg& val)
{
    return ER_ALLJOYN_ACCESS_PERMISSION_ERROR;
}