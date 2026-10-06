package com.campusgaming.service;

import com.campusgaming.exception.ValidationException;
import com.campusgaming.model.Game;
import com.campusgaming.model.Match;
import com.campusgaming.model.Player;
import com.campusgaming.model.TournamentRegistration;

import java.util.ArrayList;
import java.util.List;

public class TournamentSystem {
    private final List<Game> games = new ArrayList<Game>();
    private final List<Player> players = new ArrayList<Player>();
    private final List<TournamentRegistration> registrations = new ArrayList<TournamentRegistration>();
    private final List<Match> matches = new ArrayList<Match>();

    public void addGame(Game game) throws ValidationException {
        if (findGame(game.getGameId()) != null) throw new ValidationException("A game with that ID already exists.");
        games.add(game);
    }

    public void addPlayer(Player player) throws ValidationException {
        if (findPlayer(player.getPlayerId()) != null) throw new ValidationException("A player with that ID already exists.");
        players.add(player);
    }

    public void addRegistration(String registrationId, String playerId, String gameId)
            throws ValidationException {
        if (findRegistration(registrationId) != null) throw new ValidationException("A registration with that ID already exists.");
        Player player = findPlayer(playerId);
        Game game = findGame(gameId);
        if (player == null || game == null) throw new ValidationException("Player ID or game ID was not found.");
        registrations.add(new TournamentRegistration(registrationId, player, game));
    }

    public void addMatch(String matchId, String gameId, String playerOneId,
                         String playerTwoId, String result) throws ValidationException {
        if (findMatch(matchId) != null) throw new ValidationException("A match with that ID already exists.");
        Game game = findGame(gameId);
        if (game == null || findPlayer(playerOneId) == null || findPlayer(playerTwoId) == null) {
            throw new ValidationException("Game or player ID was not found.");
        }
        matches.add(new Match(matchId, game, playerOneId, playerTwoId, result));
    }

    public Game findGame(String id) {
        for (Game game : games) if (game.getGameId().equalsIgnoreCase(id.trim())) return game;
        return null;
    }

    public Player findPlayer(String id) {
        for (Player player : players) if (player.getPlayerId().equalsIgnoreCase(id.trim())) return player;
        return null;
    }

    private TournamentRegistration findRegistration(String id) {
        for (TournamentRegistration registration : registrations) {
            if (registration.getRegistrationId().equalsIgnoreCase(id.trim())) return registration;
        }
        return null;
    }

    private Match findMatch(String id) {
        for (Match match : matches) if (match.getMatchId().equalsIgnoreCase(id.trim())) return match;
        return null;
    }

    public List<Game> getGames() { return games; }
    public List<Player> getPlayers() { return players; }
    public List<TournamentRegistration> getRegistrations() { return registrations; }
    public List<Match> getMatches() { return matches; }
}