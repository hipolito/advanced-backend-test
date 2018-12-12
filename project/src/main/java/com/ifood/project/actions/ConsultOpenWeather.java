package com.ifood.project.actions;

import com.ifood.project.config.OpenWeatherConfiguration;
import com.ifood.project.config.RedisConfiguration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import redis.clients.jedis.Jedis;

@Component
public class ConsultOpenWeather {

    @Autowired
    private OpenWeatherConfiguration openWeatherConfiguration;

    @Autowired
    private RedisConfiguration redisConfiguration;

    public float getLocalTemperature(String city) {
        return convertToCelsius(usingCityName(city));
    }

    public float getLocalTemperature(String lat, String lon) {
        return convertToCelsius(usingCityCoordinates(lat, lon));
    }

    private float convertToCelsius(String data) {
        JSONObject json = new JSONObject(data);
        return json.getJSONObject("main").getFloat("temp") - 273;
    }

    private String usingCityName(String city) {
        final String uri = openWeatherConfiguration.getApiUri() + "q=" + city + "&APPID="
                + openWeatherConfiguration.getAppId();
        return retrieveData(uri);
    }

    private String usingCityCoordinates(String lat, String lon) {
        final String uri = openWeatherConfiguration.getApiUri() + "lat=" + lat + "&lon=" + lon + "&APPID="
                + openWeatherConfiguration.getAppId();
        return retrieveData(uri);
    }

    private String retrieveData(String uri) {
        Jedis jedis = new Jedis(redisConfiguration.getHost(), redisConfiguration.getPort());
        
        if (jedis.isConnected()) {
            String retrievedData = jedis.get(uri);
            if (retrievedData != null) {
                jedis.close();
                return retrievedData;
            }

            retrievedData = consultAPI(uri);
            jedis.set(uri, retrievedData);
            jedis.close();
            return retrievedData;
        }
        jedis.close();
        return consultAPI(uri);
    }

    @HystrixCommand(fallbackMethod = "serviceUnavailable")
    private String consultAPI(final String uri) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, String.class);
    }

    private String serviceUnavailable() {
        return "OpenWeather Service Unavailable";
    }
}