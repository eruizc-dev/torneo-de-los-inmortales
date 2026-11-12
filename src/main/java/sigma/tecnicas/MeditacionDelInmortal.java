package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public class MeditacionDelInmortal extends Tecnica{

	
	public MeditacionDelInmortal() {
		super("Meditacion del Inmortal", 20, TipoTecnica.RECUPERACION);
	}
	
	@Override
	public void aplicar(Combatiente lanzador, Combatiente objetivo) {
		lanzador.recuperarVida(50);
	}
}
