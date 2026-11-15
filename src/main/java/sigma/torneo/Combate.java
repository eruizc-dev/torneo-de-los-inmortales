package sigma.torneo;

import sigma.combatientes.Combatiente;
import sigma.efectos.EfectoTemporal;
import sigma.tecnicas.Tecnica;
import sigma.tecnicas.TipoTecnica;
import sigma.eventos.PublicadorAcontecimientos;
import sigma.eventos.Acontecimiento;
import sigma.eventos.TipoAcontecimiento;
import sigma.eventos.ResultadoCombate;

public class Combate {
	private final Pareja parejaUno;
	private final Pareja parejaDos;
	private final int limiteAsaltos;
	private boolean ejecutado = false;
	private final int ronda;
	
	private final PublicadorAcontecimientos publicador;
	
	public Combate(Pareja parejaUno, Pareja parejaDos, int limiteAsaltos, PublicadorAcontecimientos publicador, int ronda) {
		if(parejaUno == null || parejaDos == null) {
			throw new IllegalArgumentException("Las parejas no pueden ser nulas");
		}
		
		if(parejaUno.equals(parejaDos)) {
			throw new IllegalArgumentException("Una pareja no puede enfrentarse a si misma");
		}
		
		if(limiteAsaltos <= 0) {
			throw new IllegalArgumentException("El limite de asaltos debe ser mayor a cero");
		}
		
		if(publicador == null) {
			throw new IllegalArgumentException("El publicador no puede ser nulo");
		}
		
		if(ronda <= 0) {
			throw new IllegalArgumentException("La ronda debe ser positiva");
		}
		
		this.parejaUno = parejaUno;
		this.parejaDos = parejaDos;
		this.limiteAsaltos = limiteAsaltos;
		this.publicador = publicador;
		this.ronda = ronda;
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
		atacante.atacar(objetivo);
	}
	
	private boolean puedeEjecutarTecnica(Combatiente combatiente, Pareja pareja, Tecnica tecnica) {
		return combatiente.puedeUsarTecnica(tecnica) && pareja.permiteUsarTecnica(combatiente, tecnica);
	}
	
	private void ejecutarTecnica(Combatiente combatiente, Pareja pareja, Tecnica tecnica, Combatiente objetivo) {
		if(!puedeEjecutarTecnica(combatiente, pareja, tecnica)) {
			throw new IllegalArgumentException("No se puede ejecutar la tecnica");
		}
		
		combatiente.usarTecnica(tecnica, objetivo);
		
		pareja.registrarUsuarioTecnica(combatiente, tecnica);
	}
	
