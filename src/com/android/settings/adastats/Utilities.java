/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modified by Android Developer Alliance (C) 2012 Added Kernel Information
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.adastats;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.SystemProperties;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.os.Build;
import android.content.ComponentName;
import android.content.Intent;


public class Utilities {

    private static final String LOG_TAG = "ADAStatsUtilities";

    public static String getUniqueID(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);

        String device_id = digest(tm.getDeviceId());
        if (device_id == null) {
            String wifiInterface = SystemProperties.get("wifi.interface");
            try {
                String wifiMac = new String(NetworkInterface.getByName(
                        wifiInterface).getHardwareAddress());
                device_id = digest(wifiMac);
            } catch (Exception e) {
                device_id = null;
            }
        }

        return device_id;
    }

    public static String getCarrier(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String carrier = tm.getNetworkOperatorName();
        if ("".equals(carrier)) {
            carrier = "Unknown";
        }
        return carrier;
    }

    public static String getCarrierId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String carrierId = tm.getNetworkOperator();
        if ("".equals(carrierId)) {
            carrierId = "0";
        }
        return carrierId;
    }

    public static String getCountryCode(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getNetworkCountryIso();
        if (countryCode.equals("")) {
            countryCode = "Unknown";
        }
        return countryCode;
    }

    public static String getDevice() {
        return SystemProperties.get("ro.product.device");
    }

    public static String getModVersion() {
        return SystemProperties.get("ro.modversion");
    }

    public static String digest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new BigInteger(1, md.digest(input.getBytes())).toString(16)
                    .toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getKernelVersion() {
        String procVersionStr1;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/version"), 256);
            try {
                int position = 0; // position of the first blank
		procVersionStr1 = reader.readLine();
		procVersionStr1 = procVersionStr1.replaceFirst("Linux", "");
		procVersionStr1 = procVersionStr1.replaceFirst("version", "");
		procVersionStr1 = procVersionStr1.trim();

		for (int i = 0; i < procVersionStr1.length(); i++)
			// Mark position of the first blank
			if (procVersionStr1.charAt(i) == ' ') {
				position = i;
				break;
			}

		char[] cutstring = new char[position];

		// Truncate everything after the first blank
		for (int j = 0; j < position; j++)
			cutstring[j] = procVersionStr1.charAt(j);

		// Remove the trailing '+' that newer kernels have
		if (cutstring[position-1] == '+')
			cutstring[position-1] = ' ';

		procVersionStr1 = new String(cutstring);
                procVersionStr1 = procVersionStr1.trim();

            } finally {
                reader.close();
            }

            return procVersionStr1;

        } catch (IOException e) {
            Log.e(LOG_TAG,
                "IO Exception when getting kernel version for Device Info screen",
                e);

            return "Unavailable";
        }

   }
}

