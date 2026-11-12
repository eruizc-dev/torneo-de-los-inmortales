package sigma.torneo;

import java.util.HashMap;
import java.util.Map;
import sigma.combatientes.Combatiente;
import sigma.tecnicas.Tecnica;

public class Pareja {
	private static int proximoId = 1;
    private final int id;
	private final Combatiente combatienteUno;
    private final Combatiente combatienteDos;
    private final Map<String, Combatiente> usuarioTecnica;

	public Pareja(Combatiente combatiente1, Combatiente combatiente2) {
		
		if(combatiente1 == null || combatiente2 == null) {
			throw new IllegalArgumentException("Ningun combatinete puede ser nulo");
		}
		
		if(combatiente1.equals(combatiente2)) {
			throw new IllegalArgumentException("Una pareja debe tener dos combatientes distintos");
		}
		
		this.id = proximoId++;
		this.combatienteUno = combatiente1;
		this.combatienteDos = combatiente2;
		this.usuarioTecnica = new HashMap<>();
	}
	
	public void registrarUsuarioTecnica(Combatiente combatiente, Tecnica tecnica) {
		if(!permiteUsarTecnica(combatiente, tecnica)) {
			throw new IllegalStateException("El compañero tiene bloqueada esta tecnica");
		}
		
		usuarioTecnica.put(tecnica.getNombre(),combatiente);
	}
	
	public boolean permiteUsarTecnica(Combatiente combatiente, Tecnica tecnica) {
		if(combatiente == null || tecnica == null) {
			throw new IllegalArgumentException("Datos invalidos");
		}
		
		if(!combatienteUno.equals(combatiente) && !combatienteDos.equals(combatiente)) {
			throw new IllegalArgumentException("El combatiente no pertenece a esta pareja");
		}
		
		Combatiente usuario = usuarioTecnica.get(tecnica.getNombre());
		
		if(usuario == null || usuario.equals(combatiente)) {
			return true;
		}
		
		if(!usuario.estaVivo()) {
			return true;
		}
		
		for(Tecnica t : usuario.getTecnicas()) {
			if(t.getNombre().equals(tecnica.getNombre())) {
				return !usuario.tieneUsosDisponibles(t);
			}
		}
		
		return false;
	}
	
	public void reiniciarRegistroTecnicas() {
		usuarioTecnica.clear();
	}
	
	public boolean estaEnPie() {
		return combatienteUno.estaVivo() || combatienteDos.estaVivo();
	}
	
	public int calcularVidaTotal() {
		return combatienteUno.getVida() + combatienteDos.getVida();
	}
	
	public int getId() {
		return id;
	}
	
	public Combatiente getCombatienteUno() {
		return combatienteUno;
	}
	
	public Combatiente getCombatienteDos() {
		return combatienteDos;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pareja other = (Pareja) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
