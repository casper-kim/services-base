/******************************************************************************
 * Copyright (c) 2014, AllSeen Alliance. All rights reserved.
 *
 *    Permission to use, copy, modify, and/or distribute this software for any
 *    purpose with or without fee is hereby granted, provided that the above
 *    copyright notice and this permission notice appear in all copies.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 *    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 *    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 *    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 *    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 *    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 *    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ******************************************************************************/

#include <alljoyn/time/TimeServiceServerAuthorityClock.h>
#include <alljoyn/time/LogModule.h>
#include "TimeServiceAuthorityClockBusObj.h"

using namespace ajn;
using namespace services;

//Constructor
TimeServiceServerAuthorityClock::TimeServiceServerAuthorityClock() : TimeServiceServerClock()
{

    QCC_DbgTrace(("%s", __func__));
}

//Destructor
TimeServiceServerAuthorityClock::~TimeServiceServerAuthorityClock()
{

    QCC_DbgTrace(("%s", __func__));
}

//Emit TimeSync signal
QStatus TimeServiceServerAuthorityClock::timeSync()
{

    QCC_DbgTrace(("%s", __func__));

    TimeServiceAuthorityClockBusObj* authClock = (TimeServiceAuthorityClockBusObj*)TimeServiceServerClock::getClockBusObj();

    if (!authClock) {

        QCC_LogError(ER_FAIL, ("This TimeAuthority Clock hasn't been created yet"));
        return ER_FAIL;
    }

    return authClock->sendTimeSync();
}
