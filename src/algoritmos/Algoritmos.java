package algoritmos;

import implementacion.ColaString;
import implementacion.Conjunto;
import implementacion.ConjuntoString;
import tdas.ABBPrecipitacionesTDA;
import tdas.ColaPrioridadTDA;
import tdas.ColaStringTDA;
import tdas.ConjuntoStringTDA;
import tdas.ConjuntoTDA;

public class Algoritmos {
	private ABBPrecipitacionesTDA arbol;

	public Algoritmos(ABBPrecipitacionesTDA arbol) {
		this.arbol = arbol;
	}

	/**
	 * Agrega una medicion a un campo determinado, en una fecha determinada
	 * */
	public void agregarMedicion(String campo, int anio, int mes, int dia, int precipitacion) {
		arbol.agregarMedicion(campo, String.valueOf(anio), formatMes(mes), dia, precipitacion);
	}

	/**
	 * Elimina una medicions a un campo determinado, en una fecha determinada
	 * */
	public void eliminarMedicion(String campo, int anio, int mes, int dia) {
		arbol.eliminarMedicion(campo, String.valueOf(anio), formatMes(mes), dia);
	}

	/**
	 * Elimina un campo determinado recibido como parametro
	 * */
	public void eliminarCampo(String campo) {
		arbol.eliminar(campo);
	}

	/**
	 * Devuelve una cola con prioridad con las precipitaciones promedio de cada dia de un mes y año
	 * determinado en todos los campos
	 * */
	public ColaPrioridadTDA medicionesMes(int anio, int mes) {
		String periodo = armarPeriodo(anio, mes);
		int diasDelMes = diasEnMes(anio, mes);

		ColaPrioridadTDA resultado = new implementacion.ColaPrioridad();
		resultado.inicializarCola();

		for (int dia = 1; dia <= diasDelMes; dia++) {
			float promedio = promedioLluviaEnUnDia(anio, mes, dia);
			if (promedio > 0) {
				resultado.acolarPrioridad(dia, (int) promedio);
			}
		}

		return resultado;
	}


	/**
	 * Devuelve una cola con prioridad con las precipitaciones de cada dia de un mes y año
	 * determinado en un campo determinado
	 * */
	public ColaPrioridadTDA medicionesCampoMes(String campo, int anio, int mes) {
		String periodo = armarPeriodo(anio, mes);
		return buscarCampoYObtenerPrecipitaciones(arbol, campo, periodo);
	}

	private ColaPrioridadTDA buscarCampoYObtenerPrecipitaciones(ABBPrecipitacionesTDA nodo, String campo, String periodo) {
		if (nodo == null || nodo.arbolVacio()) {
			ColaPrioridadTDA vacia = new implementacion.ColaPrioridad();
			vacia.inicializarCola();
			return vacia;
		}

		if (nodo.raiz().equalsIgnoreCase(campo)) {
			return nodo.precipitaciones(periodo);
		}

		ColaPrioridadTDA resultado = buscarCampoYObtenerPrecipitaciones(nodo.hijoIzq(), campo, periodo);
		if (resultado != null && !resultado.colaVacia()) return resultado;

		return buscarCampoYObtenerPrecipitaciones(nodo.hijoDer(), campo, periodo);
	}

	/**
	 * Devuelve el numero de mes donde mas llovio entre todos los meses de todos los años de cualquier campo
	 * */
	public int mesMasLluvioso() {
		int[] sumaPorMes = new int[12];
		acumularLluviasPorMes(arbol, sumaPorMes);

		int maxMes = 0;
		for (int i = 1; i < 12; i++) {
			if (sumaPorMes[i] > sumaPorMes[maxMes]) {
				maxMes = i;
			}
		}

		return maxMes + 1;
	}

	private void acumularLluviasPorMes(ABBPrecipitacionesTDA nodo, int[] sumaPorMes) {
		if (nodo == null || nodo.arbolVacio()) return;

		ColaStringTDA periodos = nodo.periodos();
		// periodos.inicializarCola();

		while (!periodos.colaVacia()) {
			String periodo = periodos.primero();
			periodos.desacolar();
			int mes = Integer.parseInt(periodo.substring(4, 6)) - 1;
			ColaPrioridadTDA precipitaciones = nodo.precipitaciones(periodo);
			precipitaciones.inicializarCola();
			while (!precipitaciones.colaVacia()) {
				sumaPorMes[mes] += precipitaciones.prioridad();
				precipitaciones.desacolar();
			}
		}
		acumularLluviasPorMes(nodo.hijoIzq(), sumaPorMes);
		acumularLluviasPorMes(nodo.hijoDer(), sumaPorMes);
	}


	/**
	 * Devuelve el promedio de precipitaciones caidas en un dia, mes y anio determinado en todos los campos
	 * */
	public float promedioLluviaEnUnDia(int anio, int mes, int dia) {
		String periodo = armarPeriodo(anio, mes);
		int[] suma = new int[]{0};
		int[] cantidad = new int[]{0};
		recorrerYSumar(arbol, periodo, dia, suma, cantidad);
		return cantidad[0] > 0 ? (float) suma[0] / cantidad[0] : 0;
	}

