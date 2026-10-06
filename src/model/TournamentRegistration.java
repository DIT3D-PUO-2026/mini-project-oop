package model;

import exception.ValidationException;

public class TournamentRegistration {
    private final String registrationId;
    private final Player player;
    private final Game game;

    public TournamentRegistration(String registrationId, Player player, Game game)
            throws ValidationException {
        if (registrationId == null || registrationId.trim().isEmpty()) {
            throw new ValidationException("Registration ID is required.");
        }
        this.registrationId = registrationId.trim().toUpperCase();
        this.player = player;
        this.game = game;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getPlayerId() { return player.getPlayerId(); }
    public String getGameId() { return game.getGameId(); }

    public double getFee() {
        return game.calculateFee(player);
    }

    @Override
    public String toString() {
        return String.format("%-10s %-10s %-10s RM%8.2f", registrationId,
                player.getPlayerId(), game.getGameId(), getFee());
    }
}