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


package org.alljoyn.ns.transport;

import java.util.UUID;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.SignalEmitter;
import org.alljoyn.bus.SignalEmitter.GlobalBroadcast;
import org.alljoyn.bus.Status;
import org.alljoyn.ns.NotificationSender;
import org.alljoyn.ns.NotificationServiceException;
import org.alljoyn.ns.PayloadAdapter;
import org.alljoyn.ns.commons.GenericLogger;
import org.alljoyn.ns.transport.interfaces.NotificationDismisser;

/**
 * The class provides a functionality of sending Dismiss session-less-signals
 */
public class DismissEmitter implements NotificationDismisser {
    private static final String TAG = "ioe" + DismissEmitter.class.getSimpleName();

    private static final String OBJ_PATH_PREFIX = "/notificationDismisser/";

    /**
     * Send the Dismiss signal
     * @param msgId The notification id
     * @param appId The application id
     */
    public static void send(int msgId, UUID appId) {

        Transport transport = Transport.getInstance();
        BusAttachment bus = transport.getBusAttachment();

        GenericLogger logger;
        try {
            logger = transport.getLogger();
        } catch (NotificationServiceException nse) {
            System.out.println(TAG + ": Unexpected error occurred: " + nse.getMessage());
            return;
        }

        if (bus == null) {
            logger.error(TAG, "Can't call Dismiss signal, BusAttachment isn't defined, returning...");
            return;
        }

        logger.debug(TAG, "Sending the Dismiss signal notifId: '" + msgId + "', appId: '" + appId + "'");
        DismissEmitter dismissSenderBusObj = new DismissEmitter();

        String objPath = buildObjPath(msgId, appId);
        Status status = bus.registerBusObject(dismissSenderBusObj, objPath);
        if (status != Status.OK) {
            logger.error(TAG, "Failed to register a BusObject, ObjPath: '" + objPath + "', Error: '" + status + "'");
            return;
        }

        SignalEmitter emitter = new SignalEmitter(dismissSenderBusObj, GlobalBroadcast.Off);
        emitter.setSessionlessFlag(true);
        emitter.setTimeToLive(NotificationSender.MESSAGE_TTL_UL);

        byte[] bappId = PayloadAdapter.uuidToByteArray(appId);
        try {
            logger.debug(TAG, "Sending the Dismiss signal from ObjPath: '" + objPath + "'");
            emitter.getInterface(NotificationDismisser.class).dismiss(msgId, bappId);
        } catch (BusException be) {
            logger.error(TAG, "Failed to send the Dismiss signal notifId: '" + msgId + "', appId: '" + appId + "', Error: '" + be.getMessage() + "'");
        }

        bus.unregisterBusObject(dismissSenderBusObj);
    }//send

    /**
     * @see org.alljoyn.ns.transport.interfaces.NotificationDismisser#dismiss(int, byte[])
     */
    @Override
    public void dismiss(int msgId, byte[] appId) throws BusException {
    }

    /**
     * @see org.alljoyn.ns.transport.interfaces.NotificationDismisser#getVersion()
     */
    @Override
    public short getVersion() throws BusException {
        return VERSION;
    }

    /**
     * Creates the object path: OBJ_PATH_PREFIX/[APPID]/msgId
     * @param msgId
     * @param appId
     * @return The object path
     */
    private static String buildObjPath(int msgId, UUID appId) {
        String appIdStr = appId.toString().replace("-", "");
        return OBJ_PATH_PREFIX + appIdStr + "/" + Math.abs(msgId);
    }//buildObjPath

}