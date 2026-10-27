package sigma;

import java.util.ArrayList;
import java.util.List;

public class Escuela {
    private final String nombre;
    private final List<Tecnica> tecnicasIniciales;
    private final List<Equipamiento> equipamientoInicial;

    // TODOImplementar y reemplazar por el constructor completo
    @Deprecated
	public Escuela() {
		this.nombre = "Escuela de ejemplo";
		this.tecnicasIniciales = new ArrayList<>();
		this.equipamientoInicial = new ArrayList<>();
    }

	public Escuela(String nombre, List<Tecnica> tecnicasIniciales, List<Equipamiento> equipamientoInicial) {
		this.nombre = nombre;
		this.tecnicasIniciales = tecnicasIniciales;
		this.equipamientoInicial = equipamientoInicial;
	}

    // TODO hacer getters

    public static Escuela newEscuelaDeLaEspadaCelestial() {
        // TODO implementar
        return new Escuela();
    }

    public static Escuela newPalacioDelVacio() {
        // TODO implementar
        return new Escuela();
    }

    public static Escuela newMonasterioDelCuerpoDorado() {
        // TODO implementar
        return new Escuela();
    }
}
