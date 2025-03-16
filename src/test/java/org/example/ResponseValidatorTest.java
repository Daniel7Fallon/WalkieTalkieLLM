package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResponseValidatorTest {

    @Test
    void testIsDenial() {
        String response = "Sorry, I can't do that.";
        assertTrue(ResponseValidator.isDenial(response));
    }

    @Test
    void testIsDenial2() {
        String response = "I'm unable to do that.";
        assertTrue(ResponseValidator.isDenial(response));
    }

    @Test
    void testIsDenial3() {
        String response = "This content is against my programming guidelines.";
        assertTrue(ResponseValidator.isDenial(response));
    }

    @Test
    void testCaseInsensitiveUppercase() {
        String response = "SORRY, I CAN'T DO THAT.";
        assertTrue(ResponseValidator.isDenial(response));
    }

    @Test
    void testCaseInsensitiveLowercase() {
        String response = "sorry i'm unable to do that";
        assertTrue(ResponseValidator.isDenial(response));
    }

    @Test
    void testIsNotDenial() {
        String response = "Sure, here are some examples.";
        assertFalse(ResponseValidator.isDenial(response));
    }

    @Test
    void testEmptyResponse() {
        String response = "";
        assertFalse(ResponseValidator.isDenial(response));
    }

    @Test
    void testNullResponse() {
        String response = null;
        assertFalse(ResponseValidator.isDenial(response));
    }
}
