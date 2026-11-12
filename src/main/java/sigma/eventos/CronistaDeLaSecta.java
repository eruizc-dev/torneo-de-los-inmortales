package sigma.eventos;

import java.util.ArrayList;
import java.util.List;

public class CronistaDeLaSecta implements Observador{
	
	private final List<Acontecimiento> historial = new ArrayList<>();

	@Override
	public void recibirAcontecimiento(Acontecimiento acontecimiento) {
		if(acontecimiento == null) {
			throw new IllegalArgumentException("El acontecimineto no puede ser nulo");
		}
		
		historial.add(acontecimiento);
		
	}
	
	public List<Acontecimiento> getHistorial(){
		return new ArrayList<>(historial);
	}

}
