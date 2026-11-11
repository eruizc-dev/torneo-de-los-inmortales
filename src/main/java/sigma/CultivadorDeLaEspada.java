package sigma;

import java.util.List;

public class CultivadorDeLaEspada extends Combatiente{
    // Potencia el daño que provoca
		public CultivadorDeLaEspada(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
			super(id, nombre, energiaMaxima, equipamiento, energia, vida);
		}
}
