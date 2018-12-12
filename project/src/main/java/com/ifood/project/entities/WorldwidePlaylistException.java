package com.ifood.project.entities;

public class WorldwidePlaylistException {
    
    private String error;

    public WorldwidePlaylistException(String error){
        this.setError(error);
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
}