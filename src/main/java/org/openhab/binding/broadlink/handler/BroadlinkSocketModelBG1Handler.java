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
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Smart power socket handler
 *
 * @author John Marshall/Cato Sognen - Initial contribution
 */
@NonNullByDefault
public class BroadlinkSocketModelBG1Handler extends BroadlinkSocketModel4BHandler {

    public BroadlinkSocketModelBG1Handler(Thing thing) {
        super(thing, LoggerFactory.getLogger(BroadlinkSocketModelBG1Handler.class));
    }

    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            updateItemStatus();
            return;
        }

        switch (channelUID.getId()) {
            case BroadlinkBindingConstants.CHANNEL_POWER:
                try {
                    int status = command.equals(OnOffType.ON) ? 1 : 0;
                    setStatusOnDevice(status);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case BroadlinkBindingConstants.CHANNEL_S1_POWER:
                try {
                    interpretCommandForSocket(1, command);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case BroadlinkBindingConstants.CHANNEL_S2_POWER:
                try {
                    interpretCommandForSocket(2, command);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
    }

    protected void interpretCommandForSocket(int socket, Command command) throws IOException {
        String status = command.equals(OnOffType.ON) ? "1" : "0";
        final String payload = "{\"pwr" + Integer.toString(socket) + "\":" + status + "}";

        byte message[] = buildMessage((byte) 0x6a, encodeMessage(2, payload));
        byte response[] = sendAndReceiveDatagram(message, "BG1 status byte");

        thingLogger.logInfo(response.toString());
    }

    public boolean getStatusFromDevice() {
        try {
            thingLogger.logDebug("BG1 getting status...");
            String deviceStatus = getStatusJSONFromDevice();
            JsonParser parser = new JsonParser();
            JsonObject rootObj = parser.parse(deviceStatus.trim()).getAsJsonObject();

            int pwr = rootObj.get("pwr").getAsInt();
            int pwr1 = rootObj.get("pwr1").getAsInt();
            int pwr2 = rootObj.get("pwr2").getAsInt();

            OnOffType powerStatus = pwr == 1 ? OnOffType.ON : OnOffType.OFF;
            updateState(BroadlinkBindingConstants.CHANNEL_POWER, powerStatus);

            OnOffType powerStatus1 = pwr1 == 1 ? OnOffType.ON : OnOffType.OFF;
            updateState(BroadlinkBindingConstants.CHANNEL_S1_POWER, powerStatus1);

            OnOffType powerStatus2 = pwr2 == 1 ? OnOffType.ON : OnOffType.OFF;
            updateState(BroadlinkBindingConstants.CHANNEL_S2_POWER, powerStatus2);
            return true;
        } catch (Exception ex) {
            thingLogger.logError("Exception while getting status from device", ex);
            return false;
        }
    }
}
