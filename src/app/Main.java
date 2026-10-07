package app;

import exception.ValidationException;
import model.Game;
import model.IndividualPlayer;
import model.Player;
import model.TeamPlayer;
import service.PlayerManagementProcessor;
import service.TournamentSystem;

import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final TournamentSystem system = new TournamentSystem();
    private final PlayerManagementProcessor playerProcessor = new PlayerManagementProcessor();

    public static void main(String[] args) { new Main().run(); }

    private void run() {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1: addGame(); break;
                    case 2: displayGames(); break;
                    case 3: playerManagementMenu(); break;
                    case 4: createRegistration(); break;
                    case 5: displayRegistrations(); break;
                    case 6: calculateFees(); break;
                    case 7: addMatch(); break;
                    case 8: displayMatches(); break;
                    case 0: System.out.println("Thank you. Goodbye!"); break;
                    default: System.out.println("Invalid choice. Please select a menu number.");
                }
            } catch (ValidationException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n============================================");
        System.out.println("       CAMPUS GAMING TOURNAMENT SYSTEM");
        System.out.println("============================================");
        System.out.println("1. Add Game\n2. Display Games\n3. Player Management");
        System.out.println("4. Create Tournament Registration\n5. Display Tournament Registrations");
        System.out.println("6. Calculate Registration Fees\n7. Add Match Result\n8. Display Match Results\n0. Exit");
        System.out.println("============================================");
    }

    private void addGame() throws ValidationException {
        system.addGame(new Game(readText("Game ID: "), readText("Game name: "), readText("Game category: "),
                readDouble("Registration fee: "), readInt("Maximum players: ")));
        System.out.println("Game added successfully.");
    }

    private void displayGames() {
        System.out.println("\nGAME ID  GAME NAME              CATEGORY        FEE      MAX");
        if (system.getGameCount() == 0) System.out.println("No games recorded.");
        for (int index = 0; index < system.getGameCount(); index++) System.out.println(system.getGames()[index]);
    }

    private void playerManagementMenu() {
        int choice;
        do {
            System.out.println("\n============================================");
            System.out.println("             PLAYER MANAGEMENT");
            System.out.println("============================================");
            System.out.println("1. Register Player\n2. Display Players\n0. Return to Main Menu");
            System.out.println("============================================");
            choice = readInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1: registerPlayer(); break;
                    case 2: displayPlayers(); break;
                    case 0: System.out.println("Returning to the main menu."); break;
                    default: System.out.println("Invalid choice. Please select a menu number.");
                }
            } catch (ValidationException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        } while (choice != 0);
    }

    private void registerPlayer() throws ValidationException {
        String id = readText("Player ID: ");
        String name = readText("Player name: ");
        String tag = readText("Username / gamer tag: ");
        String phone = readText("Phone number: ");
        int age = readInt("Age: ");
        int type = readInt("Player type (1 Individual, 2 Team): ");

        Player player;
        if (type == 1) player = new IndividualPlayer(id, name, tag, phone, age);
        else if (type == 2) player = new TeamPlayer(id, name, tag, phone, age, readInt("Team size: "));
        else throw new ValidationException("Player type must be 1 or 2.");

        playerProcessor.registerPlayer(player);
        System.out.println("Player registered successfully.");
    }

    private void displayPlayers() {
        System.out.println("\nPLAYER ID PLAYER NAME            GAMER TAG        PHONE           TYPE           AGE");
        Player[] players = playerProcessor.getPlayers();
        if (players.length == 0) System.out.println("No players recorded.");
        for (Player player : players) System.out.println(player);
    }

    private void createRegistration() throws ValidationException {
        system.addRegistration(readText("Registration ID: "), readText("Player ID: "), readText("Game ID: "));
        System.out.println("Tournament registration created successfully.");
    }

    private void displayRegistrations() {
        System.out.println("\nREGISTRATION PLAYER ID GAME ID    FEE");
        if (system.getRegistrationCount() == 0) System.out.println("No registrations recorded.");
        for (int index = 0; index < system.getRegistrationCount(); index++) System.out.println(system.getRegistrations()[index]);
    }

    private void calculateFees() {
        double total = 0;
        for (int index = 0; index < system.getRegistrationCount(); index++) total += system.getRegistrations()[index].getFee();
        System.out.printf("Total registration fees: RM%.2f%n", total);
    }

    private void addMatch() throws ValidationException {
        system.addMatch(readText("Match ID: "), readText("Game ID: "), readText("Player 1 ID: "),
                readText("Player 2 ID: "), readText("Result / status: "));
        System.out.println("Match result added successfully.");
    }

    private void displayMatches() {
        System.out.println("\nMATCH ID GAME ID    PLAYER 1     PLAYER 2     RESULT");
        if (system.getMatchCount() == 0) System.out.println("No matches recorded.");
        for (int index = 0; index < system.getMatchCount(); index++) System.out.println(system.getMatches()[index]);
    }

    private String readText(String prompt) { System.out.print(prompt); return scanner.nextLine().trim(); }

    private int readInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(readText(prompt)); }
            catch (NumberFormatException exception) { System.out.println("Please enter a whole number."); }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try { return Double.parseDouble(readText(prompt)); }
            catch (NumberFormatException exception) { System.out.println("Please enter a valid amount."); }
        }
    }
}
