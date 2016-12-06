package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.Options;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.time.LocalDate;

class RedemptionService {

    private BufferedReader input;
    private PrintStream output;
    private LocalDate applicationDay;

    RedemptionService(BufferedReader input, PrintStream output, LocalDate applicationDay) {
        this.input = input;
        this.output = output;
        this.applicationDay = applicationDay;
    }

    void createAndPrintRedemptionPlan() {

        OptionsReader optionsReader = new OptionsReader(input, output);
        Options options = optionsReader.readOptions();

        Calculator calculator = new Calculator(options, applicationDay);

        Renderer renderer = new Renderer(calculator);
        String renderedTable = renderer.renderTable();
        this.output.print(renderedTable);
    }
}
