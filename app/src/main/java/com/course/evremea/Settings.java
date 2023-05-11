package com.course.evremea;

public class Settings {

    private static Settings instance;

    private String apiHost = "172.19.171.159";
    private int apiPort = 4567;

    private static final String LOCATION_END_POINT = "http://%s:%d/api/v1/weather/cities/city/%s";
    private static final String COORDINATES_END_POINT = "http://%s:%d/api/v1/weather/coordinates/latitude/%s/longitude/%s";

    private Settings() {
    }

    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        return instance;
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public int getApiPort() {
        return apiPort;
    }

    public void setApiPort(int apiPort) {
        this.apiPort = apiPort;
    }

    public String getLocationEndpoint(String location) {
        return String.format(LOCATION_END_POINT, apiHost, apiPort, location);
    }

    public String getCoordinatesEndpoint(String lat, String lon) {
        return String.format(COORDINATES_END_POINT, apiHost, apiPort, lat, lon);
    }
}
