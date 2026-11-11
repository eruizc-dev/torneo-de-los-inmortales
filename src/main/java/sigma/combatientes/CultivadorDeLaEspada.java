package sigma.combatientes;

import java.util.List;

import sigma.equipamiento.Equipamiento;

public class CultivadorDeLaEspada extends Combatiente{
	public CultivadorDeLaEspada(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
		super(id, nombre, energiaMaxima, equipamiento, energia, vida);
	}
		
    // Potencia el daño que provoca
	@Override
	public int calcularDanio(int danioBase) {
		return (int) (super.calcularDanio(danioBase) * 1.5);
	}
}
