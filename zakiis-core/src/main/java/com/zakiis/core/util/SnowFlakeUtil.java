package com.zakiis.core.util;

/**
 * Snow flake - an id generator
 * use a Long type to represent a none repeat number, the structure is consisted by 5 part:
 * 0 - 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0 - 00000 - 00000 - 0000 0000 0000
 * part 1: 1 bit, always be zero, represent a positive number.
 * part 2: 41 bit, represents current time stamp
 * part 3: 5 bit, represents data center id
 * part 4: 5 bit, represents the worker machine id
 * part 5: 12 bit, represents an auto increment number
 * @author 10901
 */
public class SnowFlakeUtil {
	
	private final static long sequenceMask = 0xfff;
	private final static long dataCenterIdMask = 0x1f;
	private final static long workerIdMask = 0x1f;
	//initial time stamp: 2022-01-01 00:00:00
	private final static long initalTimeMillis = 1640966400000L;

	private static long dataCenterId = 0;
	private static long workerId = 0;
	private static long lastTimestamp = -1L;
	private static long sequence = 0;
	
	public static void init(long dataCenterId, long workerId) {
		if (dataCenterId < 0 || workerId < 0 || dataCenterId > dataCenterIdMask || workerId > workerIdMask) {
			throw new RuntimeException(String.format("data center id or worker id must between 0 and %d", 0, dataCenterIdMask));
		}
		SnowFlakeUtil.dataCenterId = dataCenterId;
		SnowFlakeUtil.workerId = workerId;
	}
	
	public static synchronized long generate() {
		long currentTimeMillis = System.currentTimeMillis();
		if (currentTimeMillis < lastTimestamp) {
			throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds ", lastTimestamp - currentTimeMillis));
		}
		if (currentTimeMillis == lastTimestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) { //out of sequence
				tillNextMillis(currentTimeMillis);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = currentTimeMillis;
		return ((currentTimeMillis - initalTimeMillis) << 22) | (dataCenterId << 17) | (workerId << 12) | sequence;
	}

	private static void tillNextMillis(long timeMillis) {
		long currentTimeMillis = System.currentTimeMillis();
		while (currentTimeMillis <= timeMillis) {
			Thread.yield();
			currentTimeMillis = System.currentTimeMillis();
		}
	}
}
