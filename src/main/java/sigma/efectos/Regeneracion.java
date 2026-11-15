package sigma.efectos;

import sigma.combatientes.Combatiente;

// Recupera vida en cada turno del afectado, sin superar la vida maxima.
public class Regeneracion extends EfectoTemporal {

	public static final String NOMBRE = "Regeneracion";
	private final int curacionPorTurno;

	public Regeneracion(int curacionPorTurno, int duracion) {
		super(NOMBRE, duracion);
		if(curacionPorTurno <= 0) {
			throw new IllegalArgumentException("La curacion debe ser positiva");
		}
		this.curacionPorTurno = curacionPorTurno;
	}

	@Override
	protected void aplicar(Combatiente afectado) {
		afectado.recuperarVida(curacionPorTurno);
	}

	public int getCuracionPorTurno() {
		return curacionPorTurno;
	}
}