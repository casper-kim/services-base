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
package org.alljoyn.onboarding.sdk;

/**
 * Holder of onboarding data: onboardee, target, and timeouts
 */
public class OnboardingConfiguration {

    /**
     * Stores the onboardee WIFI credentials.
     */
    private WiFiNetworkConfiguration onboardee = null;

    /**
     * Stores the target WIFI credentials.
     */
    private WiFiNetworkConfiguration target = null;

    /**
     * Stores the timeout of establishing WIFI connection with the onboardee in
     * msec.
     */
    private long onboardeeConnectionTimeout = OnboardingManager.DEFAULT_WIFI_CONNECTION_TIMEOUT;

    /**
     * Stores the timeout of waiting for announcement from the onboardee after
     * establishing WIFI connection with the onboardee in msec.
     */
    private long onboardeeAnnoucementTimeout = OnboardingManager.DEFAULT_ANNOUNCEMENT_TIMEOUT;

    /**
     * Stores the timeout of establishing WIFI connection with the target in
     * msec.
     */
    private long targetConnectionTimeout = OnboardingManager.DEFAULT_WIFI_CONNECTION_TIMEOUT;

    /**
     * Stores the timeout of waiting for announcement from the onboardee after
     * establishing WIFI connection with the target in msec.
     */
    private long targetAnnoucementTimeout = OnboardingManager.DEFAULT_ANNOUNCEMENT_TIMEOUT;

    /**
     * Constructor of OnboardingConfiguration that receives all parameters
     * including WIFI credentials and timeouts.
     *
     * @param onboardee
     *            {@link #onboardee}
     * @param onboardeeConnectionTimeout
     *            {@link #onboardeeConnectionTimeout}
     * @param onboardeeAnnoucementTimeout
     *            {@link #onboardeeAnnoucementTimeout}
     * @param target
     *            {@link #target}
     * @param targetConnectionTimeout
     *            {@link #targetConnectionTimeout}
     * @param targetAnnoucementTimeout
     *            {@link #targetAnnoucementTimeout}
     */
    public OnboardingConfiguration(WiFiNetworkConfiguration onboardee, long onboardeeConnectionTimeout, long onboardeeAnnoucementTimeout, WiFiNetworkConfiguration target,
            long targetConnectionTimeout, long targetAnnoucementTimeout) {
        this.onboardee = onboardee;
        this.target = target;
        this.onboardeeConnectionTimeout = onboardeeConnectionTimeout;
        this.onboardeeAnnoucementTimeout = onboardeeAnnoucementTimeout;
        this.targetConnectionTimeout = targetConnectionTimeout;
        this.targetAnnoucementTimeout = targetAnnoucementTimeout;
    }

    /**
     * Constructor of OnboardingConfiguration that receives WIFI credentials and
     * uses default timeout values
     *
     * @param onboardee
     *            {@link #onboardee}
     * @param target
     *            {@link #target}
     */
    public OnboardingConfiguration(WiFiNetworkConfiguration onboardee, WiFiNetworkConfiguration target) {
        this.onboardee = onboardee;
        this.target = target;
    }

    /**
     * Get {@link #onboardee}
     *
     * @return the onboardee of the configuration {@link #onboardee}
     */
    public WiFiNetworkConfiguration getOnboardee() {
        return onboardee;
    }


    /**
     * Get {@link #target}
     *
     * @return the target of the configuration {@link #target}
     */
    public WiFiNetworkConfiguration getTarget() {
        return target;
    }


    /**
     * Get {@link #onboardeeConnectionTimeout}
     *
     * @return the onboardee Wi-Fi timeout {@link #onboardeeConnectionTimeout}
     */
    public long getOnboardeeConnectionTimeout() {
        return onboardeeConnectionTimeout;
    }


    /**
     * Get {@link #onboardeeAnnoucementTimeout}
     *
     * @return the onboardee announcement timeout
     *         {@link #onboardeeAnnoucementTimeout}
     */
    public long getOnboardeeAnnoucementTimeout() {
        return onboardeeAnnoucementTimeout;
    }



    /**
     * Get {@link #targetConnectionTimeout}
     *
     * @return the target Wi-Fi timeout {@link #targetConnectionTimeout}
     */
    public long getTargetConnectionTimeout() {
        return targetConnectionTimeout;
    }



    /**
     * Get {@link #targetAnnoucementTimeout}
     *
     * @return the target announcement timeout {@link #targetAnnoucementTimeout}
     */
    public long getTargetAnnoucementTimeout() {
        return targetAnnoucementTimeout;
    }


}