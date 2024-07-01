package br.ucb;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class ClimaService {
    private static final String API_URL = "https://archive-api.open-meteo.com/v1/archive?latitude=%f&longitude=%f&start_date=2024-01-01&end_date=2024-01-31&hourly=temperature_2m";

    public String obterDadosClimaticos(double latitude, double longitude) throws IOException {
        // Usar Locale.US para garantir que o ponto seja usado como separador decimal
        String urlString = String.format(Locale.US, API_URL, latitude, longitude);
        System.out.println("Request URL: " + urlString);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        System.out.println("HTTP Response Code: " + responseCode);
        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        Scanner sc = new Scanner(url.openStream());
        StringBuilder inline = new StringBuilder();
        while (sc.hasNext()) {
            inline.append(sc.nextLine());
        }
        sc.close();

        return inline.toString();
    }
}






