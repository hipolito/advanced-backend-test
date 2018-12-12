package com.ifood.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spotify")
public class SpotifyConfiguration {
    private String clientToken;
    private String tokenUri;
    private String playlistUri;

    /**
     * @return the clientToken
     */
    public String getClientToken() {
        return clientToken;
    }

    /**
     * @return the playlistUri
     */
    public String getPlaylistUri() {
        return playlistUri;
    }

    /**
     * @param playlistUri the playlistUri to set
     */
    public void setPlaylistUri(String playlistUri) {
        this.playlistUri = playlistUri;
    }

    /**
     * @return the tokenUri
     */
    public String getTokenUri() {
        return tokenUri;
    }

    /**
     * @param tokenUri the tokenUri to set
     */
    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    /**
     * @param clientToken the clientToken to set
     */
    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

}