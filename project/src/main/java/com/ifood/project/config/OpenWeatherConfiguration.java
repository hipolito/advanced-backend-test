package com.ifood.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("openweather")
public class OpenWeatherConfiguration {
    private String appId;
    private String apiUri;

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @return the apiUri
     */
    public String getApiUri() {
        return apiUri;
    }

    /**
     * @param apiUri the apiUri to set
     */
    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

}