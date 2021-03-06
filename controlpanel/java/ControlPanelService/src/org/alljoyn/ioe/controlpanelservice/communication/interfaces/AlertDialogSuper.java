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

package org.alljoyn.ioe.controlpanelservice.communication.interfaces;

import java.util.Map;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.Variant;

/**
 * Super interface for dialog interfaces
 */
public interface AlertDialogSuper {

    /**
     * @return Interface version
     */
    public short getVersion() throws BusException;

    /**
     * @return States bitmask
     * @throws BusException
     */
    public int getStates() throws BusException;

    /**
     * @return Optional parameters
     * @throws BusException
     */
    public Map<Short, Variant> getOptParams() throws BusException;

    /**
     * @return Returns the dialog message
     * @throws BusException
     */
    public String getMessage() throws BusException;

    /**
     * @return Returns the number of the dialog buttons
     * @throws BusException
     */
    public short getNumActions() throws BusException;

    /**
     * Call the method if is relevant
     */
    public void Action1() throws BusException;

    /**
     * Call the method if is relevant
     */
    public void Action2() throws BusException;

    /**
     * Call the method if is relevant
     */
    public void Action3() throws BusException;

    /**
     * Signal is sent when the UI container metadata changed
     * @param metadata
     */
    public void MetadataChanged() throws BusException;
}
