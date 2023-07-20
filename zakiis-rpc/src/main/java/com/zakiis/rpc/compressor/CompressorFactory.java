package com.zakiis.rpc.compressor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import com.zakiis.core.loader.EnhancedServiceLoader;
import com.zakiis.core.loader.LoadLevel;


public class CompressorFactory {

    /**
     * The constant COMPRESSOR_MAP.
     */
    protected static final Map<CompressorType, Compressor> COMPRESSOR_MAP = new ConcurrentHashMap<>();

    static {
        COMPRESSOR_MAP.put(CompressorType.NONE, new NoneCompressor());
    }

    /**
     * Get compressor by code.
     *
     * @param code the code
     * @return the compressor
     */
    public static Compressor getCompressor(byte code) {
        CompressorType type = CompressorType.getByCode(code);
		return COMPRESSOR_MAP.computeIfAbsent(type, key -> EnhancedServiceLoader.load(Compressor.class, type.name()));
        
    }

    /**
     * None compressor
     */
    @LoadLevel(name = "NONE")
    public static class NoneCompressor implements Compressor {
        @Override
        public byte[] compress(byte[] bytes) {
            return bytes;
        }

        @Override
        public byte[] decompress(byte[] bytes) {
            return bytes;
        }
    }

}