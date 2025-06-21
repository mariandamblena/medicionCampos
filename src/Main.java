import implementacion.ColaPrioridad;
import tdas.ColaPrioridadTDA;

public class Main {
    public static void main(String[] args) {
        ColaPrioridadTDA cola = new ColaPrioridad();
        cola.inicializarCola();

        // Agregar valores con distintas prioridades (en este caso, los mm son la prioridad)
        cola.acolarPrioridad(12, 10); // Día 12, 10 mm
        cola.acolarPrioridad(13, 5);  // Día 13, 5 mm
        cola.acolarPrioridad(12, 3);  // Día 12 nuevamente con prioridad 3 (debería estar después)

        System.out.println(">>> Contenido de ColaPrioridadTDA (ordenado por prioridad descendente):");

        while (!cola.colaVacia()) {
            int dia = cola.primero();
            int mm = cola.prioridad();
            System.out.println("Día " + dia + ": " + mm + " mm");
            cola.desacolar();
        }
    }
}
