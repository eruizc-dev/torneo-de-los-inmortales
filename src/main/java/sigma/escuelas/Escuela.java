package sigma.escuelas;

import java.util.ArrayList;
import java.util.List;

import sigma.equipamiento.Equipamiento;
import sigma.tecnicas.Tecnica;

public class Escuela {
    private final String nombre;
    private final List<Tecnica> tecnicasIniciales;
    private final List<Equipamiento> equipamientoInicial;

	public Escuela(String nombre, List<Tecnica> tecnicasIniciales, List<Equipamiento> equipamientoInicial) {
		
		if(nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre de la escuela no puede estar vacio");
		}
		
		if(tecnicasIniciales == null || equipamientoInicial == null) {
			throw new IllegalArgumentException("Las listas no pueden ser nulas");
		}
		this.nombre = nombre;
		this.tecnicasIniciales = new ArrayList<>(tecnicasIniciales);
		this.equipamientoInicial = new ArrayList<>(equipamientoInicial);
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public List<Tecnica> getTecnicasIniciales() {
		return new ArrayList<>(tecnicasIniciales);
	}
	
	public List<Equipamiento> getEquipamientoInicial() {
		return new ArrayList<>(equipamientoInicial);
	}
}
