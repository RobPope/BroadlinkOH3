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
package org.openhab.binding.broadlink.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.Thing;
import org.slf4j.LoggerFactory;

/**
 * Multiple power socket strip device - has one control that
 * covers all ports and can measure power consumption, so
 * we extend the socket model 2 handler which covers this
 *
 * @author John Marshall/Cato Sognen - Initial contribution
 */
@NonNullByDefault
public class BroadlinkStripModel2Handler extends BroadlinkSocketModel2Handler {

    public BroadlinkStripModel2Handler(Thing thing) {
        super(thing, LoggerFactory.getLogger(BroadlinkStripModel2Handler.class), true);
    }
}
