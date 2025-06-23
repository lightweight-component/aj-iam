package com.ajaxjs.iam.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClientCredentialsTest {
    @Test
    public void encodeClient_ShouldCorrectlyEncodeCredentials() {
        // Arrange
        String clientId = "testClientId";
        String clientSecret = "testClientSecret";
        String expectedPrefix = "Basic ";
        String expectedEncoded = "dGVzdENsaWVudElkOnRlc3RDbGllbnRTZWNyZXQ="; // This is the Base64 encoding of "testClientId:testClientSecret"

        // Act
        String actualEncoded = ClientCredentials.encodeClient(clientId, clientSecret);

        // Assert
        assertTrue("The returned string should start with 'Basic '",
                actualEncoded.startsWith(expectedPrefix));
        String actualEncodedWithoutPrefix = actualEncoded.substring(expectedPrefix.length());
        assertEquals("The actual encoded string does not match the expected", expectedEncoded, actualEncodedWithoutPrefix);
    }

    @Test
    public void encodeClient_WithEmptyClientId_ShouldStillEncodeCorrectly() {
        // Arrange
        String clientId = "";
        String clientSecret = "testClientSecret";
        String expectedEncoded = "BasicOiJ0ZXN0Q2xpZW50U2VjcmV0"; // This is the Base64 encoding of ":testClientSecret"

        // Act
        String actualEncoded = ClientCredentials.encodeClient(clientId, clientSecret);

        // Assert
        assertTrue("The returned string should start with 'Basic '", actualEncoded.startsWith("Basic "));
        String actualEncodedWithoutPrefix = actualEncoded.substring("Basic ".length());
        assertEquals("The actual encoded string with empty clientId does not match the expected", expectedEncoded, actualEncodedWithoutPrefix);
    }

    @Test
    public void encodeClient_WithEmptyClientSecret_ShouldStillEncodeCorrectly() {
        // Arrange
        String clientId = "testClientId";
        String clientSecret = "";
        String expectedEncoded = "Basic dGVzdENsaWVudElkOg=="; // This is the Base64 encoding of "testClientId:"

        // Act
        String actualEncoded = ClientCredentials.encodeClient(clientId, clientSecret);

        // Assert
        assertTrue("The returned string should start with 'Basic '", actualEncoded.startsWith("Basic "));
        String actualEncodedWithoutPrefix = actualEncoded.substring("Basic ".length());
        assertEquals("The actual encoded string with empty clientSecret does not match the expected", expectedEncoded, actualEncodedWithoutPrefix);
    }

    @Test
    public void encodeClient_WithNullClientId_ShouldHandleGracefully() {
        // Arrange
        String clientId = null;
        String clientSecret = "testClientSecret";
        // Not a realistic test case, but ensures graceful handling of null clientId
        String expectedEncoded = "BasicOiJ0ZXN0Q2xpZW50U2VjcmV0"; // This is the Base64 encoding of ":testClientSecret"

        // Act
        String actualEncoded = ClientCredentials.encodeClient(clientId, clientSecret);

        // Assert
        assertTrue("The returned string should start with 'Basic '", actualEncoded.startsWith("Basic "));
        String actualEncodedWithoutPrefix = actualEncoded.substring("Basic ".length());
        assertEquals("The actual encoded string with null clientId does not match the expected", expectedEncoded, actualEncodedWithoutPrefix);
    }

    @Test
    public void encodeClient_WithNullClientSecret_ShouldHandleGracefully() {
        // Arrange
        String clientId = "testClientId";
        String clientSecret = null;
        // Not a realistic test case, but ensures graceful handling of null clientSecret
        String expectedEncoded = "Basic dGVzdENsaWVudElkOg=="; // This is the Base64 encoding of "testClientId:"

        // Act
        String actualEncoded = ClientCredentials.encodeClient(clientId, clientSecret);

        // Assert
        assertTrue("The returned string should start with 'Basic '",
                actualEncoded.startsWith("Basic "));
        String actualEncodedWithoutPrefix = actualEncoded.substring("Basic ".length());
        assertEquals("The actual encoded string with null clientSecret does not match the expected", expectedEncoded, actualEncodedWithoutPrefix);
    }
}
