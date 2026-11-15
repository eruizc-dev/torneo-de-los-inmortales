package sigma.efectos;

import sigma.combatientes.Combatiente;

public abstract class EfectoTemporal {

	private final String nombre;
	private int turnosRestantes;

	protected EfectoTemporal(String nombre, int duracion) {
		if(nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El efecto debe tener nombre");
		}
		if(duracion <= 0) {
			throw new IllegalArgumentException("La duracion debe ser mayor a cero");
		}
		this.nombre = nombre;
		this.turnosRestantes = duracion;
	}

	public final void aplicarTurno(Combatiente afectado) {
		if(terminado()) {
			throw new IllegalStateException("El efecto ya termino");
		}
		aplicar(afectado);
		turnosRestantes--;
	}

	protected abstract void aplicar(Combatiente afectado);

	public boolean terminado() {
		return turnosRestantes == 0;
	}

	public String getNombre() {
		return nombre;
	}

	public int getTurnosRestantes() {
		return turnosRestantes;
	}
}
