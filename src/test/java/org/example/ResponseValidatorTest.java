package org.example;

import org.example.Completion.ResponseValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResponseValidatorTest {

    @Test
    void testIsDenial() {
        String response = "This content is against my programming guidelines.";
        assertTrue(ResponseValidator.isDenial(response));
    }

    @Test
    void testCaseInsensitiveUppercase() {
        String response = "THIS CONTENT IS AGAINST MY PROGRAMMING GUIDELINES.";
        assertTrue(ResponseValidator.isDenial(response));
    }

    @Test
    void testCaseInsensitiveLowercase() {
        String response = "this content is against my programming guidelines.";
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
