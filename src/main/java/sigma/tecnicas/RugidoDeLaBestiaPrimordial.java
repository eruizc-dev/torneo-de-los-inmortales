package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public class RugidoDeLaBestiaPrimordial extends Tecnica{

	
	public RugidoDeLaBestiaPrimordial() {
		super("Rugido de la Bestia Primordial", 30, TipoTecnica.OFENSIVA);
	}
	
	@Override
	public void aplicar(Combatiente lanzador, Combatiente objetivo) {
		int danioBase = 50;
		
		int danioFinal = lanzador.calcularDanio(danioBase);
		
		objetivo.recibirDanio(danioFinal);
	}
}
