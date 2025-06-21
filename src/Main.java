import implementacion.ArbolPrecipitaciones;
import tdas.ABBPrecipitacionesTDA;
import tdas.ColaPrioridadTDA;
import tdas.ColaStringTDA;
import algoritmos.Algoritmos;

public class Main {
    public static void main(String[] args) {
        ABBPrecipitacionesTDA arbol = new ArbolPrecipitaciones();
        arbol.inicializar();
        Algoritmos algoritmos = new Algoritmos(arbol);

        // Carga de datos

        arbol.agregar("CampoA");
        arbol.agregar("CampoB");
        arbol.agregar("CampoC");
        algoritmos.agregarMedicion("CampoA", 2025, 7, 10, 20);
        algoritmos.agregarMedicion("CampoA", 2025, 7, 11, 25);
        algoritmos.agregarMedicion("CampoB", 2025, 7, 10, 20);
        algoritmos.agregarMedicion("CampoB", 2025, 7, 11, 10);
        algoritmos.agregarMedicion("CampoC", 2025, 7, 11, 15);
        algoritmos.agregarMedicion("CampoC", 2025, 7, 15, 25);
        algoritmos.agregarMedicion("CampoC", 2025, 7, 16, 30);

        // Promedio diario julio 2025
        System.out.println("\n\uD83D\uDCCA Promedio diario julio 2025 (todos los campos):");
        ColaPrioridadTDA resumen = algoritmos.medicionesMes(2025, 7);
        while (!resumen.colaVacia()) {
            System.out.println("D\u00eda " + resumen.primero() + ": " + resumen.prioridad() + " mm promedio");
            resumen.desacolar();
        }

        // Mediciones CampoA julio 2025
        System.out.println("\n\uD83C\uDF3E Mediciones en CampoA (julio 2025):");
        ColaPrioridadTDA datosCampo = algoritmos.medicionesCampoMes("CampoA", 2025, 7);
        while (!datosCampo.colaVacia()) {
            System.out.println("D\u00eda " + datosCampo.primero() + ": " + datosCampo.prioridad() + " mm");
            datosCampo.desacolar();
        }

        // Mes más lluvioso
        System.out.println("\n\uD83C\uDF27\uFE0F Mes más lluvioso: " + algoritmos.mesMasLluvioso());

        // Promedio lluvia día 11
        System.out.println("\n\uD83D\uDCC8 Promedio d\u00eda 11/07/2025: " + algoritmos.promedioLluviaEnUnDia(2025, 7, 11) + " mm");

        // Campo más lluvioso
        System.out.println("\n\uD83C\uDF1F Campo más lluvioso en la historia: " + algoritmos.campoMasLLuvisoHistoria());

        // Campos con lluvia mayor al promedio
        System.out.println("\n\uD83D\uDEA9 Campos con lluvias mayores al promedio en julio 2025:");
        ColaStringTDA mayores = algoritmos.camposConLLuviaMayorPromedio(2025, 7);
        if (mayores.colaVacia()) {
            System.out.println("(No se encontraron campos con lluvia superior al promedio)");
        } else {
            while (!mayores.colaVacia()) {
                System.out.println("\uD83C\uDF31 " + mayores.primero());
                mayores.desacolar();
            }
        }

        // Test final con diferencia forzada
        System.out.println("\n\uD83D\uDEA9 Campos con lluvias mayores al promedio en julio 2025 (test forzado):");
        algoritmos.agregarMedicion("CampoA", 2025, 7, 20, 100);
        algoritmos.agregarMedicion("CampoB", 2025, 7, 20, 10);
        algoritmos.agregarMedicion("CampoC", 2025, 7, 20, 10);

        ColaStringTDA forzado = algoritmos.camposConLLuviaMayorPromedio(2025, 7);
        if (forzado.colaVacia()) {
            System.out.println("(No se encontraron campos con lluvia superior al promedio)");
        } else {
            while (!forzado.colaVacia()) {
                System.out.println("\uD83C\uDF31 " + forzado.primero());
                forzado.desacolar();
            }
        }
    }
}
