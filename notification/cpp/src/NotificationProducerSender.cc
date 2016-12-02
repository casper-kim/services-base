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

#ifdef _WIN32
/* Disable deprecation warnings */
#pragma warning(disable: 4996)
#endif

#include "NotificationProducerSender.h"
#include "Transport.h"
#include "NotificationConstants.h"
#include <alljoyn/notification/NotificationService.h>
#include <alljoyn/notification/LogModule.h>

using namespace ajn;
using namespace services;
using namespace qcc;
using namespace nsConsts;

NotificationProducerSender::NotificationProducerSender(ajn::BusAttachment* bus, QStatus& status) :
    NotificationProducer(bus, status, (qcc::String("Sender")))
{

    /**
     * Do not add code until the status that returned from the base class is verified.
     */
    if (status != ER_OK) {
        return;
    }

}

NotificationProducerSender::~NotificationProducerSender()
{
}

QStatus NotificationProducerSender::Dismiss(const char* busName, ajn::SessionId sessionId, int32_t mgsId)
{
    QCC_DbgPrintf(("NotificationProducerSender::Dismiss busName:%s sessionId:%u mgsId:%d", busName, sessionId, mgsId));

    QStatus status = ER_OK;
    if (!m_InterfaceDescription) {
        return ER_FAIL;
    }
    ProxyBusObject* proxyBusObj = new ProxyBusObject(*m_BusAttachment, busName, AJ_NOTIFICATION_PRODUCER_PATH.c_str(), sessionId);
    if (!proxyBusObj) {
        return ER_FAIL;
    }
    status = proxyBusObj->AddInterface(*m_InterfaceDescription);
    if (status != ER_OK) {
        QCC_LogError(status, ("MethodCallDismiss - AddInterface."));
        delete proxyBusObj;
        proxyBusObj = NULL;
        return status;
    }
    MsgArg args[1];
    Message replyMsg(*m_BusAttachment);

    args[0].Set(AJPARAM_INT.c_str(), mgsId);
    status = proxyBusObj->MethodCall(AJ_NOTIFICATION_PRODUCER_INTERFACE.c_str(), AJ_DISMISS_METHOD_NAME.c_str(), args, 1, replyMsg);
    if (status != ER_OK) {
        QCC_LogError(status, ("MethodCallDismiss - MethodCall."));
        delete proxyBusObj;
        proxyBusObj = NULL;
        return status;
    }

    delete proxyBusObj;
    proxyBusObj = NULL;
    return status;
}