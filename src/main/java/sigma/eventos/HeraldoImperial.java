package sigma.eventos;

import java.util.List;

public class HeraldoImperial implements Observador{

	
	@Override
	public void recibirAcontecimiento(Acontecimiento acontecimiento) {
		if(acontecimiento.getTipoAcontecimiento() == TipoAcontecimiento.INICIO_COMBATE) {
			System.out.println();
			System.out.println("===============================");
			System.out.println("		RONDA " + acontecimiento.getRonda());
			System.out.println("===============================");
		}
		
		if (acontecimiento.getTipoAcontecimiento() == TipoAcontecimiento.ACCION) {

		    System.out.println("   > " + acontecimiento.getDescripcion());

		} else if (acontecimiento.getTipoAcontecimiento() == TipoAcontecimiento.ELIMINACION) {

		    System.out.println();
		    System.out.println("   [ELIMINACION] " + acontecimiento.getDescripcion());
		    System.out.println();

		} else if (acontecimiento.getTipoAcontecimiento() == TipoAcontecimiento.PROCLAMACION) {

		    System.out.println();
		    System.out.println("==========================================");
		    System.out.println("          CAMPEONES DEL TORNEO");
		    System.out.println("==========================================");
		    System.out.println(acontecimiento.getDescripcion());
		    System.out.println("==========================================");
		    System.out.println();

		} else {

		    System.out.println(acontecimiento.getDescripcion());

		}
		
		if(acontecimiento.getTipoAcontecimiento() == TipoAcontecimiento.RESULTADO_COMBATE) {
			ResultadoCombate resultado = acontecimiento.getResultadoCombate();
			
			System.out.println();
			System.out.println("------------------------------------------");
			System.out.println("          RESULTADO DEL COMBATE");
			System.out.println("------------------------------------------");

			System.out.println("Ronda disputada: " + resultado.getRonda());

			System.out.println(
			    "Pareja " + resultado.getIdParejaUno()
			    + " | Vida restante: " + resultado.getVidaFinalParejaUno()
			);

			System.out.println(
			    "Pareja " + resultado.getIdParejaDos()
			    + " | Vida restante: " + resultado.getVidaFinalParejaDos()
			);

			System.out.println();
			System.out.println(">>> GANADORA: PAREJA " + resultado.getIdParejaGanadora() + " <<<");
			System.out.println("------------------------------------------");
			System.out.println();
		}
	}
	
	public void mostrarTablaDePosiciones(List<PosicionPareja> tabla) {

	    System.out.println();
	    System.out.println("================================================");
	    System.out.println("              TABLA DE POSICIONES");
	    System.out.println("================================================");

	    System.out.println("POS.\tPAREJA\tRONDA\tVIDA FINAL");
	    System.out.println("------------------------------------------------");

	    for (int i = 0; i < tabla.size(); i++) {

	        PosicionPareja posicion = tabla.get(i);

	        System.out.println(
	            (i + 1) + "\t"
	            + posicion.getIdPareja() + "\t"
	            + posicion.getUltimaRonda() + "\t"
	            + posicion.getVidaFinal()
	        );
	    }

	    System.out.println("================================================");
	}
}
