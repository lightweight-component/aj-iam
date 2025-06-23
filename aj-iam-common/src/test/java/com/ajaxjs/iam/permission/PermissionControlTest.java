package com.ajaxjs.iam.permission;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionControlTest {
    @Test
    public void testCheck_Positive() {
        // Test with a positive scenario where the bit at the given position is set to 1
        long num = 0b100000000000000000000000000000000000000000000000000000101L; // 1 followed by 41 zeros
        int position = 0;
        boolean result = PermissionControl.check(num, position);
        assertTrue(result, "Expected true as the bit at position is set to 1");

        position = 1;
        result = PermissionControl.check(num, position);
        assertFalse(result, "Expected true as the bit at position is set to 1");

        position = 2;
        result = PermissionControl.check(num, position);
        assertTrue(result, "Expected true as the bit at position is set to 1");
    }

    @Test
    public void testCheck_InvalidPosition() {
        // Test with invalid position values (negative and >= Long.SIZE)
        long num = 1L;

        try {
            PermissionControl.check(num, -1);
            fail("Expected an IllegalArgumentException for negative position");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            PermissionControl.check(num, Long.SIZE);
            fail("Expected an IllegalArgumentException for position >= Long.SIZE");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }


    @Test
    public void testSetPermissionToTrueWhenFalse() {
        long num = 0b00000000000000000000000000000000L; // Initially no permissions
        int position = 2; // Let's say we want to set the 3rd bit (indexing starts from 0)
        long expected = 0b00000000000000000000000000000100L; // Expected value after setting the 3rd bit

        long result = PermissionControl.set(num, position, true);

        assertEquals(expected, result, "Setting permission to true when false");
    }


    @Test
    public void testRemoveBit_Success() {
        // Arrange
        long num = 15; // 1111 in binary
        int position = 2;

        // Act
        long result = PermissionControl.removeBit(num, position);

        // Assert
        assertEquals(7L, result, "Expected binary value after removing bit at position 2");
    }
}
