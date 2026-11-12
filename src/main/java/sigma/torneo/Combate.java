package sigma.torneo;

import sigma.combatientes.Combatiente;
import sigma.tecnicas.Tecnica;
import sigma.tecnicas.TipoTecnica;

public class Combate {
	private final Pareja parejaUno;
	private final Pareja parejaDos;
	private final int limiteAsaltos;
	private boolean ejecutado = false;
	
	public Combate(Pareja parejaUno, Pareja parejaDos, int limiteAsaltos) {
		if(parejaUno == null || parejaDos == null) {
			throw new IllegalArgumentException("Las parejas no pueden ser nulas");
		}
		
		if(parejaUno.equals(parejaDos)) {
			throw new IllegalArgumentException("Una pareja no puede enfrentarse a si misma");
		}
		
		if(limiteAsaltos <= 0) {
			throw new IllegalArgumentException("El limite de asaltos debe ser mayor a cero");
		}
		
		this.parejaUno = parejaUno;
		this.parejaDos = parejaDos;
		this.limiteAsaltos = limiteAsaltos;
	}
	
	private void prepararCombate() {
		parejaUno.getCombatienteUno().reiniciarCombate();
		parejaUno.getCombatienteDos().reiniciarCombate();
		
		parejaUno.reiniciarRegistroTecnicas();
		
		parejaDos.getCombatienteUno().reiniciarCombate();
		parejaDos.getCombatienteDos().reiniciarCombate();
		
		parejaDos.reiniciarRegistroTecnicas();
	}
	
	private Pareja obtenerPrimeraPareja() {
		if(parejaUno.getId() < parejaDos.getId()) {
			return parejaUno;
		}
		
		return parejaDos;
	}
	
	private Pareja obtenerSegundaPareja() {
		if(parejaUno.getId() > parejaDos.getId()) {
			return parejaUno;
		}
		
		return parejaDos;
	}
	
	private Combatiente obtenerPrimerCombatiente(Pareja pareja) {
		Combatiente combatienteUno = pareja.getCombatienteUno();
		Combatiente combatienteDos = pareja.getCombatienteDos();
		
		if(combatienteUno.getId() < combatienteDos.getId()) {
			return combatienteUno;
		}
		
		return combatienteDos;
	}
	
	private Combatiente obtenerSegundoCombatiente(Pareja pareja) {
		Combatiente combatienteUno = pareja.getCombatienteUno();
		Combatiente combatienteDos = pareja.getCombatienteDos();
		
		if(combatienteUno.getId() > combatienteDos.getId()) {
			return combatienteUno;
		}
		
		return combatienteDos;
	}
	
	private Combatiente seleccionarObjetivo(Pareja rival) {
		Combatiente combatienteUno = rival.getCombatienteUno();
		Combatiente combatienteDos = rival.getCombatienteDos();
		
		if(!combatienteUno.estaVivo()) {
			return combatienteDos.estaVivo() ? combatienteDos : null;
		}
		
		if(!combatienteDos.estaVivo()) {
			return combatienteUno;
		}
		
		if(combatienteUno.getVida() < combatienteDos.getVida()) {
			return combatienteUno;
		}
		
		if(combatienteUno.getVida() > combatienteDos.getVida()) {
			return combatienteDos;
		}
		
		return combatienteUno.getId() < combatienteDos.getId() ? combatienteUno : combatienteDos;
	}
	
	private void ataqueBasico(Combatiente atacante, Combatiente objetivo) {
		int danioBase = 10;
		
		int danioFinal = atacante.calcularDanio(danioBase);
		
		objetivo.recibirDanio(danioFinal);
	}
	
	private boolean puedeEjecutarTecnica(Combatiente combatiente, Pareja pareja, Tecnica tecnica) {
		return combatiente.puedeUsarTecnica(tecnica) && pareja.permiteUsarTecnica(combatiente, tecnica);
	}
	
	private void ejecutarTecnica(Combatiente combatiente, Pareja pareja, Tecnica tecnica, Combatiente objetivo) {
		if(!puedeEjecutarTecnica(combatiente, pareja, tecnica)) {
			throw new IllegalArgumentException("No se puede ejecutar la tecnica");
		}
		
		int costo = combatiente.calcularCostoEnergia(tecnica.getCostoEnergia());
		
		tecnica.aplicar(combatiente, objetivo);
		
		combatiente.consumirEnergia(costo);
		combatiente.registrarUsoTecnica(tecnica);
		pareja.registrarUsuarioTecnica(combatiente, tecnica);
	}
	
	private Tecnica buscarTecnicaRecuperacion(Combatiente combatiente, Pareja pareja) {
		if(combatiente.getVida() == combatiente.getVidaMaxima()) {
			return null;
		}
		
		for(Tecnica tecnica : combatiente.getTecnicas()) {
			if(tecnica.getTipoTecnica() == TipoTecnica.RECUPERACION && pareja.permiteUsarTecnica(combatiente, tecnica)) {
				return tecnica;
			}
		}
		
		return null;
	}
	
