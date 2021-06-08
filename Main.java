package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    static final double COEF_ARI_1 = 4.71, COEF_ARI_2 = 0.5, COEF_ARI_3 = 21.43;
    static final double COEF_FK_1 = 0.39, COEF_FK_2 = 11.8, COEF_FK_3 = 15.59;
    static final double COEF_SMG_1 = 1.043, COEF_SMG_2 = 30, COEF_SMG_3 = 3.1291;
    static final double COEF_CLI_1 = 0.0588, COEF_CLI_2 = 0.296, COEF_CLI_3 = 15.8;
    static final String[] AGE = { "6", "7", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "24", "24+" };

    public static void main(String[] args) {
        try {
            String text = Files.readString(Paths.get(args[0]));

            int letters = text.replaceAll("[^a-zA-Z]", "").length();
            int sentences = text.split("[.!?] ").length;
            int words = text.split(" ").length;
            int characters = text.replaceAll("\\s", "").length();

            String str = text.replaceAll("e\\b", "").toLowerCase();
            str = str.replaceAll("[aeiouy]{2}", "a").replaceAll("[^aeiouy\\d ]", "");
            int syllables = str.replaceAll("\\s", "").length();
            str += " ";
            str = str.replaceAll("\\d", "").replaceAll("[aeiouy]{1,2} ", " ");
            str = str.replaceAll(" +", "-");
            int polysyllables = str.split("-").length;
            if (str.charAt(0) == '-' && str.charAt(str.length() - 1) == '-')
                polysyllables--;

            System.out.println("The text is:\n" + text);
            System.out.println("Words: " + words);
            System.out.println("Sentences: " + sentences);
            System.out.println("Characters: " + characters);
            System.out.println("Syllables: " + syllables);
            System.out.println("Polysyllables: " + polysyllables);

            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "ARI":
                    ARI(characters, words, sentences);
                    break;
                case "FK":
                    FKrt(syllables, words, sentences);
                    break;
                case "SMOG":
                    SMG(polysyllables, sentences);
                    break;
                case "CL":
                    CLI(letters, words, sentences);
                    break;
                case "all":
                    double average = 0;
                    average += ARI(characters, words, sentences);
                    average += FKrt(syllables, words, sentences);
                    average += SMG(polysyllables, sentences);
                    average += CLI(letters, words, sentences);
                    System.out.printf("This text should be understood in average by %s-year-olds.", average / 4);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
    }

    static double ARI(int characters, int words, int sentences) {
        double score = COEF_ARI_1 * characters / words + COEF_ARI_2 * words / sentences - COEF_ARI_3;
        int index = (int) Math.round(score);
        System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).\n", score, AGE[index - 1]);
        if (index < 14)
            return Double.parseDouble(AGE[index - 1]);
        else
            return Double.parseDouble(AGE[AGE.length - 2]);
    }

    static double FKrt(int syllables, int words, int sentences) {
        double score = COEF_FK_1 * words / sentences + COEF_FK_2 * syllables / words - COEF_FK_3;
        int index = (int) Math.round(score);
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).\n", score, AGE[index - 1]);
        if (index < 14)
            return Double.parseDouble(AGE[index - 1]);
        else
            return Double.parseDouble(AGE[AGE.length - 2]);
    }

    static double SMG(int polysyllables, int sentences) {
        double score = COEF_SMG_1 * Math.sqrt(polysyllables * COEF_SMG_2 / sentences) + COEF_SMG_3;
        int index = (int) Math.round(score);
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).\n", score, AGE[index - 1]);
        if (index < 14)
            return Double.parseDouble(AGE[index - 1]);
        else
            return Double.parseDouble(AGE[AGE.length - 2]);
    }

    static double CLI(int letters, int words, int sentences) {
        double score = COEF_CLI_1 * letters / words * 100 - COEF_CLI_2 * sentences / words * 100 - COEF_CLI_3;
        int index = (int) Math.round(score);
        System.out.printf("Coleman–Liau index: %.2f (about %s-year-olds).\n", score, AGE[index - 1]);
        if (index < 14)
            return Double.parseDouble(AGE[index - 1]);
        else
            return Double.parseDouble(AGE[AGE.length - 2]);
    }
}