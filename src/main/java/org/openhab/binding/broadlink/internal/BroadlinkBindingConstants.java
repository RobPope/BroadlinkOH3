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
package org.openhab.binding.broadlink.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link BroadlinkBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author John Marshall - Initial contribution
 */
@NonNullByDefault
public class BroadlinkBindingConstants {

    private static final String BINDING_ID = "broadlink";

    public static final String BROADLINK_AUTH_KEY = "097628343fe99e23765c1513accf8b02";
    public static final String BROADLINK_IV = "562e17996d093d28ddb3ba695a2e6f58";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_RM = new ThingTypeUID(BINDING_ID, "rm");
    public static final ThingTypeUID THING_TYPE_RM2 = new ThingTypeUID(BINDING_ID, "rm2");
    public static final ThingTypeUID THING_TYPE_RM3 = new ThingTypeUID(BINDING_ID, "rm3");
    public static final ThingTypeUID THING_TYPE_RM3Q = new ThingTypeUID(BINDING_ID, "rm3q");
    public static final ThingTypeUID THING_TYPE_RM4 = new ThingTypeUID(BINDING_ID, "rm4");
    public static final ThingTypeUID THING_TYPE_A1 = new ThingTypeUID(BINDING_ID, "a1");
    public static final ThingTypeUID THING_TYPE_MP1 = new ThingTypeUID(BINDING_ID, "mp1");
    public static final ThingTypeUID THING_TYPE_MP1_1K3S2U = new ThingTypeUID(BINDING_ID, "mp1_1k3s2u");
    public static final ThingTypeUID THING_TYPE_MP2 = new ThingTypeUID(BINDING_ID, "mp2");
    public static final ThingTypeUID THING_TYPE_SP1 = new ThingTypeUID(BINDING_ID, "sp1");
    public static final ThingTypeUID THING_TYPE_SP2 = new ThingTypeUID(BINDING_ID, "sp2");
    public static final ThingTypeUID THING_TYPE_SP3 = new ThingTypeUID(BINDING_ID, "sp3");
    public static final ThingTypeUID THING_TYPE_SP3S = new ThingTypeUID(BINDING_ID, "sp3s");
    public static final ThingTypeUID THING_TYPE_SP4B = new ThingTypeUID(BINDING_ID, "sp4b");
    public static final ThingTypeUID THING_TYPE_BG1 = new ThingTypeUID(BINDING_ID, "bg1");
    public static final ThingTypeUID THING_TYPE_U01 = new ThingTypeUID(BINDING_ID, "u01");

    public static final Map<ThingTypeUID, String> SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP = new HashMap<ThingTypeUID, String>();

    static {
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_RM, "Broadlink RM");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_RM2, "Broadlink RM2");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_RM3, "Broadlink RM3");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_RM3Q, "Broadlink RM3 v11057");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_RM4, "Broadlink RM4 / RM4 Mini / RM4 Pro");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_A1, "Broadlink A1");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_MP1, "Broadlink MP1");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_MP1_1K3S2U, "Broadlink MP1 1K3S2U");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_MP2, "Broadlink MP2");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_SP1, "Broadlink SP1");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_SP2, "Broadlink SP2");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_SP3, "Broadlink SP3");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_SP3S, "Broadlink SP3s");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_SP4B, "Broadlink SP4b");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_BG1, "British General BG1");
        SUPPORTED_THING_TYPES_UIDS_TO_NAME_MAP.put(THING_TYPE_U01, "British General U01");
    }

    // List of all Channel ids
    public static final String CHANNEL_TEMPERATURE = "temperature";
    public static final String CHANNEL_HUMIDITY = "humidity";
    public static final String CHANNEL_LIGHT_LEVEL = "light";
    public static final String CHANNEL_AIR_QUALITY = "air";
    public static final String CHANNEL_NOISE_LEVEL = "noise";
    public static final String CHANNEL_POWER_CONSUMPTION = "powerConsumption";

    public static final String CHANNEL_COMMAND = "command";

    public static final String CHANNEL_POWER = "powerOn";
    public static final String CHANNEL_NIGHTLIGHT_POWER = "nightLight";
    public static final String CHANNEL_S1_POWER = "s1powerOn";
    public static final String CHANNEL_S2_POWER = "s2powerOn";
    public static final String CHANNEL_S3_POWER = "s3powerOn";
    public static final String CHANNEL_S4_POWER = "s4powerOn";
    public static final String CHANNEL_USB_POWER = "usbPowerOn";
}
