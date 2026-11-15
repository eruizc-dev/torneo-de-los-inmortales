package sigma.tecnicas;

import sigma.combatientes.Combatiente;
import sigma.efectos.Veneno;


public class ToqueDeLaSerpiente extends Tecnica {

	public static final int DANIO_INMEDIATO = 5;
	public static final int DANIO_VENENO = 6;
	public static final int DURACION_VENENO = 3;

	public ToqueDeLaSerpiente() {
		super("Toque de la Serpiente", 15, TipoTecnica.OFENSIVA);
	}

	@Override
	public void aplicar(Combatiente lanzador, Combatiente objetivo) {
		objetivo.recibirDanio(lanzador.calcularDanio(DANIO_INMEDIATO));
		if(objetivo.estaVivo()) {
			objetivo.aplicarEfecto(new Veneno(lanzador.calcularDanio(DANIO_VENENO), DURACION_VENENO));
		}
	}
}