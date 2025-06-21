import implementacion.ArbolPrecipitaciones;
import implementacion.DiccionarioSimple;
import implementacion.Conjunto;
import tdas.ABBPrecipitacionesTDA;
import tdas.ConjuntoTDA;

public class Main {
    public static void main(String[] args) {
        // Crear el árbol de precipitaciones
        ABBPrecipitacionesTDA arbol = new ArbolPrecipitaciones();
        arbol.inicializar();

        // Agregar campo
        arbol.agregar("CampoA");

        // Agregar precipitaciones al campo (año y mes separados como String)
        arbol.agregarMedicion("CampoA", "2024", "06", 12, 10); // 10 mm el 12 de junio 2024
        arbol.agregarMedicion("CampoA", "2024", "06", 13, 5);  // 5 mm el 13 de junio
        arbol.agregarMedicion("CampoA", "2024", "06", 12, 3);  // suma 3 mm más al 12

        // Simular un diccionario directamente
        DiccionarioSimple dic = new DiccionarioSimple();
        dic.inicializar();
        dic.agregar(1, 100);
        dic.agregar(2, 200);
        dic.agregar(1, 50); // debería sumar a la clave 1

        ConjuntoTDA claves = dic.obtenerClaves();
        while (!claves.estaVacio()) {
            int k = claves.elegir();
            claves.sacar(k);
            int v = dic.recuperar(k);
            System.out.println("Día: " + k + " -> " + v + " mm");
        }
    }
}
