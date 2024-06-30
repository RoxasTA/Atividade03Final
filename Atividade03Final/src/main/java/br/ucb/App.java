package br.ucb;

import br.ucb.Capital;
import br.ucb.ClimaService;
import br.ucb.ProcessadorDados;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    private static List<Capital> capitais = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        carregarCapitais();

        System.out.println("Versão de referência (sem threads):");
        executarExperimento(1, false);  // Sem threads

        System.out.println("Versão com 3 threads:");
        executarExperimento(3, true);  // Com 3 threads

        System.out.println("Versão com 9 threads:");
        executarExperimento(9, true);  // Com 9 threads

        System.out.println("Versão com 27 threads:");
        executarExperimento(27, true); // Com 27 threads
    }

    private static void carregarCapitais() {
        capitais.add(new Capital("Rio Branco", -9.97499, -67.8243));
        capitais.add(new Capital("Maceió", -9.66599, -35.735));
        capitais.add(new Capital("Macapá", 0.0389, -51.0664));
        capitais.add(new Capital("Manaus", -3.10194, -60.025));
        capitais.add(new Capital("Salvador", -12.9714, -38.5014));
        capitais.add(new Capital("Fortaleza", -3.71722, -38.5434));
        capitais.add(new Capital("Brasília", -15.7801, -47.9292));
        capitais.add(new Capital("Vitória", -20.3155, -40.3128));
        capitais.add(new Capital("Goiânia", -16.6864, -49.2643));
        capitais.add(new Capital("São Luís", -2.53073, -44.3068));
        capitais.add(new Capital("Cuiabá", -15.601, -56.0974));
        capitais.add(new Capital("Campo Grande", -20.4428, -54.6464));
        capitais.add(new Capital("Belo Horizonte", -19.9167, -43.9345));
        capitais.add(new Capital("Belém", -1.45502, -48.5024));
        capitais.add(new Capital("João Pessoa", -7.11509, -34.8641));
        capitais.add(new Capital("Curitiba", -25.4284, -49.2733));
        capitais.add(new Capital("Recife", -8.04756, -34.877));
        capitais.add(new Capital("Teresina", -5.08921, -42.8016));
        capitais.add(new Capital("Rio de Janeiro", -22.9035, -43.2096));
        capitais.add(new Capital("Natal", -5.79448, -35.211));
        capitais.add(new Capital("Porto Alegre", -30.0331, -51.23));
        capitais.add(new Capital("Porto Velho", -8.76077, -63.8999));
        capitais.add(new Capital("Boa Vista", 2.81972, -60.6733));
        capitais.add(new Capital("Florianópolis", -27.5954, -48.548));
        capitais.add(new Capital("São Paulo", -23.5505, -46.6333));
        capitais.add(new Capital("Aracaju", -10.9472, -37.0731));
        capitais.add(new Capital("Palmas", -10.2045, -48.3603));
    }

    private static void executarExperimento(int numThreads, boolean usarThreads) throws InterruptedException {
        ClimaService climaService = new ClimaService();
        ProcessadorDados processador = new ProcessadorDados();
        long somaTempos = 0;

        for (int i = 0; i < 10; i++) {
            long startTime = System.currentTimeMillis();

            if (usarThreads) {
                ExecutorService executor = Executors.newFixedThreadPool(numThreads);
                for (Capital capital : capitais) {
                    executor.submit(() -> {
                        try {
                            String dados = climaService.obterDadosClimaticos(capital.getLatitude(), capital.getLongitude());
                            processador.processarDados(dados);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.HOURS);
            } else {
                for (Capital capital : capitais) {
                    try {
                        String dados = climaService.obterDadosClimaticos(capital.getLatitude(), capital.getLongitude());
                        processador.processarDados(dados);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            long endTime = System.currentTimeMillis();
            long tempoExecucao = endTime - startTime;
            somaTempos += tempoExecucao;

            System.out.println("Rodada " + (i + 1) + ": " + tempoExecucao + " ms");
        }

        long tempoMedio = somaTempos / 10;
        System.out.println("Tempo médio com " + (usarThreads ? numThreads + " threads" : "sem threads") + ": " + tempoMedio + " ms");
    }
}
