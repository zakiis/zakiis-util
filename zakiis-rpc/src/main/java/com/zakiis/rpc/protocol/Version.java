package com.zakiis.rpc.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.core.util.NetUtil;

import io.netty.channel.Channel;

public class Version {

    private static final Logger LOGGER = LoggerFactory.getLogger(Version.class);

    /**
     * The constant CURRENT.
     */
    private static final String CURRENT = "1.7.0-native-rc2";
    private static final String VERSION_0_7_1 = "0.7.1";
    private static final String VERSION_1_5_0 = "1.5.0";
    private static final int MAX_VERSION_DOT = 3;

    /**
     * The constant VERSION_MAP.
     */
    public static final Map<String, String> VERSION_MAP = new ConcurrentHashMap<>();

    private Version() {

    }

    /**
     * Gets current.
     *
     * @return the current
     */
    public static String getCurrent() {
        return CURRENT;
    }

    /**
     * Put channel version.
     *
     * @param c the c
     * @param v the v
     */
    public static void putChannelVersion(Channel c, String v) {
        VERSION_MAP.put(NetUtil.toStringAddress(c.remoteAddress()), v);
    }

    /**
     * Gets channel version.
     *
     * @param c the c
     * @return the channel version
     */
    public static String getChannelVersion(Channel c) {
        return VERSION_MAP.get(NetUtil.toStringAddress(c.remoteAddress()));
    }

    /**
     * Check version string.
     *
     * @param version the version
     * @throws IncompatibleVersionException the incompatible version exception
     */
    public static void checkVersion(String version) throws IncompatibleVersionException {
        long current = convertVersion(CURRENT);
        long clientVersion = convertVersion(version);
        long divideVersion = convertVersion(VERSION_0_7_1);
        if ((current > divideVersion && clientVersion < divideVersion) || (current < divideVersion && clientVersion > divideVersion)) {
            throw new IncompatibleVersionException("incompatible client version:" + version);
        }
    }

    /**
     * Determine whether the client version is greater than or equal to version 1.5.0
     *
     * @param version client version
     * @return true: client version is above or equal version 1.5.0, false: on the contrary
     */
    public static boolean isAboveOrEqualVersion150(String version) {
        boolean isAboveOrEqualVersion150 = false;
        try {
            long clientVersion = convertVersion(version);
            long divideVersion = convertVersion(VERSION_1_5_0);
            isAboveOrEqualVersion150 = clientVersion >= divideVersion;
        } catch (Exception e) {
            LOGGER.error("convert version error, clientVersion:{}", version, e);
        }
        return isAboveOrEqualVersion150;
    }

    public static long convertVersion(String version) throws IncompatibleVersionException {
        String[] parts = StringUtils.split(version, '.');

        int size = parts.length;
        if (size > MAX_VERSION_DOT + 1) {
            throw new IncompatibleVersionException("incompatible version format:" + version);
        }

        long result = 0L;
        int i = 1;
        size = MAX_VERSION_DOT + 1;
        for (String part : parts) {
            if (StringUtils.isNumeric(part)) {
                result += calculatePartValue(part, size, i);
            } else {
                String[] subParts = StringUtils.split(part, '-');
                if (StringUtils.isNumeric(subParts[0])) {
                    result += calculatePartValue(subParts[0], size, i);
                }
            }

            i++;
        }
        return result;
    }

    public static long convertVersionNotThrowException(String version) {
        try {
            return convertVersion(version);
        } catch (Exception e) {
            LOGGER.error("convert version error,version:{}",version,e);
        }
        return -1;
    }

    private static long calculatePartValue(String partNumeric, int size, int index) {
        return Long.parseLong(partNumeric) * Double.valueOf(Math.pow(100, size - index)).longValue();
    }
}
