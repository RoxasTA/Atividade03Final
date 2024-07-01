package br.ucb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProcessadorDados {

    public void processarDados(String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray temperaturas = obj.getJSONObject("hourly").getJSONArray("temperature_2m");

        int horasPorDia = 24;
        int totalDias = temperaturas.length() / horasPorDia;

        List<Double> mediasDiarias = new ArrayList<>();
        List<Double> minimasDiarias = new ArrayList<>();
        List<Double> maximasDiarias = new ArrayList<>();

        for (int i = 0; i < totalDias; i++) {
            double somaTemperaturas = 0;
            double minima = Double.MAX_VALUE;
            double maxima = Double.MIN_VALUE;

            for (int j = 0; j < horasPorDia; j++) {
                double temperatura = temperaturas.getDouble(i * horasPorDia + j);
                somaTemperaturas += temperatura;
                if (temperatura < minima) {
                    minima = temperatura;
                }
                if (temperatura > maxima) {
                    maxima = temperatura;
                }
            }

            double media = somaTemperaturas / horasPorDia;
            mediasDiarias.add(media);
            minimasDiarias.add(minima);
            maximasDiarias.add(maxima);
        }

        // Exibir os resultados
        System.out.println("Resultados Diários:");
        for (int i = 0; i < totalDias; i++) {
            System.out.printf("Dia %d: Média = %.2f, Mínima = %.2f, Máxima = %.2f%n",
                    i + 1, mediasDiarias.get(i), minimasDiarias.get(i), maximasDiarias.get(i));
        }
    }
}



