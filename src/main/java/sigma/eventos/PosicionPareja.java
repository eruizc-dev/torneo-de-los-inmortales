package sigma.eventos;

public class PosicionPareja {
	
	private final int idPareja;
	private final int ultimaRonda;
	private final int vidaFinal;
	
	public PosicionPareja(int idPareja, int ultimaRonda, int vidaFinal) {
		if(idPareja <= 0) {
			throw new IllegalArgumentException("El id de la pareja debe ser positivo");
		}
		
		if(ultimaRonda <= 0) {
			throw new IllegalArgumentException("La ronda debe ser mayor a cero");
		}
		
		if(vidaFinal < 0) {
			throw new IllegalArgumentException("La vida final no puede ser negativa");
		}
		
		this.idPareja = idPareja;
		this.ultimaRonda = ultimaRonda;
		this.vidaFinal = vidaFinal;
	}
	
	public int getIdPareja() {
		return idPareja;
	}
	
	public int getUltimaRonda() {
		return ultimaRonda;
	}
	
	public int getVidaFinal() {
		return vidaFinal;
	}
}
