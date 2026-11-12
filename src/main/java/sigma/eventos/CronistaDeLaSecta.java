package sigma.eventos;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CronistaDeLaSecta implements Observador{
	
	private final List<Acontecimiento> historial = new ArrayList<>();

	@Override
	public void recibirAcontecimiento(Acontecimiento acontecimiento) {
		if(acontecimiento == null) {
			throw new IllegalArgumentException("El acontecimineto no puede ser nulo");
		}
		
		historial.add(acontecimiento);
		
	}
	
	private Map<Integer, PosicionPareja> obtenerUltimasPosiciones(){
		Map<Integer, PosicionPareja> posiciones = new HashMap<>();
		
		for(Acontecimiento acontecimiento : historial) {
			if(acontecimiento.getTipoAcontecimiento() == TipoAcontecimiento.RESULTADO_COMBATE) {
				ResultadoCombate resultado = acontecimiento.getResultadoCombate();
				
				PosicionPareja posicionParejaUno = new PosicionPareja(resultado.getIdParejaUno(), resultado.getRonda(), resultado.getVidaFinalParejaUno());
				PosicionPareja posicionParejaDos = new PosicionPareja(resultado.getIdParejaDos(), resultado.getRonda(), resultado.getVidaFinalParejaDos());
				
				posiciones.put(resultado.getIdParejaUno(), posicionParejaUno);
				posiciones.put(resultado.getIdParejaDos(), posicionParejaDos);
			}
		}
		
		return posiciones;
		
	}
	
	public List<PosicionPareja> getTablaPosiciones(){
		Map<Integer, PosicionPareja> posiciones = obtenerUltimasPosiciones();
		
		List<PosicionPareja> tabla = new ArrayList<>(posiciones.values());
		
		for(int i = 0; i < tabla.size() - 1; i++) {
			for(int j = i + 1; j< tabla.size(); j++) {
				PosicionPareja actual = tabla.get(i);
				PosicionPareja siguiente = tabla.get(j);
				
				boolean intercambiar = false;
				
				if(actual.getUltimaRonda() < siguiente.getUltimaRonda()) {
					intercambiar = true;
				}else if(actual.getUltimaRonda() == siguiente.getUltimaRonda()) {
					if(actual.getVidaFinal() < siguiente.getVidaFinal()) {
						intercambiar = true;
					}else if(actual.getVidaFinal() == siguiente.getVidaFinal() && actual.getIdPareja() > siguiente.getIdPareja()) {
						intercambiar = true;
					}
				}
				
				if(intercambiar) {
					tabla.set(i, siguiente);
					tabla.set(j, actual);
				}				
			}
		}
		
		return tabla;
	}
	
	public List<Acontecimiento> getHistorial(){
		return new ArrayList<>(historial);
	}

}
