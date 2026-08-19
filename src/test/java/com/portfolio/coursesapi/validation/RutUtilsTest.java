package com.portfolio.coursesapi.validation;

import com.portfolio.coursesapi.validation.rut.RutUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RutUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"12345678-5", "12.345.678-5", "11111111-1", "11.111.111-1", "1-9"})
    void validRutsShouldPass(String rut) {
        assertTrue(RutUtils.isValid(rut));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678-9", "12345678-K", "abcdefg-1", "", "  "})
    void invalidRutsShouldFail(String rut) {
        assertFalse(RutUtils.isValid(rut));
    }

    @Test
    void nullRutShouldBeInvalid() {
        assertFalse(RutUtils.isValid(null));
    }

    @Test
    void normalizeShouldStripDotsAndUppercaseDv() {
        assertEquals("12345678-5", RutUtils.normalize("12.345.678-5"));
        assertEquals("12345678-5", RutUtils.normalize("12345678-5"));
    }

    @Test
    void normalizeShouldThrowOnInvalidFormat() {
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> RutUtils.normalize("no-es-un-rut"));
    }
}
