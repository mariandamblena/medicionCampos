import implementacion.ArbolPrecipitaciones;
import algoritmos.Algoritmos;
import tdas.ABBPrecipitacionesTDA;
import tdas.ColaPrioridadTDA;

public class Main {
    public static void main(String[] args) {
        // Crear árbol y cargar datos
        ABBPrecipitacionesTDA arbol = new ArbolPrecipitaciones();
        arbol.inicializar();

        arbol.agregar("CampoX");
        arbol.agregar("CampoB");
        arbol.agregar("CampoZ");

        arbol.agregarMedicion("CampoX", "2025", "07", 10, 15);
        arbol.agregarMedicion("CampoX", "2025", "07", 11, 20);
        arbol.agregarMedicion("CampoX", "2025", "07", 12, 5);

        arbol.agregarMedicion("CampoB", "2025", "07", 8, 12);
        arbol.agregarMedicion("CampoB", "2025", "07", 9, 9);
        arbol.agregarMedicion("CampoB", "2025", "07", 10, 7);

        arbol.agregarMedicion("CampoZ", "2025", "07", 15, 25);
        arbol.agregarMedicion("CampoZ", "2025", "07", 16, 30);

        // Instanciar clase Algoritmos con el árbol cargado
        Algoritmos algoritmo = new Algoritmos(arbol);

        // Probar mesMasLluvioso
        int mesMasLluvia = algoritmo.mesMasLluvioso();
        System.out.println("\n🌧️ Mes más lluvioso (número de mes): " + mesMasLluvia);

        // Probar campo más lluvioso en la historia
        String campoMax = algoritmo.campoMasLLuvisoHistoria();
        System.out.println("🌾 Campo con más lluvia histórica: " + campoMax);

        // Probar promedio de lluvia en un día específico
        float promedioDia = algoritmo.promedioLluviaEnUnDia(2025, 7, 10);
        System.out.println("📊 Promedio lluvia el 10/07/2025: " + promedioDia + " mm");

        // Ver precipitaciones en CampoZ
        System.out.println("\n📌 Lluvias en CampoZ (julio 2025):");
        ColaPrioridadTDA datos = algoritmo.medicionesCampoMes("CampoZ", 2025, 7);
        while (!datos.colaVacia()) {
            int dia = datos.primero();
            int mm = datos.prioridad();
            System.out.println("Día " + dia + ": " + mm + " mm");
            datos.desacolar();

        }
    }
}
