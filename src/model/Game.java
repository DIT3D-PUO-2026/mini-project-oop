package model;

import exception.ValidationException;

public class Game {
    private final String gameId;
    private final String gameName;
    private final String category;
    private final double registrationFee;
    private final int maximumPlayers;

    public Game(String gameId, String gameName, String category,
                double registrationFee, int maximumPlayers) throws ValidationException {
        if (isBlank(gameId) || isBlank(gameName) || isBlank(category)) {
            throw new ValidationException("Game ID, name and category are required.");
        }
        if (registrationFee < 0 || maximumPlayers <= 0) {
            throw new ValidationException("Fee cannot be negative and maximum players must be positive.");
        }
        this.gameId = gameId.trim().toUpperCase();
        this.gameName = gameName.trim();
        this.category = category.trim();
        this.registrationFee = registrationFee;
        this.maximumPlayers = maximumPlayers;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String getGameId() {
        return gameId;
    }

    public String getGameName() { return gameName; }
    public String getCategory() { return category; }
    public double getRegistrationFee() { return registrationFee; }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public double calculateFee(Player player) {
        return registrationFee + player.getAdditionalFee();
    }

    @Override
    public String toString() {
        return String.format("%-8s %-22s %-15s RM%8.2f %5d", gameId, gameName, category,
                registrationFee, maximumPlayers);
    }
}