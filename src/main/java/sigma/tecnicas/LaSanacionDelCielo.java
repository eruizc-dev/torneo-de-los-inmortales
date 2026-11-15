package sigma.tecnicas;

import sigma.combatientes.Combatiente;
import sigma.efectos.Regeneracion;

public class LaSanacionDelCielo extends Tecnica {

	public static final int CURACION_POR_TURNO = 10;
	public static final int DURACION = 3;

	public LaSanacionDelCielo() {
		super("La sanacion del cielo", 15, TipoTecnica.RECUPERACION);
	}

	@Override
	public void aplicar(Combatiente lanzador, Combatiente objetivo) {
		lanzador.aplicarEfecto(new Regeneracion(CURACION_POR_TURNO, DURACION));
	}
}
