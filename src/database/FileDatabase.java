package database;

import model.Game;
import model.Match;
import model.Player;
import model.TeamPlayer;
import model.TournamentRegistration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileDatabase {
    private final File folder;

    public FileDatabase(String folderName) {
        folder = new File(folderName);
        if (!folder.exists()) folder.mkdirs();
    }

    public void saveGame(Game game) throws IOException { append("games.db", join(game.getGameId(), game.getGameName(), game.getCategory(),
            String.valueOf(game.getRegistrationFee()), String.valueOf(game.getMaximumPlayers()))); }

    public void savePlayer(Player player) throws IOException {
        String teamSize = player instanceof TeamPlayer ? String.valueOf(((TeamPlayer) player).getTeamSize()) : "0";
        append("players.db", join(player.getPlayerId(), player.getPlayerName(), player.getGamerTag(),
                player.getPhoneNumber(), String.valueOf(player.getAge()), teamSize));
    }

    public void saveRegistration(TournamentRegistration registration) throws IOException {
        append("registrations.db", join(registration.getRegistrationId(), registration.getPlayerId(), registration.getGameId()));
    }

    public void saveMatch(Match match) throws IOException {
        append("matches.db", join(match.getMatchId(), match.getGameId(), match.getPlayerOneId(),
                match.getPlayerTwoId(), match.getResult()));
    }

    public String[][] read(String table) throws IOException {
        String[][] rows = new String[1000][];
        int rowCount = 0;
        File file = new File(folder, table);
        if (!file.exists()) return rows;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null && rowCount < rows.length) {
                if (!line.trim().isEmpty()) rows[rowCount++] = line.split("\\|", -1);
            }
        } finally { reader.close(); }
        return rows;
    }

    private void append(String table, String row) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(folder, table), true));
        try { writer.write(row); writer.newLine(); } finally { writer.close(); }
    }

    private String join(String... values) {
        StringBuilder row = new StringBuilder();
        for (String value : values) row.append(value.replace("|", "/")).append('|');
        return row.substring(0, row.length() - 1);
    }
}