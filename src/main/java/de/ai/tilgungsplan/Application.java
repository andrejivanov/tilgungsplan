package de.ai.tilgungsplan;

import java.time.LocalDate;

public class Application {
    public static void main(String[] args) {
        new RedemptionService(System.in, System.out, LocalDate.now()).createAndPrintRedemptionPlan();
    }
}