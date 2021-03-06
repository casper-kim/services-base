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

#include "CertificateUtil.h"

#include <string>
#include <fstream>
#include <streambuf>
#include <cassert>
#include <algorithm>

using namespace qcc;
using namespace ajn;


// HACK: qcc::GetEpochTimestamp()
//
// forward declared to avoid inclusion of private core header qcc/time.h
namespace qcc {
extern uint64_t GetEpochTimestamp();
}

const size_t SERIAL_NUMBER_LENGTH = 20; // RFC 5280 4.1.2.2

/**
 * Generate random bytes and deposit them in the provided buffer.
 *
 * Copied from alljoyn/alljoyn_core/samples/secure/SampleCertificateUtility.cc
 */
static QStatus Crypto_GetRandomBytes(uint8_t* buf, const size_t count)
{
    /* Getting good randomness is highly platform-dependent. To keep this sample simple,
     * we will acquire it by generating one or more ECC keys and using the private key for
     * the randomness. */
    QStatus status = ER_OK;
    Crypto_ECC ecc;

    size_t i = 0;
    while (i < count) {
        status = ecc.GenerateDSAKeyPair();
        if (ER_OK != status) {
            return status;
        }
        const ECCPrivateKey* key = ecc.GetDSAPrivateKey();
        memcpy(buf + i, key->GetD(), std::min(count - i, key->GetDSize()));
        i += std::min(count - i, key->GetDSize());
    }

    return status;
}



QStatus CertificateUtil::GenerateIdentityCertificate(const ECCPublicKey& publicKey,
                                             const GUID128 identityGuid,
                                             const String& identityName,
                                             IdentityCertificate& cert)
{
    cert.SetAlias(identityGuid.ToString());
    cert.SetSubjectOU((const uint8_t*)identityName.c_str(), identityName.length());

    String aki;
    QStatus status = CertificateX509::GenerateAuthorityKeyId(&publicKey, aki);

    if (ER_OK == status) {
        status = GenerateCertificate(aki, &publicKey, 365 * 5, cert);
    }

    return status;
}



QStatus CertificateUtil::GenerateMembershipCertificate(const ECCPublicKey& publicKey,
                                               const GUID128& group,
                                               MembershipCertificate& cert)
{
    cert.SetGuild(group);

    String aki;
    QStatus status = CertificateX509::GenerateAuthorityKeyId(&publicKey, aki);

    if (ER_OK == status) {
        status = GenerateCertificate(aki, &publicKey, 365 * 5, cert);
    }

    return status;
}



QStatus CertificateUtil::IssueCertificate(const Crypto_ECC& issuerKeyPair,
                                  const String& issuerCN,
                                  CertificateX509& cert)
{
    std::cout << "IssueCertificate. issuer CN: " << issuerCN.c_str() << std::endl;

    cert.SetIssuerCN((const uint8_t*)issuerCN.c_str(), issuerCN.length());

    QStatus status = cert.SignAndGenerateAuthorityKeyId(issuerKeyPair.GetDSAPrivateKey(), issuerKeyPair.GetDSAPublicKey());

    if (ER_OK == status) {
        std::cout << "IssueCertificate " << cert.ToString() << std::endl;
    }

    return status;
}



QStatus CertificateUtil::GenerateCA(const Crypto_ECC& caKeyPair, const String& caCN, CertificateX509& cert)
{
    std::cout << "GenerateCA. CN: " << caCN.c_str() << std::endl;
    cert.SetCA(true);

    QStatus status = GenerateCertificate(caCN, caKeyPair.GetDSAPublicKey(), 365 * 10, cert);

    if (ER_OK == status) {
        status = IssueCertificate(caKeyPair, caCN, cert);
    }

    if (ER_OK != status) {
        std::cout << "WARNING - Failed to generate a CA: status is " << QCC_StatusText(status) << std::endl;
    }

    return status;
}



QStatus CertificateUtil::GenerateCertificate(const String& subjectCN,
                                     const ECCPublicKey* subjectPublicKey,
                                     uint64_t validDays,
                                     CertificateX509& cert)
{
    QStatus status = ER_OK;
    std::cout << "GenerateCertificate. subject CN: " << subjectCN.c_str() << std::endl;

    if (!subjectPublicKey) {
        return ER_BAD_ARG_2;
    }

    cert.SetSubjectCN((uint8_t*)subjectCN.c_str(), subjectCN.length());
    cert.SetSubjectPublicKey(subjectPublicKey);

    CertificateX509::ValidPeriod period;
    uint64_t currentTime = GetEpochTimestamp() / 1000;
    period.validFrom = currentTime;
    period.validTo = period.validFrom + 3600 * 24 * validDays;
    period.validFrom = period.validFrom - 3600;
    cert.SetValidity(&period);

    uint8_t serialNumber[SERIAL_NUMBER_LENGTH];
    status = Crypto_GetRandomBytes(serialNumber, sizeof(serialNumber));
    if (ER_OK == status) {
        serialNumber[0] &= 0x7F;
        cert.SetSerial(serialNumber, sizeof(serialNumber));
    } else {
        std::cout << "WARNING - Could not generate random serial number; status is " << QCC_StatusText(status) << std::endl;
    }

    return status;
}

QStatus CertificateUtil::SignManifest(const ECCPrivateKey* issuerKey, const CertificateX509& subjectCertificate, Manifest& manifest)
{
    return manifest->ComputeThumbprintAndSign(subjectCertificate, issuerKey);
}


bool CertificateUtil::SaveCertificate(const String& filename, CertificateX509& cert)
{
    std::ofstream fs;
    fs.open(filename, std::fstream::out | std::fstream::trunc);
    if (fs.is_open()) {
        fs << cert.GetPEM();
        fs.close();
        return true;
    }

    return false;
}

bool CertificateUtil::LoadCertificate(const String& filename, CertificateX509& cert)
{
    std::ifstream fs(filename);
    if (fs.is_open()) {
        std::string pem((std::istreambuf_iterator<char>(fs)), (std::istreambuf_iterator<char>()));
        fs.close();

        QStatus status = cert.LoadPEM(String(pem));
        return ER_OK == status;
    }

    return false;
}

bool CertificateUtil::SavePrivateKey(const String& filename, const ECCPrivateKey* privateKey)
{
    String privateKeyPem;

    if (ER_OK == CertificateX509::EncodePrivateKeyPEM(privateKey, privateKeyPem)) {
        std::ofstream fs;
        fs.open(filename, std::fstream::out | std::fstream::trunc);
        if (fs.is_open()) {
            fs << privateKeyPem;
            fs.close();
            return true;
        }
    }

    return false;
}

bool CertificateUtil::LoadPrivateKey(const String& filename, ECCPrivateKey* privateKey)
{
    std::ifstream fs(filename);
    if (fs.is_open()) {
        std::string pem((std::istreambuf_iterator<char>(fs)), (std::istreambuf_iterator<char>()));
        fs.close();

        QStatus status = CertificateX509::DecodePrivateKeyPEM(String(pem), privateKey);
        return ER_OK == status;
    }

    return false;
}
