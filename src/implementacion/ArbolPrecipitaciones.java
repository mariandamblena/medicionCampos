package implementacion;

import tdas.ABBPrecipitacionesTDA;
import tdas.ColaPrioridadTDA;
import tdas.ColaStringTDA;
import tdas.ConjuntoStringTDA;
import tdas.ConjuntoTDA;
import tdas.DiccionarioSimpleStringTDA;
import tdas.DiccionarioSimpleTDA;

public class ArbolPrecipitaciones implements ABBPrecipitacionesTDA {

	class nodoArbol {
		String campo;
		DiccionarioSimpleStringTDA mensualPrecipitaciones;
		ABBPrecipitacionesTDA hijoIzquierdo;
		ABBPrecipitacionesTDA hijoDerecho;
	}
	
	private nodoArbol raiz;
	
	@Override
	public void inicializar() {
		raiz = null;
	}

	@Override
	public void agregar(String valor) {
		nodoArbol nuevo = new nodoArbol();
		nuevo.campo = valor;
		nuevo.mensualPrecipitaciones = new DiccionarioSimpleString();

		if (raiz == null) {
			raiz = nuevo;
		} else {
			if (esMenor(valor, raiz.campo)) {
				if (raiz.hijoIzquierdo == null) {
					raiz.hijoIzquierdo = new ArbolPrecipitaciones();
					raiz.hijoIzquierdo.inicializar();
				}
				raiz.hijoIzquierdo.agregar(valor);
			} else if (esMayor(valor, raiz.campo)) {
				if (raiz.hijoDerecho == null) {
					raiz.hijoDerecho = new ArbolPrecipitaciones();
					raiz.hijoDerecho.inicializar();
				}
				raiz.hijoDerecho.agregar(valor);
			}
		}
	}

	@Override
	public void agregarMedicion(String valor, String anio, String mes, int dia, int precipitacion) {
		if (raiz == null) return;

		if (valor.equalsIgnoreCase(raiz.campo)) {
			String periodo = anio + mes;

			if (!diaValido(Integer.parseInt(anio), Integer.parseInt(mes), dia)) return;

			raiz.mensualPrecipitaciones.agregar(periodo, dia, precipitacion);

		} else if (esMenor(valor, raiz.campo)) {
			if (raiz.hijoIzquierdo != null) {
				raiz.hijoIzquierdo.agregarMedicion(valor, anio, mes, dia, precipitacion);
			}
		} else if (esMayor(valor, raiz.campo)) {
			if (raiz.hijoDerecho != null) {
				raiz.hijoDerecho.agregarMedicion(valor, anio, mes, dia, precipitacion);
			}
		}
	}


	@Override
	public void eliminar(String valor) {
		raiz = eliminarRecursivo(raiz, valor);
	}

	private nodoArbol obtenerMin(nodoArbol nodo) {
		ABBPrecipitacionesTDA subarbol = nodo.hijoIzquierdo;
		if (subarbol == null) return nodo;

		while (!subarbol.arbolVacio() && subarbol.hijoIzq() != null) {
			subarbol = subarbol.hijoIzq();
		}
		return ((ArbolPrecipitaciones) subarbol).raiz;
	}

	private nodoArbol eliminarRecursivo(nodoArbol actual, String valor) {
		if (actual == null) return null;

		if (valor.equalsIgnoreCase(actual.campo)) {
			if ((actual.hijoIzquierdo == null || actual.hijoIzquierdo.arbolVacio()) &&
					(actual.hijoDerecho == null || actual.hijoDerecho.arbolVacio())) {
				return null;
			}
			else if (actual.hijoIzquierdo == null || actual.hijoIzquierdo.arbolVacio()) {
				return ((ArbolPrecipitaciones) actual.hijoDerecho).raiz;
			}
			else if (actual.hijoDerecho == null || actual.hijoDerecho.arbolVacio()) {
				return ((ArbolPrecipitaciones) actual.hijoIzquierdo).raiz;
			}
			else {
				nodoArbol sucesor = obtenerMin(((ArbolPrecipitaciones) actual.hijoDerecho).raiz);
				actual.campo = sucesor.campo;
				actual.mensualPrecipitaciones = sucesor.mensualPrecipitaciones;
				(actual.hijoDerecho).eliminar(sucesor.campo);
			}
		} else if (esMenor(valor, actual.campo)) {
			if (actual.hijoIzquierdo != null) {
				(actual.hijoIzquierdo).eliminar(valor);
			}
		} else {
			if (actual.hijoDerecho != null) {
				(actual.hijoDerecho).eliminar(valor);
			}
		}

		return actual;
	}


	@Override
	public String raiz() {
		return raiz.campo;
	}

	@Override
	public void eliminarMedicion(String valor, String anio, String mes, int dia) {
		if (raiz == null) return;

		if (valor.equalsIgnoreCase(raiz.campo)) {
			String periodo = anio + mes;
			DiccionarioSimpleTDA diccDias = raiz.mensualPrecipitaciones.recuperar(periodo);
			if (diccDias != null) {
				diccDias.eliminar(dia);
			}
		} else if (esMenor(valor, raiz.campo)) {
			if (raiz.hijoIzquierdo != null) {
				raiz.hijoIzquierdo.eliminarMedicion(valor, anio, mes, dia);
			}
		} else {
			if (raiz.hijoDerecho != null) {
				raiz.hijoDerecho.eliminarMedicion(valor, anio, mes, dia);
			}
		}
	}

	@Override
	public ColaStringTDA periodos() {
		ColaStringTDA cola = new ColaString();
		cola.inicializarCola();
		if (raiz == null) return cola;

		ConjuntoStringTDA cs = raiz.mensualPrecipitaciones.claves();
		cs.inicializar();
		while (!cs.estaVacio()) {
			String p = cs.elegir();
			cola.acolar(p);
			cs.sacar(p);
		}
		return cola;
	}
	@Override
	public ColaPrioridadTDA precipitaciones(String periodo) {
		ColaPrioridadTDA cola = new ColaPrioridad();
		cola.inicializarCola();

		if (raiz == null) return cola;

		DiccionarioSimpleTDA dias = raiz.mensualPrecipitaciones.recuperar(periodo);
		if (dias != null) {
			ConjuntoTDA claves = dias.obtenerClaves();
			//claves.inicializar();
			while (!claves.estaVacio()) {
				int dia = claves.elegir();
				int cantidad = dias.recuperar(dia);
				cola.acolarPrioridad(dia, cantidad);
				claves.sacar(dia);
			}
		}
		return cola;
	}


	@Override
	public ABBPrecipitacionesTDA hijoIzq() {
		return raiz != null ? raiz.hijoIzquierdo : null;
	}

	@Override
	public ABBPrecipitacionesTDA hijoDer() {
		return raiz != null ? raiz.hijoDerecho : null;
	}

	@Override
	public boolean arbolVacio() {
		return raiz == null;

	}

	private boolean esMenor(String v1, String v2) {
		return v1.compareToIgnoreCase(v2) < 0;
	}

	private boolean esMayor(String v1, String v2) {
		return v1.compareToIgnoreCase(v2) > 0;
	}
	private boolean diaValido(int anio, int mes, int dia) {
		if (mes < 1 || mes > 12 || anio < 0) return false;

		int[] diasPorMes = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		return dia >= 1 && dia <= diasPorMes[mes - 1];
	}

}
