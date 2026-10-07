package service;

import database.FileDatabase;
import exception.ValidationException;
import model.Game;
import model.IndividualPlayer;
import model.Match;
import model.Player;
import model.TeamPlayer;
import model.TournamentRegistration;

import java.io.IOException;

public class TournamentSystem {
    private static final int MAX_RECORDS = 1000;
    private final Game[] games = new Game[MAX_RECORDS];
    private final Player[] players = new Player[MAX_RECORDS];
    private final TournamentRegistration[] registrations = new TournamentRegistration[MAX_RECORDS];
    private final Match[] matches = new Match[MAX_RECORDS];
    private int gameCount;
    private int playerCount;
    private int registrationCount;
    private int matchCount;
    private final FileDatabase database = new FileDatabase("data");

    public TournamentSystem() {
        loadSavedData();
    }

    public void addGame(Game game) throws ValidationException {
        if (findGame(game.getGameId()) != null) throw new ValidationException("A game with that ID already exists.");
        saveGame(game);
        ensureCapacity(gameCount, "games");
        games[gameCount++] = game;
    }

    public void addPlayer(Player player) throws ValidationException {
        if (findPlayer(player.getPlayerId()) != null) throw new ValidationException("A player with that ID already exists.");
        savePlayer(player);
        ensureCapacity(playerCount, "players");
        players[playerCount++] = player;
    }

    public void addRegistration(String registrationId, String playerId, String gameId)
            throws ValidationException {
        if (findRegistration(registrationId) != null) throw new ValidationException("A registration with that ID already exists.");
        Player player = findPlayer(playerId);
        Game game = findGame(gameId);
        if (player == null || game == null) throw new ValidationException("Player ID or game ID was not found.");
        TournamentRegistration registration = new TournamentRegistration(registrationId, player, game);
        saveRegistration(registration);
        ensureCapacity(registrationCount, "registrations");
        registrations[registrationCount++] = registration;
    }

    public void addMatch(String matchId, String gameId, String playerOneId,
                         String playerTwoId, String result) throws ValidationException {
        if (findMatch(matchId) != null) throw new ValidationException("A match with that ID already exists.");
        Game game = findGame(gameId);
        if (game == null || findPlayer(playerOneId) == null || findPlayer(playerTwoId) == null) {
            throw new ValidationException("Game or player ID was not found.");
        }
        Match match = new Match(matchId, game, playerOneId, playerTwoId, result);
        saveMatch(match);
        ensureCapacity(matchCount, "matches");
        matches[matchCount++] = match;
    }

    public Game findGame(String id) {
        for (int index = 0; index < gameCount; index++) {
            if (games[index].getGameId().equalsIgnoreCase(id.trim())) return games[index];
        }
        return null;
    }

    public Player findPlayer(String id) {
        for (int index = 0; index < playerCount; index++) {
            if (players[index].getPlayerId().equalsIgnoreCase(id.trim())) return players[index];
        }
        return null;
    }

    private TournamentRegistration findRegistration(String id) {
        for (int index = 0; index < registrationCount; index++) {
            if (registrations[index].getRegistrationId().equalsIgnoreCase(id.trim())) return registrations[index];
        }
        return null;
    }

    private Match findMatch(String id) {
        for (int index = 0; index < matchCount; index++) {
            if (matches[index].getMatchId().equalsIgnoreCase(id.trim())) return matches[index];
        }
        return null;
    }

    private void loadSavedData() {
        try {
            String[][] rows = database.read("games.db");
            for (String[] row : rows) if (row != null) {
                addLoadedGame(new Game(row[0], row[1], row[2], Double.parseDouble(row[3]), Integer.parseInt(row[4])));
            }
            rows = database.read("players.db");
            for (String[] row : rows) if (row != null) {
                if (Integer.parseInt(row[5]) > 0) {
                    addLoadedPlayer(new TeamPlayer(row[0], row[1], row[2], row[3], Integer.parseInt(row[4]), Integer.parseInt(row[5])));
                } else {
                    addLoadedPlayer(new IndividualPlayer(row[0], row[1], row[2], row[3], Integer.parseInt(row[4])));
                }
            }
            rows = database.read("registrations.db");
            for (String[] row : rows) if (row != null) {
                Game game = findGame(row[2]);
                Player player = findPlayer(row[1]);
                if (game != null && player != null) addLoadedRegistration(new TournamentRegistration(row[0], player, game));
            }
            rows = database.read("matches.db");
            for (String[] row : rows) if (row != null) {
                Game game = findGame(row[1]);
                if (game != null && findPlayer(row[2]) != null && findPlayer(row[3]) != null) {
                    addLoadedMatch(new Match(row[0], game, row[2], row[3], row[4]));
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read saved database files.", exception);
        } catch (ValidationException | NumberFormatException exception) {
            throw new IllegalStateException("Saved database contains invalid data.", exception);
        }
    }

    private void saveGame(Game game) throws ValidationException {
        try { database.saveGame(game); }
        catch (IOException exception) { throw new ValidationException("Could not save game data."); }
    }

    private void savePlayer(Player player) throws ValidationException {
        try { database.savePlayer(player); }
        catch (IOException exception) { throw new ValidationException("Could not save player data."); }
    }

    private void saveRegistration(TournamentRegistration registration) throws ValidationException {
        try { database.saveRegistration(registration); }
        catch (IOException exception) { throw new ValidationException("Could not save registration data."); }
    }

    private void saveMatch(Match match) throws ValidationException {
        try { database.saveMatch(match); }
        catch (IOException exception) { throw new ValidationException("Could not save match data."); }
    }

    private void ensureCapacity(int count, String recordType) throws ValidationException {
        if (count >= MAX_RECORDS) throw new ValidationException("The maximum number of " + recordType + " is " + MAX_RECORDS + ".");
    }

    private void addLoadedGame(Game game) { if (gameCount < MAX_RECORDS) games[gameCount++] = game; }
    private void addLoadedPlayer(Player player) { if (playerCount < MAX_RECORDS) players[playerCount++] = player; }
    private void addLoadedRegistration(TournamentRegistration registration) { if (registrationCount < MAX_RECORDS) registrations[registrationCount++] = registration; }
    private void addLoadedMatch(Match match) { if (matchCount < MAX_RECORDS) matches[matchCount++] = match; }

    public Game[] getGames() { return games; }
    public int getGameCount() { return gameCount; }
    public Player[] getPlayers() { return players; }
    public int getPlayerCount() { return playerCount; }
    public TournamentRegistration[] getRegistrations() { return registrations; }
    public int getRegistrationCount() { return registrationCount; }
    public Match[] getMatches() { return matches; }
    public int getMatchCount() { return matchCount; }
}