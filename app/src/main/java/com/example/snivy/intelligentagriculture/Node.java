package com.example.snivy.intelligentagriculture;

import org.joda.time.LocalDateTime;

import java.sql.Time;
import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Snivy on 2018/4/17.
 */

class Node {
    private int MAC;
    private String Type, Node_name, Wind_direction;
    private Double X, Y;
    private String Time;
    private Float Temperature,Humidity,Pressure,Precipitation,Wind_speed,Soil_temperature,Soil_water_content,Light,Dissolved_oxygen,Oxygen_density,CO2_density,Water_level;

    public int getMAC() {
        return MAC;
    }

    public void setMAC(int MAC) {
        this.MAC = MAC;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNode_name() {
        return Node_name;
    }

    public void setNode_name(String node_name) {
        Node_name = node_name;
    }

    public String getWind_direction() {
        return Wind_direction;
    }

    public void setWind_direction(String wind_direction) {
        Wind_direction = wind_direction;
    }

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }

    public String getTime() {
        LocalDateTime localDateTime = LocalDateTime.parse(Time);
        return localDateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public void setTime(String time) {
        Time = time;
    }

    public Float getTemperature() {
        return Temperature;
    }

    public void setTemperature(Float temperature) {
        Temperature = temperature;
    }

    public Float getHumidity() {
        return Humidity;
    }

    public void setHumidity(Float humidity) {
        Humidity = humidity;
    }

    public Float getPressure() {
        return Pressure;
    }

    public void setPressure(Float pressure) {
        Pressure = pressure;
    }

    public Float getPrecipitation() {
        return Precipitation;
    }

    public void setPrecipitation(Float precipitation) {
        Precipitation = precipitation;
    }

    public Float getWind_speed() {
        return Wind_speed;
    }

    public void setWind_speed(Float wind_speed) {
        Wind_speed = wind_speed;
    }

    public Float getSoil_temperature() {
        return Soil_temperature;
    }

    public void setSoil_temperature(Float soil_temperature) {
        Soil_temperature = soil_temperature;
    }

    public Float getSoil_water_content() {
        return Soil_water_content;
    }

    public void setSoil_water_content(Float soil_water_content) {
        Soil_water_content = soil_water_content;
    }

    public Float getLight() {
        return Light;
    }

    public void setLight(Float light) {
        Light = light;
    }

    public Float getDissolved_oxygen() {
        return Dissolved_oxygen;
    }

    public void setDissolved_oxygen(Float dissolved_oxygen) {
        Dissolved_oxygen = dissolved_oxygen;
    }

    public Float getOxygen_density() {
        return Oxygen_density;
    }

    public void setOxygen_density(Float oxygen_density) {
        Oxygen_density = oxygen_density;
    }

    public Float getCO2_density() {
        return CO2_density;
    }

    public void setCO2_density(Float CO2_density) {
        this.CO2_density = CO2_density;
    }

    public Float getWater_level() {
        return Water_level;
    }

    public void setWater_level(Float water_level) {
        Water_level = water_level;
    }
}