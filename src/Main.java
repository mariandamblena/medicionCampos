import implementacion.ArbolPrecipitaciones;
import tdas.ABBPrecipitacionesTDA;
import algoritmos.Algoritmos;
import tdas.ColaPrioridadTDA;
import tdas.ColaStringTDA;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ABBPrecipitacionesTDA arbol = new ArbolPrecipitaciones();
        arbol.inicializar();

        String[] campos = {"Mithras", "Capella", "Acrisia", "Ampion", "Arcadia"};
        String[] periodos = {
                "202401", "202402", "202403", "202404", "202405", "202406",
                "202501", "202502", "202503", "202504", "202505", "202506"
        };

        Random r = new Random();
        for (String campo : campos) {
            for (String periodo : periodos) {
                int anio = Integer.parseInt(periodo.substring(0, 4));
                int mes = Integer.parseInt(periodo.substring(4));
                int dias = (mes == 2 ? 28 : 30);
                int cantidad = r.nextInt(10) + 1;
                for (int x = 0; x < cantidad; x++) {
                    int dia = r.nextInt(dias) + 1;
                    int precipitacion = r.nextInt(200) + 20;
                    arbol.agregarMedicion(campo, String.valueOf(anio), String.format("%02d", mes), dia, precipitacion);
                }
            }
        }

        Algoritmos alg = new Algoritmos(arbol);

        // ✅ Medición forzada para Mithras el 15/06/2025
        alg.agregarMedicion("Mithras", 2025, 6, 15, 100);

        System.out.println("Carga de datos finalizada.");

        // 🔍 Construcción del informe
        StringBuilder salida = new StringBuilder();
        salida.append(">>> Resultados de prueba\n\n");

        salida.append("Mes más lluvioso: ").append(alg.mesMasLluvioso()).append("\n");
        salida.append("Promedio lluvia 2025/06 día 15: ")
                .append(alg.promedioLluviaEnUnDia(2025, 6, 15)).append(" mm\n");
        salida.append("Campo más lluvioso en la historia: ")
                .append(alg.campoMasLLuvisoHistoria()).append("\n\n");

        // 📋 Mediciones de Mithras en junio 2025
        salida.append(">>> Mediciones para Mithras en junio 2025:\n");
        ColaPrioridadTDA datos = alg.medicionesCampoMes("Mithras", 2025, 6);
        datos.inicializarCola();
        boolean hayDatos = false;
        while (!datos.colaVacia()) {
            int dia = datos.primero();
            int mm = datos.prioridad();
            salida.append("Día ").append(dia).append(": ").append(mm).append(" mm\n");
            datos.desacolar();
            hayDatos = true;
        }
        if (!hayDatos) {
            salida.append("(Sin datos)\n");
        }

        // 🧪 Diagnóstico: verificar periodos para Mithras
        salida.append("\n>>> Periodos disponibles en Mithras:\n");
        ColaStringTDA periodosMithras = obtenerPeriodosCampo(arbol, "Mithras");
        periodosMithras.inicializarCola();
        boolean hayPeriodos = false;
        while (!periodosMithras.colaVacia()) {
            String periodo = periodosMithras.primero();
            salida.append("- ").append(periodo).append("\n");
            periodosMithras.desacolar();
            hayPeriodos = true;
        }
        if (!hayPeriodos) {
            salida.append("(No se encontraron periodos para Mithras)\n");
        }

        // 💾 Guardar resultados en archivo
        try (FileWriter writer = new FileWriter("resultados.txt")) {
            writer.write(salida.toString());
            System.out.println("\n✅ Resultados guardados en resultados.txt");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }

        // 📤 Mostrar en consola también
        System.out.println("\n" + salida);
    }

    // 🔍 Búsqueda recursiva para obtener periodos del campo
    private static ColaStringTDA obtenerPeriodosCampo(ABBPrecipitacionesTDA nodo, String campo) {
        if (nodo == null || nodo.arbolVacio()) {
            ColaStringTDA vacia = new implementacion.ColaString();
            vacia.inicializarCola();
            return vacia;
        }
        if (nodo.raiz().equalsIgnoreCase(campo)) {
            return nodo.periodos();
        }
        ColaStringTDA izq = obtenerPeriodosCampo(nodo.hijoIzq(), campo);
        if (!izq.colaVacia()) return izq;

        return obtenerPeriodosCampo(nodo.hijoDer(), campo);
    }
}
