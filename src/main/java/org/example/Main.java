package org.example;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Main {
    private static final String API_KEY = "ВСТАВЬТЕ СЮДА ВАШ КЛЮЧ";
    private static final String REQUEST_ADDRESS = "https://api.weather.yandex.ru/v2/forecast?lat=43.580749&lon=39.723327";

    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REQUEST_ADDRESS))
                .header("X-Yandex-Weather-Key", API_KEY)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // для преобразования json в объект я использую библиотеку gson
            // мой простой data-класс YandexWeatherData содержит только необходимые по заданию поля
            Gson gson = new Gson();
            YandexWeatherData weather = gson.fromJson(response.body(), YandexWeatherData.class);

            // вычисление средней дневной температуры по прогнозу за несколько дней
            double temperatureSummary = 0;
            for (Forecast forecast : weather.forecasts) {
                temperatureSummary += forecast.parts.day.temp_avg;
            }
            double averageTemperature = round(temperatureSummary / weather.forecasts.size(), 1);
            String dateBegin = weather.forecasts.get(0).date;
            String dateEnd = weather.forecasts.get(weather.forecasts.size() - 1).date;

            System.out.println("Температура в городе Сочи");
            System.out.println("Ответ сервера: " + response.body());
            System.out.println("Температура сейчас: " + weather.fact.temp + " градусов цельсия");
            System.out.println("Средняя дневная температура за период с [" + dateBegin + "] по [" + dateEnd + "]: " + averageTemperature + " градусов цельсия");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}