package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public class DominioDeLaNada extends Tecnica{
	
	public DominioDeLaNada() {
		super("Dominio de la Nada", 10);
	}
	
	@Override
	public void aplicar(Combatiente lanzador, Combatiente objetivo) {
		int danioBase = 10;
		int energiaReducida = 10;
		
		int danioFinal = lanzador.calcularDanio(danioBase);
		
		objetivo.recibirDanio(danioFinal);
		objetivo.perderEnergia(energiaReducida);
	}

}
