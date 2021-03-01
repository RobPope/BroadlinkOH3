/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.broadlink.internal.socket;

import java.io.IOException;
import java.net.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.broadlink.internal.BroadlinkConfiguration;
import org.openhab.binding.broadlink.internal.ThingLogger;

/**
 * @author John Marshall - Initial contribution
 */
@NonNullByDefault
public class RetryableSocket {
    @Nullable
    private DatagramSocket socket = null;
    private final ThingLogger thingLogger;
    private final BroadlinkConfiguration thingConfig;

    public RetryableSocket(BroadlinkConfiguration thingConfig, ThingLogger thingLogger) {
        this.thingConfig = thingConfig;
        this.thingLogger = thingLogger;
    }

    /**
     * Send a packet to the device, and expect a response.
     * If retries in the thingConfig is > 0, we will send
     * and receive repeatedly if we fail to get any response.
     */
    public byte[] sendAndReceive(byte message[], String purpose) throws IllegalStateException {
        try {
            return sendAndReceiveOneTime(message, purpose);
        } catch (IllegalStateException e) {
            if (thingConfig.getRetries() > 0) {
                thingLogger.logTrace("Retrying sendAndReceive (for " + purpose + ") ONE time before giving up...");
                return sendAndReceiveOneTime(message, purpose);
            }
        }
        throw new IllegalStateException("sendAndReceive: Send/Receive failure");
    }

    private byte[] sendAndReceiveOneTime(byte message[], String purpose) throws IllegalStateException {
        // To avoid the possibility of a very fast response not being heard,
        // we set up as much as possible for the response ahead of time:
        byte response[] = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(response, response.length);
        if (sendDatagram(message, purpose)) {
            receivePacket = receiveDatagram(purpose, receivePacket);
            if (receivePacket != null) {
                byte tempArray[] = new byte[receivePacket.getLength()];
                System.arraycopy(receivePacket.getData(), 0, tempArray, 0, receivePacket.getLength());
                // return receivePacket.getData();
                return tempArray;
            }
        }

        throw new IllegalStateException("sendAndReceiveOneTime: Send/Receive failure");
    }

    private boolean sendDatagram(byte message[], String purpose) {
        DatagramSocket localSocket = socket;
        try {
            thingLogger
                    .logTrace("Sending " + purpose + " to " + thingConfig.getIpAddress() + ":" + thingConfig.getPort());
            if (localSocket == null || localSocket.isClosed()) {
                thingLogger.logTrace("No existing socket ... creating");
                localSocket = new DatagramSocket();
                localSocket.setBroadcast(true);
                localSocket.setReuseAddress(true);
                localSocket.setSoTimeout(5000);
                socket = localSocket;
            }
            InetAddress host = InetAddress.getByName(thingConfig.getIpAddress());
            int port = thingConfig.getPort();
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, new InetSocketAddress(host, port));
            localSocket.send(sendPacket);
            thingLogger.logTrace("Sending " + purpose + " complete");
            return true;
        } catch (IOException e) {
            thingLogger.logError("IO error during UDP command sending " + purpose + " +: ", e);
            return false;
        }
    }

    private @Nullable DatagramPacket receiveDatagram(String purpose, DatagramPacket receivePacket) {
        thingLogger.logTrace("Awaiting " + purpose + " response");
        final DatagramSocket localSocket = socket;

        try {
            if (localSocket == null) {
                thingLogger.logError("receiveDatagram " + purpose + " for socket was unexpectedly null");
            } else {
                localSocket.receive(receivePacket);
                thingLogger.logTrace("Received " + purpose + " (" + receivePacket.getLength() + " bytes)");
                return receivePacket;
            }
        } catch (SocketTimeoutException ste) {
            thingLogger.logDebug("No further " + purpose + " response received for device");
        } catch (Exception e) {
            thingLogger.logError("While " + purpose, e);
        }

        return null;
    }

    public void close() {
        final DatagramSocket localSocket = socket;
        if (localSocket != null) {
            localSocket.close();
            socket = null;
        }
    }
}
