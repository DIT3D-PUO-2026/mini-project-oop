package model;

import exception.ValidationException;

public class Match {
    private final String matchId;
    private final Game game;
    private final String playerOneId;
    private final String playerTwoId;
    private final String result;

    public Match(String matchId, Game game, String playerOneId,
                 String playerTwoId, String result) throws ValidationException {
        if (isBlank(matchId) || isBlank(playerOneId) || isBlank(playerTwoId) || isBlank(result)) {
            throw new ValidationException("All match details are required.");
        }
        this.matchId = matchId.trim().toUpperCase();
        this.game = game;
        this.playerOneId = playerOneId.trim().toUpperCase();
        this.playerTwoId = playerTwoId.trim().toUpperCase();
        this.result = result.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String getMatchId() {
        return matchId;
    }

    public String getGameId() { return game.getGameId(); }
    public String getPlayerOneId() { return playerOneId; }
    public String getPlayerTwoId() { return playerTwoId; }
    public String getResult() { return result; }

    @Override
    public String toString() {
        return String.format("%-8s %-10s %-12s %-12s %s", matchId, game.getGameId(),
                playerOneId, playerTwoId, result);
    }
}