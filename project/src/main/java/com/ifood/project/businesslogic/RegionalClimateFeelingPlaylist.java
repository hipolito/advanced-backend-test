package com.ifood.project.businesslogic;

import java.util.List;

import com.ifood.project.actions.ConsultOpenWeather;
import com.ifood.project.actions.ConsultSpotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegionalClimateFeelingPlaylist {

    @Autowired
    private ConsultSpotify consultSpotify;

    @Autowired
    private ConsultOpenWeather consultOpenWeather;

    public List<String> getPlaylistByCityName(String city) {
        float temperature = consultOpenWeather.getLocalTemperature(city);
        String playlist = getTemperatureBasedPlaylistType(temperature);
        return consultSpotify.retrieveCategoryList(playlist);
    }

    public List<String> getPlaylistByCityCoordinates(String lat, String lon) {
        float temperature = consultOpenWeather.getLocalTemperature(lat, lon);
        String playlist = getTemperatureBasedPlaylistType(temperature);
        return consultSpotify.retrieveCategoryList(playlist);
    }

    private String getTemperatureBasedPlaylistType(float temperature) {
        if (temperature > 30)
            return "party";
        if (temperature >= 15)
            return "pop";
        if (temperature >= 10)
            return "rock";
        return "classical";
    }

}