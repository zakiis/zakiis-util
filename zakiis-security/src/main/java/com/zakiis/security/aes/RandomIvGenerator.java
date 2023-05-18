package com.zakiis.security.aes;

import java.security.SecureRandom;

import lombok.SneakyThrows;

public class RandomIvGenerator {
	
	private static final String DEFAULT_SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
	private final SecureRandom random;
	
	@SneakyThrows
	public RandomIvGenerator() {
		random = SecureRandom.getInstance(DEFAULT_SECURE_RANDOM_ALGORITHM);
    }
	
	@SneakyThrows
	public RandomIvGenerator(final String secureRandomAlgorithm) {
		random = SecureRandom.getInstance(secureRandomAlgorithm);
	}
	
	public byte[] generateIv(final int lengthBytes) {
        final byte[] iv = new byte[lengthBytes];
        synchronized (this.random) {
            this.random.nextBytes(iv);
        }
        return iv;
    }
}
