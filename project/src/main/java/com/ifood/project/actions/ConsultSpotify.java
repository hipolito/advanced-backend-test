package com.ifood.project.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.ifood.project.config.RedisConfiguration;
import com.ifood.project.config.SpotifyConfiguration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import redis.clients.jedis.Jedis;

@Component
public class ConsultSpotify {

    @Autowired
    private SpotifyConfiguration spotifyConfiguration;

    @Autowired
    private RedisConfiguration redisConfiguration;

    public List<String> retrieveCategoryList(String playlist) {
        return getPlaylistTracks(playlist);
    }

    private String getToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + spotifyConfiguration.getClientToken());
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(body,
                headers);
        String response = restTemplate.postForObject(spotifyConfiguration.getTokenUri(), request, String.class);
        JSONObject json = new JSONObject(response);

        return json.getString("access_token");
    }

    private List<String> getPlaylistTracks(String playlist) {
        String playlistTracksUri = getPlaylistTracksUri(playlist);
        String spotifyData = retrieveData(playlistTracksUri);
        JSONObject json = new JSONObject(spotifyData);

        List<String> trackNames = new ArrayList<String>();

        for (int i = 0; i < json.getJSONArray("items").length(); i++) {
            JSONObject jsonObject = json.getJSONArray("items").getJSONObject(i);
            trackNames.add(jsonObject.getJSONObject("track").getString("name"));
        }
        return trackNames;
    }

    @HystrixCommand(fallbackMethod = "serviceUnavailable")
    private String sendGETRequest(String uri) {
        String accessToken = getToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public String serviceUnavailable() {
        return "Spotify Service Unavailable";
    }

    private String getPlaylistTracksUri(String playlist) {
        String playlistTypesUri = spotifyConfiguration.getPlaylistUri() + playlist + "/playlists";
        String spotifyData = retrieveData(playlistTypesUri);

        JSONObject convertedData = new JSONObject(spotifyData);
        JSONArray playlists = convertedData.getJSONObject("playlists").getJSONArray("items");

        int index = new Random().nextInt(playlists.length());

        String playListTrackUri = playlists.getJSONObject(index).getJSONObject("tracks").getString("href");

        return playListTrackUri;
    }

    private String retrieveData(String uri) {
        Jedis jedis = new Jedis(redisConfiguration.getHost(), redisConfiguration.getPort());
        if (jedis.isConnected()) {
            String retrievedData = jedis.get(uri);
            if (retrievedData != null) {
                jedis.close();
                return retrievedData;
            }

            retrievedData = sendGETRequest(uri);
            jedis.set(uri, retrievedData);
            jedis.close();
            return retrievedData;
        }
        jedis.close();
        return sendGETRequest(uri);
    }
}