package de.ai.tilgungsplan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;

public class Application {
    public static void main(String[] args) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        new RedemptionService(input, System.out, LocalDate.now()).createAndPrintRedemptionPlan();
    }
}