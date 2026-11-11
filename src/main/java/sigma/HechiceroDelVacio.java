package sigma;

import java.util.List;

public class HechiceroDelVacio extends Combatiente{
	public HechiceroDelVacio(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
		super(id, nombre, energiaMaxima, equipamiento, energia, vida);
	}
	
    // Gasta menos energia al ejecutar tecnicas
	@Override
	public int calcularCostoEnergia(int costoBase) {
		return (int) (super.calcularCostoEnergia(costoBase) * 0.8);
	}
}
