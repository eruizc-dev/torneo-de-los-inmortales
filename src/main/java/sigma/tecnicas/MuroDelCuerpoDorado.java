package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public class MuroDelCuerpoDorado extends Tecnica{
	
	public MuroDelCuerpoDorado() {
		super("Muro del Cuerpo Dorado", 10, TipoTecnica.DEFENSIVA);
	}
	
	@Override
	public void aplicar(Combatiente lanzador, Combatiente objetivo) {
		lanzador.activarDefensa(25);
	}
}
