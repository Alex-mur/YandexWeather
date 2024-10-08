package org.example;

import java.util.ArrayList;

public class YandexWeatherData {
    public Fact fact;
    public ArrayList<Forecast> forecasts;
}

class Fact {
    Double temp;
}

class Forecast {
    public String date;
    public Parts parts;
}

class Parts {
    public Day day;
}

class Day {
    public double temp_avg;
}

