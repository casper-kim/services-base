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

#ifndef ALLJOYN_CERTIFICATEUTIL_H
#define ALLJOYN_CERTIFICATEUTIL_H

#include <qcc/CryptoECC.h>
#include <qcc/CertificateECC.h>
#include <qcc/String.h>
#include <qcc/GUID.h>
#include <alljoyn/Status.h>
#include <alljoyn/PermissionPolicy.h>

class CertificateUtil {
  public:
    static QStatus GenerateIdentityCertificate(const qcc::ECCPublicKey& publicKey,
                                               const qcc::GUID128 identityGuid,
                                               const qcc::String& identityName,
                                               qcc::IdentityCertificate& cert);

    static QStatus GenerateMembershipCertificate(const qcc::ECCPublicKey& publicKey,
                                                 const qcc::GUID128& group,
                                                 qcc::MembershipCertificate& cert);

    static QStatus GenerateCA(const qcc::Crypto_ECC& caKeyPair,
                              const qcc::String& caCN,
                              qcc::CertificateX509& cert);

    static QStatus IssueCertificate(const qcc::Crypto_ECC& issuerKeyPair,
                                    const qcc::String& issuerCN,
                                    qcc::CertificateX509& cert);

    static QStatus SignManifest(const qcc::ECCPrivateKey* issuerKey,
                                const qcc::CertificateX509& subjectCertificate,
                                ajn::Manifest& manifest);

    static bool SaveCertificate(const qcc::String& filename, qcc::CertificateX509& cert);
    static bool LoadCertificate(const qcc::String& filename, qcc::CertificateX509& cert);

    static bool SavePrivateKey(const qcc::String& filename, const qcc::ECCPrivateKey* privateKey);
    static bool LoadPrivateKey(const qcc::String& filename, qcc::ECCPrivateKey* privateKey);

  private:
    static QStatus GenerateCertificate(const qcc::String& subjectCN,
                                       const qcc::ECCPublicKey* subjectPublicKey,
                                       uint64_t validDays,
                                       qcc::CertificateX509& cert);
   
};


#endif //ALLJOYN_CERTIFICATEUTIL_H
