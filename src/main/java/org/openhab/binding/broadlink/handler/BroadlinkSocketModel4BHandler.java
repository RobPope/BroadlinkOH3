/* Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.broadlink.handler;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.broadlink.internal.BroadlinkBindingConstants;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Thing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Smart power socket handler
 *
 * @author John Marshall/Cato Sognen - Initial contribution
 */
@NonNullByDefault
public class BroadlinkSocketModel4BHandler extends BroadlinkSocketHandler {

    public BroadlinkSocketModel4BHandler(Thing thing) {
        super(thing, LoggerFactory.getLogger(BroadlinkSocketModel4BHandler.class));
    }

    public BroadlinkSocketModel4BHandler(Thing thing, Logger logger) {
        super(thing, logger);
    }

    public boolean getStatusFromDevice() {
        try {
            thingLogger.logDebug("BG1 getting status...");
            String deviceStatus = getStatusJSONFromDevice();
            JsonParser parser = new JsonParser();
            JsonObject rootObj = parser.parse(deviceStatus.trim()).getAsJsonObject();

            int pwr = rootObj.get("pwr").getAsInt();

            OnOffType powerStatus = pwr == 1 ? OnOffType.ON : OnOffType.OFF;
            updateState(BroadlinkBindingConstants.CHANNEL_POWER, powerStatus);
            return true;
        } catch (Exception ex) {
            thingLogger.logError("Exception while getting status from device", ex);
            return false;
        }
    }

    protected String getStatusJSONFromDevice() throws IOException {
        String payload = "{}";
        byte message[] = buildMessage((byte) 0x6a, encodeMessage(1, payload));
        byte response[] = sendAndReceiveDatagram(message, "BG1 status byte");
        byte decryptedResponse[] = decodeDevicePacket(response);
        return decodeMessage(decryptedResponse);
    }

    protected byte[] encodeMessage(int flag, String json) {
        // int length = 12 + json.length();
        // byte[] packet = new byte[14 + json.length()];
        // byte[] packet = new byte[32];
        int length = 14 + json.length();
        if (length % 16 != 0) {
            length = length + (16 - (length % 16));
        }
        byte[] packet = new byte[length];

        packet[0x00] = (byte) (length & 0xFF);
        packet[0x01] = (byte) ((length >> 8) & 0xFF);
        packet[0x02] = (byte) (0xa5a5 & 0xFF);
        packet[0x03] = (byte) ((0xa5a5 >> 8) & 0xFF);
        packet[0x04] = 0x5a5a & 0xFF;
        packet[0x05] = ((0x5a5a >> 8) & 0xFF);
        packet[0x06] = 0x00;
        packet[0x07] = 0x00;
        packet[0x08] = (byte) flag;
        packet[0x09] = 0x0B;
        packet[0x0a] = (byte) (json.length() & 0xFF);
        packet[0x0b] = (byte) ((json.length() >> 8) & 0xFF);
        packet[0x0c] = (byte) ((json.length() >> 16) & 0xFF);
        packet[0x0d] = (byte) ((json.length() >> 24) & 0xFF);
        for (int i = 0; i < json.length(); i++) {
            packet[i + 14] = (byte) json.charAt(i);
        }
        int checksum = 0xc0ad;
        for (int i = 0x08; i < packet.length; i++) {
            checksum = (checksum + packet[i]) & 0xffff;
        }
        packet[0x06] = (byte) (checksum & 0xff);
        packet[0x07] = (byte) (checksum >> 8);

        return packet;
    }

    protected String decodeMessage(byte[] message) {
        int err = message[0x22] | (message[0x23] << 8);
        err = 0;
        String status = "";
        if (err != 0) {
            status = "{}";
            return status;
        } else {
            for (int i = 0x0e; i < message.length; i++) {
                status += (char) message[i];
            }
            return status;

        }
    }

    protected void setStatusOnDevice(int status) throws IOException {
        final String payload = "{\"pwr\":" + status + "}";

        byte message[] = buildMessage((byte) 0x6a, encodeMessage(2, payload));
        byte response[] = sendAndReceiveDatagram(message, "BG1 status byte");

        thingLogger.logInfo(response.toString());
    }
}
