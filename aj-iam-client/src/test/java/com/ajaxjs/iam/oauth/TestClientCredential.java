package com.ajaxjs.iam.oauth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestClientCredential {
    @Test
    public void encodeClient_ShouldCorrectlyEncodeCredentials() {
        // Arrange
        String clientId = "testClientId";
        String clientSecret = "testClientSecret";
        String expectedPrefix = "Basic ";
        String expectedEncoded = "dGVzdENsaWVudElkOnRlc3RDbGllbnRTZWNyZXQ="; // This is the Base64 encoding of "testClientId:testClientSecret"

        // Act
        String actualEncoded = OAuthTools.encodeClient(clientId, clientSecret);

        // Assert
        assertTrue(actualEncoded.startsWith(expectedPrefix), "The returned string should start with 'Basic '");
        String actualEncodedWithoutPrefix = actualEncoded.substring(expectedPrefix.length());
        assertEquals(expectedEncoded, actualEncodedWithoutPrefix, "The actual encoded string does not match the expected");
    }

    @Test
    void test() {
        // DO NOT SAVE Credential!!!
        String clientId = "";
        String clientSecret = "";

        String actualEncoded = OAuthTools.encodeClient(clientId, clientSecret);
        System.out.println(actualEncoded);
    }
}
