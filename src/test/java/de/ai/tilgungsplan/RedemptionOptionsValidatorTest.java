package de.ai.tilgungsplan;

import org.junit.Test;

import static de.ai.tilgungsplan.RedemptionOptionsValidator.isBigNumberValueValid;
import static de.ai.tilgungsplan.RedemptionOptionsValidator.isIntegerValueValid;
import static org.junit.Assert.*;

public class RedemptionOptionsValidatorTest {

    @Test
    public void validatorShouldPassCorrectBigNumberValue() throws Exception {
        assertTrue("Correct value should be passed", isBigNumberValueValid("100000"));
    }

    @Test
    public void validatorShouldNotPassIncorrectBigNumberValue() throws Exception {
        assertFalse("Incorrect value should be not passed", isBigNumberValueValid("bad"));
    }

    @Test
    public void validatorShouldNotPassEmptyBigNumberValue() throws Exception {
        assertFalse("Empty value should be not passed", isBigNumberValueValid(""));
    }

    @Test
    public void validatorShouldNotPassNullBigNumberValue() throws Exception {
        assertFalse("Null value should be not passed", isBigNumberValueValid(null));
    }

    @Test
    public void validatorShouldPassCorrectIntegerValue() throws Exception {
        assertTrue("Correct value should be passed", isIntegerValueValid("20"));
    }

    @Test
    public void validatorShouldNotPassIncorrectIntegerValue() throws Exception {
        assertFalse("Incorrect value should be not passed", isIntegerValueValid("bad"));
    }

    @Test
    public void validatorShouldNotPassEmptyIntegerValue() throws Exception {
        assertFalse("Empty value should be not passed", isIntegerValueValid(""));
    }

    @Test
    public void validatorShouldNotPassNullIntegerValue() throws Exception {
        assertFalse("Null value should be not passed", isIntegerValueValid(null));
    }

}