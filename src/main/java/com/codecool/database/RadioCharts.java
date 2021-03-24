package com.codecool.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RadioCharts {

    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;

    public RadioCharts(String DB_URL, String DB_USER, String DB_PASSWORD) {
        this.DB_URL = DB_URL;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
    }

    public Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public String getMostPlayedSong() {

        String sql = "SELECT song, times_aired FROM music_broadcast ORDER BY times_aired DESC";
        String name = "";
        int aired;

        List<Song> songs = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                name = rs.getString("song");
                aired = rs.getInt("times_aired");
                songs.add(new Song(name, aired));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (songs.size() == 0) {
            return "";
        }
        songs.sort(Comparator.comparing(Song::getTimesAired));

        for (int i = 1; i < songs.size(); i++) {
            if (songs.get(i - 1).getTimesAired() == songs.get(i).getTimesAired()) {
                return songs.get(i).getTitle();
            }
        }
        return songs.get(0).getTitle();
    }


    public String getMostActiveArtist() {

        String sql = "SELECT artist, song, times_aired FROM music_broadcast";
        String nameOfArtist = "";
        String titleOfSong = "";
        int aired;

        List<Artist> artists = new ArrayList<>();
        List<Artist> arts = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                nameOfArtist = rs.getString("artist");
                titleOfSong = rs.getString("song");
                aired = rs.getInt("times_aired");
                artists.add(new Artist(nameOfArtist, titleOfSong, aired));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (artists.size() == 0) {
            return "";
        }
        artists.sort(Comparator.comparing(Artist::getName));

        int counter = 0;
        arts.add(artists.get(counter));
        for (int i = 1; i < artists.size(); i++) {
            if (!(artists.get(i).getName().equals(arts.get(counter).getName()))) {
                arts.add(artists.get(i));
                counter++;
            }
        }
        if (artists.size() > arts.size()) {
            int[] played = new int[arts.size()];
            for (int i = 0; i < arts.size(); i++) {
                for (int j = 0; j < artists.size(); j++) {
                    if (arts.get(i).getName().equals(artists.get(j).getName())) {
                        played[i] += 1;
                    }
                }
            }
            int maxIndex = 0;
            for (int k = 0; k < played.length; k++) {
                if (played[k] > maxIndex) {
                    maxIndex = k;
                }
            }
            return arts.get(maxIndex).getName();
        }
        return artists.get(0).getName();
    }
}


