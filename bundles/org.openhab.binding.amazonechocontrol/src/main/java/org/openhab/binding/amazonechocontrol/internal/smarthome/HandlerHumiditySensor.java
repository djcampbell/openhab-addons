/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.amazonechocontrol.internal.smarthome;

import static org.openhab.binding.amazonechocontrol.internal.smarthome.Constants.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.amazonechocontrol.internal.Connection;
import org.openhab.binding.amazonechocontrol.internal.handler.SmartHomeDeviceHandler;
import org.openhab.binding.amazonechocontrol.internal.jsons.JsonSmartHomeCapabilities.SmartHomeCapability;
import org.openhab.binding.amazonechocontrol.internal.jsons.JsonSmartHomeDevices.SmartHomeDevice;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.StateDescription;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * The {@link HandlerHumiditySensor} is responsible for the Alexa.HumiditySensorInterface
 *
 * @author Daniel Campbell - Initial contribution
 */
@NonNullByDefault
public class HandlerHumiditySensor extends HandlerBase {
    // Logger
    private final Logger logger = LoggerFactory.getLogger(HandlerHumiditySensor.class);
    // Interface
    public static final String INTERFACE = "Alexa.HumiditySensor";
    // Channel definitions
    private static final ChannelInfo HUMIDITY = new ChannelInfo("Humidity" /* propertyName */ ,
            "Humidity" /* ChannelId */, CHANNEL_TYPE_HUMIDITY /* Channel Type */ ,
            ITEM_TYPE_NUMBER_HUMIDITY /* Item Type */);

    public HandlerHumiditySensor(SmartHomeDeviceHandler smartHomeDeviceHandler) {
        super(smartHomeDeviceHandler);
    }

    @Override
    public String[] getSupportedInterface() {
        return new String[] { INTERFACE };
    }

    @Override
    protected ChannelInfo @Nullable [] findChannelInfos(SmartHomeCapability capability, String property) {
        if (HUMIDITY.propertyName.equals(property)) {
            return new ChannelInfo[] { HUMIDITY };
        }
        return null;
    }

    @Override
    public void updateChannels(String interfaceName, List<JsonObject> stateList, UpdateChannelResult result) {
        for (JsonObject state : stateList) {
            Integer humidityValue = null;
            logger.debug("Updating " + interfaceName + " with state: " + state.toString());
            if (HUMIDITY.propertyName.equals(state.get("name").getAsString())) {
                JsonObject value = state.get("value").getAsJsonObject();
                if (humidityValue == null) {
                    humidityValue = value.get("value").getAsInt();
                }
                updateState(HUMIDITY.channelId,
                        humidityValue == null ? UnDefType.UNDEF : new PercentType(humidityValue));
            }
        }
    }

    @Override
    public boolean handleCommand(Connection connection, SmartHomeDevice shd, String entityId,
            List<SmartHomeCapability> capabilities, String channelId, Command command) throws IOException {
        return false;
    }

    @Override
    public @Nullable StateDescription findStateDescription(String channelId, StateDescription originalStateDescription,
            @Nullable Locale locale) {
        return null;
    }
}
