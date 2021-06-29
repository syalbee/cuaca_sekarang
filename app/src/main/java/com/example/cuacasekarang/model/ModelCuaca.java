package com.example.cuacasekarang.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelCuaca {

    private ArrayList<weather> weather;
    @SerializedName("main")
    public Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public ArrayList<weather> getWeather() {
        return weather;
    }
    public void setWeather(ArrayList<ModelCuaca.weather> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "ModelCuaca{" +
                "weather=" + weather +
                '}';
    }


    public class weather{

        private Integer id;
        private String main;
        private String description;
        private String icon;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return "weather{" +
                    "id=" + id +
                    ", main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }

    public class Main{

        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
        public int pressure;
        public int humidity;
        public int sea_level;
        public int grnd_level;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getSea_level() {
            return sea_level;
        }

        public void setSea_level(int sea_level) {
            this.sea_level = sea_level;
        }

        public int getGrnd_level() {
            return grnd_level;
        }

        public void setGrnd_level(int grnd_level) {
            this.grnd_level = grnd_level;
        }

        @Override
        public String toString() {
            return "Main{" +
                    "temp=" + temp +
                    ", feels_like=" + feels_like +
                    ", temp_min=" + temp_min +
                    ", temp_max=" + temp_max +
                    ", pressure=" + pressure +
                    ", humidity=" + humidity +
                    ", sea_level=" + sea_level +
                    ", grnd_level=" + grnd_level +
                    '}';
        }
    }
}
