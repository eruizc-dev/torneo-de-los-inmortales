package sigma.eventos;

import java.util.ArrayList;
import java.util.List;

public class PublicadorAcontecimientos {
	
	private final List<Observador> observadores = new ArrayList<>();
	
	public void agregarObservador(Observador observador) {
		if(observador == null) {
			throw new IllegalArgumentException("El observador no puede ser nulo");
		}
		
		if(observadores.contains(observador)) {
			throw new IllegalArgumentException("El observador ya esta registrado");
		}
		
		observadores.add(observador);
	}
	
	public void notificar(Acontecimiento acontecimiento) {
		if(acontecimiento == null) {
			throw new IllegalArgumentException("El acontecimiento no puede ser nulo");
		}
		
		for(Observador observador : observadores) {
			observador.recibirAcontecimiento(acontecimiento);
		}
	}
}
