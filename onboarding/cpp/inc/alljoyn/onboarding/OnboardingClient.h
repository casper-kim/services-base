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

#ifndef ONBOARDINGCLIENT_H_
#define ONBOARDINGCLIENT_H_

#include <vector>
#include <alljoyn/Message.h>
#include <alljoyn/BusListener.h>
#include <alljoyn/BusObject.h>
#include <alljoyn/BusAttachment.h>
#include <alljoyn/MsgArg.h>
#include <qcc/String.h>
#include <alljoyn/onboarding/Onboarding.h>
#include <alljoyn/onboarding/OnboardingClientListener.h>

namespace ajn {
namespace services {

/**
 * OnboardingClient is a helper class used by an AllJoyn IoE client application to onboard/offboard
 * nearby AllJoyn IoE service application devices.
 */
class OnboardingClient {

  public:

    /**
     * Constructor of OnboardingClient
     * @param[in] bus is a reference to BusAttachment
     * @param[in] handle is a reference to the users implementation of  OnboardingClientListener
     */
    OnboardingClient(ajn::BusAttachment& bus, ajn::services::OnboardingClientListener& handle);

    /**
     * Destruct OnboardingClient
     */
    ~OnboardingClient();

    /**
     *	map of AboutData using qcc::String as key and ajn::MsgArg as value.
     */
    typedef std::vector<OBScanInfo> ScanInfos;

    /**
     * Get the ScanInfo array for specified bus name.
     * @param[in] busName Unique or well-known name of AllJoyn node to retrieve Onboarding data from.
     * @param[out] age
     * @param[out] scanInfos
     * @param[in] sessionId sessionIdthe session received  after joining alljoyn session
     * @return Status.OK if successful.
     */
    QStatus GetScanInfo(const char* busName, unsigned short& age, ScanInfos& scanInfos, ajn::SessionId sessionId = 0);


    /**
     * Configure WiFi a specified bus name.
     * @param[in] busName busName Unique or well-known name of AllJoyn node to retrieve Onboarding data from.
     * @param[in] oBInfo reference to  OBInfo
     * @param[out] resultStatus the status of the request
     * @param[in] sessionId the session received  after joining alljoyn session
     * @return Status.OK if successful.
     */
    QStatus ConfigureWiFi(const char* busName, const OBInfo& oBInfo, short& resultStatus,  ajn::SessionId sessionId = 0);

    /**
     * Connect WiFi a specified bus name.
     *
     * @param[in] busName    Unique or well-known name of AllJoyn node to retrieve Onboarding data from.
     * @param[in] sessionId the session received  after joining alljoyn session
     * @return Status.OK if successful.
     */
    QStatus ConnectTo(const char* busName, ajn::SessionId sessionId = 0);

    /**
     * Offboard WiFi a specified bus name.
     *
     * @param[in] busName   Unique or well-known name of AllJoyn node to retrieve Onboarding data from.
     * @param[in] sessionId the session received  after joining alljoyn session
     * @return Status.OK if successful.
     */
    QStatus OffboardFrom(const char* busName, ajn::SessionId sessionId = 0);
    /**
     * GetVersion retrieves the Onboarding version
     * @param[in] busName  Unique or well-known name of AllJoyn node to retrieve Onboarding data from.
     * @param[out] version
     * @param[in] sessionId the session received  after joining alljoyn session
     * @return Status.OK if successful.
     */
    QStatus GetVersion(const char* busName, int& version, ajn::SessionId sessionId = 0);


    /**
     * GetState retrieves  the last state of the WIFI connection attempt
     * @param[in] busName Unique or well-known name of AllJoyn node to retrieve Onboarding data from.
     * @param[out] state
     * @param[in] sessionId the session received  after joining alljoyn session
     * @return Status.OK if successful.
     */
    QStatus GetState(const char* busName, short& state, ajn::SessionId sessionId = 0);

    /**
     * GetLastError retrieves the last error of WIFI connection attempt
     * @param[in] busName Unique or well-known name of AllJoyn node to retrieve Onboarding data from.
     * @param[out] lastError
     * @param[in] sessionId the session received  after joining alljoyn session
     * @return Status.OK if successful.
     */
    QStatus GetLastError(const char* busName, OBLastError& lastError, ajn::SessionId sessionId = 0);

  private:
    /**
     *	pointer to  BusAttachment
     */
    ajn::BusAttachment* m_BusAttachment;
};

}
}

#endif /* ONBOARDINGCLIENT_H_ */