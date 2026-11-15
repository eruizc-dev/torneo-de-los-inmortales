package sigma.efectos;

import sigma.combatientes.Combatiente;

// Quita vida en cada turno del afectado. Es daño interno: ignora armaduras y defensas.
public class Veneno extends EfectoTemporal {

	public static final String NOMBRE = "Veneno";
	private final int danioPorTurno;

	public Veneno(int danioPorTurno, int duracion) {
		super(NOMBRE, duracion);
		if(danioPorTurno <= 0) {
			throw new IllegalArgumentException("El daño del veneno debe ser positivo");
		}
		this.danioPorTurno = danioPorTurno;
	}

	@Override
	protected void aplicar(Combatiente afectado) {
		afectado.perderVida(danioPorTurno);
	}

	public int getDanioPorTurno() {
		return danioPorTurno;
	}
}
