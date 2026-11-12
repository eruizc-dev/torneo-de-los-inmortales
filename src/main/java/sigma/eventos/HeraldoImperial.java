package sigma.eventos;

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
}
