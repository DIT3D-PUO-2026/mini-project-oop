package model;

import exception.ValidationException;

public abstract class Player {
    private final String playerId;
    private final String playerName;
    private final String gamerTag;
    private final String phoneNumber;
    private final int age;

    protected Player(String playerId, String playerName, String gamerTag,
                     String phoneNumber, int age) throws ValidationException {
        if (isBlank(playerId) || isBlank(playerName) || isBlank(gamerTag) || isBlank(phoneNumber)) {
            throw new ValidationException("All player details are required.");
        }
        if (age < 13 || age > 100) {
            throw new ValidationException("Player age must be between 13 and 100.");
        }
        this.playerId = playerId.trim().toUpperCase();
        this.playerName = playerName.trim();
        this.gamerTag = gamerTag.trim();
        this.phoneNumber = phoneNumber.trim();
        this.age = age;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() { return playerName; }
    public String getGamerTag() { return gamerTag; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getAge() { return age; }

    public abstract String getPlayerType();

    public abstract double getAdditionalFee();

    @Override
    public String toString() {
        return String.format("%-8s %-22s %-16s %-15s %-14s %3d", playerId, playerName,
                gamerTag, phoneNumber, getPlayerType(), age);
    }
}