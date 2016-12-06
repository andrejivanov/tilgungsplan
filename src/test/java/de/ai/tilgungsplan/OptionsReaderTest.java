package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.Options;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OptionsReaderTest {

    @Mock
    BufferedReader input;

    @Mock
    PrintStream printStream;

    @Test
    public void readerShouldReadCorrectInputs() throws Exception {

        when(input.readLine())
                .thenReturn("100000")
                .thenReturn("2,12")
                .thenReturn("2")
                .thenReturn("10");

        OptionsReader optionsReader = new OptionsReader(input, printStream);
        Options options = optionsReader.readOptions();
        Options expectedOptions = Options.newBuilder()
                .withCreditValueInEuro(BigDecimal.valueOf(100000.0))
                .withBorrowingRate(BigDecimal.valueOf(0.0212).setScale(4, BigDecimal.ROUND_HALF_UP))
                .withFirstRedemptionRate(BigDecimal.valueOf(0.0200).setScale(4, BigDecimal.ROUND_HALF_UP))
                .withYearsOfFixedInterestRate(10)
                .build();
        assertEquals(expectedOptions, options);
    }

    @Test
    public void readerShouldShowErrorMessageIfInputWrongFormated() throws Exception {
        when(input.readLine())
                .thenReturn("abs")
                .thenReturn("100000")
                .thenReturn("2,12")
                .thenReturn("2")
                .thenReturn("10");
        OptionsReader optionsReader = new OptionsReader(input, printStream);
        optionsReader.readOptions();
        verify(printStream).println(startsWith("Ihre Eingabe "));
    }

}