package sigma.combatientes;

import java.util.List;

import sigma.equipamiento.Equipamiento;

public class MonjeDelCuerpoCelestial extends Combatiente{
	public MonjeDelCuerpoCelestial(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
		super(id, nombre, energiaMaxima, equipamiento, energia, vida);
	}
	
	// Reduce el daño que recibe
	@Override
	protected int calcularDanioRecibido(int danio) {
		return (int) (super.calcularDanioRecibido(danio) * 0.6);
	}
}
