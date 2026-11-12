package sigma.eventos;

import java.util.List;

public class HeraldoImperial implements Observador{

	
	@Override
	public void recibirAcontecimiento(Acontecimiento acontecimiento) {
		System.out.println(acontecimiento.getDescripcion());
		
		if(acontecimiento.getTipoAcontecimiento() == TipoAcontecimiento.RESULTADO_COMBATE) {
			ResultadoCombate resultado = acontecimiento.getResultadoCombate();
			
			System.out.println("Ronda: " + resultado.getRonda());
			System.out.println("Pareja: " + resultado.getIdParejaUno() + " - Vida Final: " + resultado.getVidaFinalParejaUno());
			System.out.println("Pareja: " + resultado.getIdParejaDos() + " - Vida Final: " + resultado.getVidaFinalParejaDos());
			System.out.println("Ganadora: " + resultado.getIdParejaGanadora());
		}
	}
	
	public void mostrarTablaDePosiciones(List<PosicionPareja> tabla) {
		System.out.println("===== TABLA DE POSICIONES =====");
		
		for(int i = 0; i < tabla.size(); i++) {
			PosicionPareja posicion = tabla.get(i);
			
			System.out.println((i + 1) + ". Pareja " + posicion.getIdPareja() + " | Ronda: " + posicion.getUltimaRonda() + " | Vida final: " + posicion.getVidaFinal());
		}
	}
}
