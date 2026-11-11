package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public class DominioDeLaNada extends Tecnica{
	
	public DominioDeLaNada() {
		super("Dominio de la Nada", 10);
	}
	
	@Override
	public void aplicar(Combatiente lanzador, Combatiente objetivo) {
		throw new UnsupportedOperationException("Pendiente de ejecutar");
	}

}
