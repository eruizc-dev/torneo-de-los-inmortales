package sigma.eventos;

public class ResultadoCombate {

	private final int ronda;
	
	private final int idParejaUno;
	private final int idParejaDos;
	private final int idParejaGanadora;
	
	private final int vidaFinalParejaUno;
	private final int vidaFinalParejaDos;
	
	public ResultadoCombate(int ronda, int idParejaUno, int idParejaDos, int idParejaGanadora, int vidaFinalParejaUno, int vidaFinalParejaDos) {
		if(ronda <= 0) {
			throw new IllegalArgumentException("La ronda debe ser positiva");		
		}
		
		if(idParejaUno <= 0 || idParejaDos <= 0 || idParejaUno == idParejaDos) {
			throw new IllegalArgumentException("Identificadores de pareja invalidos");
		}
		
		if(idParejaGanadora != idParejaUno && idParejaGanadora != idParejaDos) {
			throw new IllegalArgumentException("La pareja ganadora debe ser una de las dos parejas participantes");
		}
		
		if(vidaFinalParejaUno < 0 || vidaFinalParejaDos < 0) {
			throw new IllegalArgumentException("La vida final de las parejas no puede ser negativa");
		}
		
		this.ronda = ronda;
		this.idParejaUno = idParejaUno;
		this.idParejaDos = idParejaDos;
		this.idParejaGanadora = idParejaGanadora;
		this.vidaFinalParejaUno = vidaFinalParejaUno;
		this.vidaFinalParejaDos = vidaFinalParejaDos;
	}
	
	public int getRonda() {
		return ronda;
	}
	
	public int getIdParejaUno() {
		return idParejaUno;
	}
	
	public int getIdParejaDos() {
		return idParejaDos;
	}
	
	public int getIdParejaGanadora() {
		return idParejaGanadora;
	}
	
	public int getVidaFinalParejaUno() {
		return vidaFinalParejaUno;
	}
	
	public int getVidaFinalParejaDos() {
		return vidaFinalParejaDos;
	}
}
