package sigma;

import java.util.List;

public class MonjeDelCuerpoCelestial extends Combatiente{
    // Reduce el daño que recibe
		public MonjeDelCuerpoCelestial(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
			super(id, nombre, energiaMaxima, equipamiento, energia, vida);
		}
}
