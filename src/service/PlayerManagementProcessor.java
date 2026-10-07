package service;

import database.FileDatabase;
import exception.ValidationException;
import model.IndividualPlayer;
import model.Player;
import model.TeamPlayer;

import java.io.IOException;

/** Handles player records and their persistence independently from the main tournament service. */
public class PlayerManagementProcessor {
    private static final int MAX_PLAYERS = 1000;
    private final Player[] players = new Player[MAX_PLAYERS];
    private final FileDatabase database;
    private int playerCount;

    public PlayerManagementProcessor() {
        this(new FileDatabase("data"));
    }

    public PlayerManagementProcessor(FileDatabase database) {
        this.database = database;
        loadPlayers();
    }

    public void registerPlayer(Player player) throws ValidationException {
        if (findPlayer(player.getPlayerId()) != null) {
            throw new ValidationException("A player with that ID already exists.");
        }
        if (playerCount >= MAX_PLAYERS) {
            throw new ValidationException("The maximum number of players is " + MAX_PLAYERS + ".");
        }
        try {
            database.savePlayer(player);
        } catch (IOException exception) {
            throw new ValidationException("Could not save player data.");
        }
        players[playerCount++] = player;
    }

    public Player findPlayer(String id) {
        if (id == null) return null;
        for (int index = 0; index < playerCount; index++) {
            if (players[index].getPlayerId().equalsIgnoreCase(id.trim())) return players[index];
        }
        return null;
    }

    public Player[] getPlayers() {
        Player[] result = new Player[playerCount];
        System.arraycopy(players, 0, result, 0, playerCount);
        return result;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    private void loadPlayers() {
        try {
            for (String[] row : database.read("players.db")) {
                if (row == null) continue;
                if (row.length != 6) throw new IllegalStateException("Saved player record has an invalid number of fields.");
                int age = Integer.parseInt(row[4]);
                int teamSize = Integer.parseInt(row[5]);
                Player player = teamSize > 0
                        ? new TeamPlayer(row[0], row[1], row[2], row[3], age, teamSize)
                        : new IndividualPlayer(row[0], row[1], row[2], row[3], age);
                if (findPlayer(player.getPlayerId()) == null && playerCount < MAX_PLAYERS) {
                    players[playerCount++] = player;
                }
            }
        } catch (IOException | NumberFormatException | ValidationException exception) {
            throw new IllegalStateException("Could not load saved player data.", exception);
        }
    }
}
