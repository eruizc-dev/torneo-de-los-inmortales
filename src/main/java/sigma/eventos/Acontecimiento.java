package sigma.eventos;

public class Acontecimiento {
	
	private final TipoAcontecimiento tipo;
	private final int ronda;
	private final String descripcion;
	private final ResultadoCombate resultado;
	
	public Acontecimiento(TipoAcontecimiento tipo, int ronda, String descripcion) {
		if(tipo == null) {
			throw new IllegalArgumentException("El tipo no puede ser nulo");
		}
		
		if(tipo == TipoAcontecimiento.RESULTADO_COMBATE) {
			throw new IllegalArgumentException("Un acontecimiento de resultado debe incluir los datos del combate");
		}
		
		if(descripcion == null || descripcion.trim().isEmpty()) {
			throw new IllegalArgumentException("La descripcion no puede estar vacia");
		}
		
		this.tipo = tipo;
		this.ronda = ronda;
		this.descripcion = descripcion;
		this.resultado = null;
	}
	
	public Acontecimiento(TipoAcontecimiento tipo, int ronda, String descripcion, ResultadoCombate resultado) {
		if(tipo != TipoAcontecimiento.RESULTADO_COMBATE) {
			throw new IllegalArgumentException("El resultado solo corresponde a un acontecimiento de resultado de combate");
		}
		
		if(resultado == null || ronda != resultado.getRonda()) {
			throw new IllegalArgumentException("Resultado de combate invalido");
		}
		
		if(descripcion == null || descripcion.trim().isEmpty()) {
			throw new IllegalArgumentException("La descripcion no puede estar vacia");
		}
		
		this.tipo = tipo;
		this.ronda = ronda;
		this.descripcion = descripcion;
		this.resultado = resultado;
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
	
	public ResultadoCombate getResultadoCombate() {
		return resultado;
	}
}
