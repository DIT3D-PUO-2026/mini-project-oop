package com.campusgaming.model;

import com.campusgaming.exception.ValidationException;

public class IndividualPlayer extends Player {
    public IndividualPlayer(String playerId, String playerName, String gamerTag,
                            String phoneNumber, int age) throws ValidationException {
        super(playerId, playerName, gamerTag, phoneNumber, age);
    }

    @Override
    public String getPlayerType() {
        return "Individual";
    }

    @Override
    public double getAdditionalFee() {
        return 0.00;
    }
}