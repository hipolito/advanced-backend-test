package com.ifood.project.entities;

import java.util.List;

public class WorldwidePlaylistResult {
    private final List<String> musics;

    public WorldwidePlaylistResult(List<String> musics) {
        this.musics = musics;
    }

    public List<String> getMusics() {
        return musics;
    }
}