	private Tecnica buscarTecnicaDefensiva(Combatiente combatiente, Pareja pareja) {
		if(combatiente.tieneDefensaActiva()) {
			return null;
		}
		
		for(Tecnica tecnica : combatiente.getTecnicas()) {
			if(tecnica.getTipoTecnica() == TipoTecnica.DEFENSIVA && pareja.permiteUsarTecnica(combatiente, tecnica)) {
				return tecnica;
			}
		}
		
		return null;
	}
	
	private Tecnica buscarTecnicaOfensiva(Combatiente combatiente, Pareja pareja) {
		Tecnica mejorTecnica = null;
		int mayorUsosRestantes = -1;
		
		for(Tecnica tecnica : combatiente.getTecnicas()) {
			if(tecnica.getTipoTecnica() == TipoTecnica.OFENSIVA && pareja.permiteUsarTecnica(combatiente, tecnica)) {
				int usosRestante = 5 - combatiente.getUsosTecnica(tecnica);
				if(usosRestante > mayorUsosRestantes) {
					mayorUsosRestantes = usosRestante;
					mejorTecnica = tecnica;
				}
			}
		}
		
		return mejorTecnica;
	}
	
	private Tecnica seleccionarTecnica(Combatiente combatiente, Pareja pareja) {
		Tecnica tecnica;
		
		tecnica = buscarTecnicaRecuperacion(combatiente, pareja);
		if(tecnica != null)
			return tecnica;
		
		tecnica = buscarTecnicaDefensiva(combatiente, pareja);
		if(tecnica != null)
			return tecnica;

		tecnica = buscarTecnicaOfensiva(combatiente, pareja);
		if(tecnica != null)
			return tecnica;
		
		return null;
	}
	
	private void realizarAccion(Combatiente combatiente, Pareja pareja, Pareja rival) {
		if(!combatiente.estaVivo() || !rival.estaEnPie()) {
			return;
		}
		
		Tecnica tecnica = seleccionarTecnica(combatiente, pareja);
		
		Combatiente objetivo;
		
		if(tecnica == null) {
			objetivo = seleccionarObjetivo(rival);
			ataqueBasico(combatiente, objetivo);
			return;
		}
		
		if(tecnica.getTipoTecnica() == TipoTecnica.OFENSIVA) {
			objetivo = seleccionarObjetivo(rival);
		}else {
			objetivo = combatiente;
		}
		
		ejecutarTecnica(combatiente, pareja, tecnica, objetivo);
	}
	
	private void realizarTurno(Pareja pareja, Pareja rival) {
		Combatiente primero = obtenerPrimerCombatiente(pareja);
		Combatiente segundo = obtenerSegundoCombatiente(pareja);
		
		if(primero.estaVivo() && rival.estaEnPie()) {
			realizarAccion(primero, pareja, rival);
		}
		
		if(segundo.estaVivo() && rival.estaEnPie()) {
			realizarAccion(segundo, pareja, rival);
		}
		
	}
	
	private void recuperarEnergiaAsalto() {
		int recuperacion = 10;
		
		Combatiente[] combatientes = {
				parejaUno.getCombatienteUno(),
				parejaUno.getCombatienteDos(),
				parejaDos.getCombatienteUno(),
				parejaDos.getCombatienteDos()
		};
		
		for(Combatiente combatiente : combatientes) {
			if(combatiente.estaVivo()) {
				combatiente.recuperarEnergia(recuperacion);
			}
		}
	}
	
	private void realizarAsalto() {
		recuperarEnergiaAsalto();
		
		Pareja primera = obtenerPrimeraPareja();
		Pareja segunda = obtenerSegundaPareja();
		
		realizarTurno(primera, segunda);
		
		if(primera.estaEnPie() && segunda.estaEnPie()) {
			realizarTurno(segunda, primera);
		}
		
	}
	
	private Pareja ejecutar() {
		if(ejecutado) {
			throw new IllegalStateException("El combate ya fue ejecutado");
		}
		
		ejecutado = true;
		
		prepararCombate();
		
		int asalto = 0;
		
		while (asalto < limiteAsaltos && parejaUno.estaEnPie() && parejaDos.estaEnPie()) {
			realizarAsalto();
			
			asalto++;
		}
		
		int vidaUno = parejaUno.calcularVidaTotal();
		int vidaDos = parejaDos.calcularVidaTotal();
		
		if(vidaUno > vidaDos) {
			return parejaUno;
		}
		
		if(vidaUno < vidaDos) {
			return parejaDos;
		}
		
		return obtenerPrimeraPareja();
	}
	
}
