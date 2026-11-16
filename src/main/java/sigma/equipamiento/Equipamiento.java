package sigma.equipamiento;

public class Equipamiento {
	
	private final String nombre;
	private final CategoriaEquipamiento categoria;
	
	private final int bonoDanio;
	private final int reduccionDanio;
	private final int bonoVidaMaxima;
	private final int bonoEnergiaMaxima;
	
	public Equipamiento(String nombre, CategoriaEquipamiento categoria,
            int bonoDanio, int reduccionDanio,
            int bonoVidaMaxima, int bonoEnergiaMaxima) {
		
		if(nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede estar vacio");
		}
		
		if(categoria == null) {
			throw new IllegalArgumentException("La categoria no puede ser nula");
		}
		
		if(bonoDanio < 0 || bonoVidaMaxima < 0 || bonoEnergiaMaxima < 0) {
			throw new IllegalArgumentException("Las bonificaciones no pueden ser negativas");
		}
		
		if(reduccionDanio < 0 || reduccionDanio > 100) {
			throw new IllegalArgumentException("La reduccion de daño debe estar entre 0 y 100");
		}
		
		this.nombre = nombre;
		this.categoria = categoria;
		this.bonoDanio = bonoDanio;
		this.reduccionDanio = reduccionDanio;
		this.bonoVidaMaxima = bonoVidaMaxima;
		this.bonoEnergiaMaxima = bonoEnergiaMaxima;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public CategoriaEquipamiento getCategoriaEquipamiento() {
		return categoria;
	}
	
	public int getBonoDanio() {
		return bonoDanio;
	}
	
	public int getReduccionDanio() {
		return reduccionDanio;
	}
	
	public int getBonoVidaMaxima() {
		return bonoVidaMaxima;
	}
	
	public int getBonoEnergiaMaxima() {
		return bonoEnergiaMaxima;
	}
}
