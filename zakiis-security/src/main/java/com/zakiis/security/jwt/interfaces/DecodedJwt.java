package com.zakiis.security.jwt.interfaces;

public interface DecodedJwt extends Payload, Header {
    /**
     * Getter for the String Token used to create this JWT instance.
     *
     * @return the String Token.
     */
    String getToken();

    /**
     * Getter for the Header contained in the JWT as a Base64 encoded String.
     * This represents the first part of the token.
     *
     * @return the Header of the JWT.
     */
    String getHeader();

    /**
     * Getter for the Payload contained in the JWT as a Base64 encoded String.
     * This represents the second part of the token.
     *
     * @return the Payload of the JWT.
     */
    String getPayload();

    /**
     * Getter for the Signature contained in the JWT as a Base64 encoded String.
     * This represents the third part of the token.
     *
     * @return the Signature of the JWT.
     */
    String getSignature();
}