	private Tecnica buscarTecnicaRecuperacion(Combatiente combatiente, Pareja pareja) {
		if(combatiente.getVida() == combatiente.getVidaMaxima()) {
			return null;
		}
		
		for(Tecnica tecnica : combatiente.getTecnicas()) {
			if(tecnica.getTipoTecnica() == TipoTecnica.RECUPERACION && puedeEjecutarTecnica(combatiente, pareja, tecnica)) {
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
			if(tecnica.getTipoTecnica() == TipoTecnica.DEFENSIVA && puedeEjecutarTecnica(combatiente, pareja, tecnica)) {
				return tecnica;
			}
		}
		
		return null;
	}
	
	private Tecnica buscarTecnicaOfensiva(Combatiente combatiente, Pareja pareja) {
		Tecnica mejorTecnica = null;
		int mayorUsosRestantes = -1;
		
		for(Tecnica tecnica : combatiente.getTecnicas()) {
			if(tecnica.getTipoTecnica() == TipoTecnica.OFENSIVA && puedeEjecutarTecnica(combatiente, pareja, tecnica)) {
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
			notificarAccion(combatiente.getNombre() + " realizo un ataque basico contra " + objetivo.getNombre());
			notificarEliminacion(objetivo);
			return;
		}
		
		if(tecnica.getTipoTecnica() == TipoTecnica.OFENSIVA) {
			objetivo = seleccionarObjetivo(rival);
		}else {
			objetivo = combatiente;
		}
		
		ejecutarTecnica(combatiente, pareja, tecnica, objetivo);
		notificarAccion(combatiente.getNombre() + " utilizo la tecnica " + tecnica.getNombre() + " contra " + objetivo.getNombre());
		if(tecnica.getTipoTecnica() == TipoTecnica.OFENSIVA) {
			notificarEliminacion(objetivo);
		}
	}
	
	private void realizarTurno(Pareja pareja, Pareja rival) {
		Combatiente primero = obtenerPrimerCombatiente(pareja);
		Combatiente segundo = obtenerSegundoCombatiente(pareja);
		
		realizarTurnoDeCombatiente(primero,pareja,rival);
		realizarTurnoDeCombatiente(segundo,pareja,rival);
	}
	
	private void realizarTurnoDeCombatiente(Combatiente combatiente, Pareja pareja, Pareja rival) {
		if(!combatiente.estaVivo() || !rival.estaEnPie()) {
			return;
		}
		
		for(EfectoTemporal efecto: combatiente.procesarEfectosDelTurno()) {
			notificarEfecto(combatiente,efecto);
		}
		
		notificarEliminacion(combatiente);
		realizarAccion(combatiente, pareja, rival);
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
	
	public Pareja ejecutar() {
		if(ejecutado) {
			throw new IllegalStateException("El combate ya fue ejecutado");
		}
			
		ejecutado = true;
		
		Pareja ganadora;
		
		prepararCombate();
		
		Acontecimiento inicio = new Acontecimiento(TipoAcontecimiento.INICIO_COMBATE, ronda, "Comienza el combate de la ronda " + ronda);
		
		publicador.notificar(inicio);
		
		int asalto = 0;
		
		while (asalto < limiteAsaltos && parejaUno.estaEnPie() && parejaDos.estaEnPie()) {
			realizarAsalto();
			
			asalto++;
		}
		
		int vidaUno = parejaUno.calcularVidaTotal();
		int vidaDos = parejaDos.calcularVidaTotal();
		
		if(vidaUno > vidaDos) {
			ganadora = parejaUno;
		} else if(vidaUno < vidaDos) {
			ganadora =  parejaDos;
		} else {
			ganadora = obtenerPrimeraPareja();
		}
		
		notificarResultado(parejaUno, parejaDos, ganadora);
		
		return ganadora;
	}
	
	public void notificarAccion(String descripcion) {
		Acontecimiento accion = new Acontecimiento(TipoAcontecimiento.ACCION, ronda, descripcion);
		
		publicador.notificar(accion);
	}
	
	public void notificarEliminacion(Combatiente combatiente) {
		if(!combatiente.estaVivo()) {
			Acontecimiento eliminacion = new Acontecimiento(TipoAcontecimiento.ELIMINACION, ronda, "El combatiente " + combatiente.getNombre() + " fue eliminado");
			
			publicador.notificar(eliminacion);
		}
	}
	
	public void notificarResultado(Pareja parejaUno, Pareja parejaDos, Pareja parejaGanadora) {
		ResultadoCombate resultado = new ResultadoCombate(ronda, parejaUno.getId(), parejaDos.getId(), parejaGanadora.getId(), parejaUno.calcularVidaTotal(), parejaDos.calcularVidaTotal());
		
		Acontecimiento acontecimiento = new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE, ronda, "Finalizo el combate. Gano la pareja " + parejaGanadora.getId(), resultado);
		
		publicador.notificar(acontecimiento);
	}
	
	public void notificarEfecto(Combatiente combatiente, EfectoTemporal efecto) {
		String descripcion = combatiente.getNombre() + " sufre efecto " + efecto.getNombre() + "(turnos restantes " + efecto.getTurnosRestantes() + ")";
		publicador.notificar(new Acontecimiento(TipoAcontecimiento.EFECTO, ronda, descripcion));
		
	}
	
}
