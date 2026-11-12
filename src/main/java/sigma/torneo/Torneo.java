package sigma.torneo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sigma.combatientes.Combatiente;
import sigma.eventos.Observador;
import sigma.eventos.PublicadorAcontecimientos;
import sigma.eventos.Acontecimiento;
import sigma.eventos.TipoAcontecimiento;

public class Torneo {
	
	public boolean iniciado;
	
	private final PublicadorAcontecimientos publicador = new PublicadorAcontecimientos();
	private final List<Pareja> parejasInscriptas = new ArrayList<>();
	private final Set<Combatiente> combatientesInscriptos = new HashSet<>();
	
	public void inscribir(Pareja pareja) {
		if(iniciado) {
			throw new IllegalStateException("No se pueden inscribir parejas una vez iniciado el torneo");
		}
		
		if(pareja == null) {
			throw new IllegalArgumentException("La pareja no puede ser nula");
		}
		
		if(parejasInscriptas.size() >= 8) {
			throw new IllegalStateException("El torneo admite como maximo 8 parejas");
		}
		
		if(parejasInscriptas.contains(pareja)) {
			throw new IllegalArgumentException("La pareja ya esta inscripta");
		}
		
		Combatiente combatienteUno = pareja.getCombatienteUno();
		Combatiente combatienteDos = pareja.getCombatienteDos();
		
		if(combatientesInscriptos.contains(combatienteUno) || combatientesInscriptos.contains(combatienteDos)) {
			throw new IllegalArgumentException("Uno de los combatientes ya esta inscripto en el torneo");
		}
		
		parejasInscriptas.add(pareja);
		combatientesInscriptos.add(combatienteUno);
		combatientesInscriptos.add(combatienteDos);
	}
	
	private void validarCantidadDeParejas() {
		int cantidad = parejasInscriptas.size();
		
		if(cantidad != 4 && cantidad != 8) {
			throw new IllegalStateException("El torneo debe tener 4 u 8 parejas");
		}
	}
	
	public Pareja iniciar() {
		if(iniciado) {
			throw new IllegalStateException("El torneo ya fue inciado");
		}
		
		validarCantidadDeParejas();
		iniciado = true;
		
		List<Pareja> rondaActual = new ArrayList<>(parejasInscriptas);
		
		int numeroRonda = 1;
		
		while(rondaActual.size() > 1) {
			List<Pareja> ganadoras = new ArrayList<>();
			
			for(int i = 0; i < rondaActual.size(); i += 2) {
				Combate combate = new Combate(rondaActual.get(i), rondaActual.get(i+1), 20, publicador, numeroRonda);
				
				Pareja ganadadora = combate.ejecutar();
				ganadoras.add(ganadadora);
			}
			rondaActual = ganadoras;
			
			numeroRonda++;
		}
		
		Pareja campeona = rondaActual.get(0);
		
		notificarProclamacion(campeona, numeroRonda - 1);
		
		return campeona;
	}
	
	public void agregarObservaodr(Observador observador) {
		publicador.agregarObservador(observador);
	}
	
	public void notificarProclamacion(Pareja campeona, int rondaFinal) {
		Acontecimiento proclamacion = new Acontecimiento(TipoAcontecimiento.PROCLAMACION, rondaFinal, "¡La pareja " + campeona.getId() + " se proclama campeona del Torneo de los Inmortales!");
		
		publicador.notificar(proclamacion);
	}
}
