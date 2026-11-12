package sigma.eventos;

public class Acontecimiento {
	
	private final TipoAcontecimiento tipo;
	private final int ronda;
	private final String descripcion;
	
	public Acontecimiento(TipoAcontecimiento tipo, int ronda, String descripcion) {
		if(tipo == null) {
			throw new IllegalArgumentException("El tipo no puede ser nulo");
		}
		
		if(descripcion == null || descripcion.trim().isEmpty()) {
			throw new IllegalArgumentException("La descripcion no puede estar vacia");
		}
		
		this.tipo = tipo;
		this.ronda = ronda;
		this.descripcion = descripcion;
	}
	
	public TipoAcontecimiento getTipoAcontecimiento() {
		return tipo;
	}
	
	public int getRonda() {
		return ronda;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
}
