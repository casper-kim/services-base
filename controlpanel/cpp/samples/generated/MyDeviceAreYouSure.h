/******************************************************************************
 * Copyright AllSeen Alliance. All rights reserved.
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

#ifndef MYDEVICEAREYOUSURE_H_
#define MYDEVICEAREYOUSURE_H_

#include <alljoyn/controlpanel/Dialog.h>

#if defined(QCC_OS_GROUP_WINDOWS)
/* Disabling warning C 4702. unreachable code */
#pragma warning(push)
#pragma warning(disable: 4702)
#endif

namespace ajn {
namespace services {

/**
 * Generated class
 */
class MyDeviceAreYouSure : public ajn::services::Dialog {
  public:
    MyDeviceAreYouSure(qcc::String name, Widget* rootWidget);
    virtual ~MyDeviceAreYouSure();

    bool executeAction1CallBack();
    bool executeAction2CallBack();
    bool executeAction3CallBack();
};
} //namespace services
} //namespace ajn

#endif /* MYDEVICEAREYOUSURE_H_ */