	private void recorrerYSumar(ABBPrecipitacionesTDA nodo, String periodo, int dia, int[] suma, int[] cantidad) {
		if (nodo == null || nodo.arbolVacio()) return;
		ColaPrioridadTDA cola = nodo.precipitaciones(periodo);
		//cola.inicializarCola();
		while (!cola.colaVacia()) {
			int actualDia = cola.primero();
			int mm = cola.prioridad();
			if (actualDia == dia) {
				suma[0] += mm;
				cantidad[0]++;
			}
			cola.desacolar();
		}
		recorrerYSumar(nodo.hijoIzq(), periodo, dia, suma, cantidad);
		recorrerYSumar(nodo.hijoDer(), periodo, dia, suma, cantidad);
	}


	/**
	 * Devuelve el campo que recibio mas lluvia
	 * */
	public String campoMasLLuvisoHistoria() {
		return campoMasLluviosoRecursiva(arbol, new String[]{null}, new int[]{-1});
	}

	private String campoMasLluviosoRecursiva(ABBPrecipitacionesTDA nodo, String[] campoMax, int[] lluviaMax) {
		if (nodo == null || nodo.arbolVacio()) return campoMax[0];

		int totalLluviaCampo = 0;

		ColaStringTDA periodos = nodo.periodos();
		//periodos.inicializarCola();

		while (!periodos.colaVacia()) {
			String periodo = periodos.primero();
			periodos.desacolar();

			ColaPrioridadTDA precipitaciones = nodo.precipitaciones(periodo);
			precipitaciones.inicializarCola();

			while (!precipitaciones.colaVacia()) {
				totalLluviaCampo += precipitaciones.prioridad();
				precipitaciones.desacolar();
			}
		}

		if (totalLluviaCampo > lluviaMax[0]) {
			lluviaMax[0] = totalLluviaCampo;
			campoMax[0] = nodo.raiz();
		}

		campoMasLluviosoRecursiva(nodo.hijoIzq(), campoMax, lluviaMax);
		campoMasLluviosoRecursiva(nodo.hijoDer(), campoMax, lluviaMax);

		return campoMax[0];
	}

	/**
	 * Devuelve los campos con una cantidad de lluvia en un periodo determinado que es mayor al
	 * promedio de lluvia en un periodo determinado
	 * */
	public ColaStringTDA camposConLLuviaMayorPromedio(int anio, int mes) {
		String periodo = armarPeriodo(anio, mes);
		ConjuntoStringTDA campos = new ConjuntoString();
		campos.inicializar();
		ConjuntoTDA lluvias = new Conjunto();
		lluvias.inicializar();
		acumularLluvias(arbol, periodo, campos, lluvias);
		int sumaTotal = 0, cantidad = 0;
		while (!lluvias.estaVacio()) {
			sumaTotal += lluvias.elegir();
			lluvias.sacar(lluvias.elegir());
			cantidad++;
		}
		float promedio = cantidad > 0 ? (float) sumaTotal / cantidad : 0;
		ColaStringTDA resultado = new ColaString();
		resultado.inicializarCola();
		while (!campos.estaVacio()) {
			String campo = campos.elegir();
			int lluvia = calcularLluviaCampo(arbol, campo, periodo);
			if (lluvia > promedio) {
				resultado.acolar(campo);
			}
			campos.sacar(campo);
		}
		return resultado;
	}

	private void acumularLluvias(ABBPrecipitacionesTDA nodo, String periodo, ConjuntoStringTDA campos, ConjuntoTDA lluvias) {
		if (nodo == null || nodo.arbolVacio()) return;
		int suma = calcularLluviaCampo(nodo, nodo.raiz(), periodo);
		if (suma > 0) {
			campos.agregar(nodo.raiz());
			lluvias.agregar(suma);
		}
		acumularLluvias(nodo.hijoIzq(), periodo, campos, lluvias);
		acumularLluvias(nodo.hijoDer(), periodo, campos, lluvias);
	}

	private int calcularLluviaCampo(ABBPrecipitacionesTDA nodo, String campo, String periodo) {
		if (nodo == null || nodo.arbolVacio()) return 0;
		if (nodo.raiz().equalsIgnoreCase(campo)) {
			int suma = 0;
			ColaPrioridadTDA datos = nodo.precipitaciones(periodo);
			datos.inicializarCola();
			while (!datos.colaVacia()) {
				suma += datos.prioridad();
				datos.desacolar();
			}
			return suma;
		}
		int izq = calcularLluviaCampo(nodo.hijoIzq(), campo, periodo);
		int der = calcularLluviaCampo(nodo.hijoDer(), campo, periodo);
		return izq + der;
	}


	private String formatMes(int mes) {
		return (mes < 10 ? "0" : "") + mes;
	}
	private String armarPeriodo(int anio, int mes) {
		return anio + formatMes(mes);
	}
	private int diasEnMes(int anio, int mes) {
		int[] dias = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		return dias[mes - 1];
	}

}
