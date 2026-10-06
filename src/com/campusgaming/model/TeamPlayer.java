package com.campusgaming.model;

import com.campusgaming.exception.ValidationException;

public class TeamPlayer extends Player {
    private final int teamSize;

    public TeamPlayer(String playerId, String playerName, String gamerTag,
                      String phoneNumber, int age, int teamSize) throws ValidationException {
        super(playerId, playerName, gamerTag, phoneNumber, age);
        if (teamSize < 2 || teamSize > 10) {
            throw new ValidationException("Team size must be between 2 and 10.");
        }
        this.teamSize = teamSize;
    }

    @Override
    public String getPlayerType() {
        return "Team (" + teamSize + ")";
    }

    @Override
    public double getAdditionalFee() {
        return teamSize * 5.00;
    }
}