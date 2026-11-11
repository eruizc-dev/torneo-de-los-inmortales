package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public class CorteDeLosMilAmaneceres extends Tecnica{
	
	public CorteDeLosMilAmaneceres() {
		super("Corte de los Mil Amaneceres", 20);
	}
	
	@Override
	public void aplicar(Combatiente lanzador, Combatiente objetivo) {
		int danioBase = 30;
		int danioFinal = lanzador.calcularDanio(danioBase);
		
		objetivo.recibirDanio(danioFinal);
	}

}
