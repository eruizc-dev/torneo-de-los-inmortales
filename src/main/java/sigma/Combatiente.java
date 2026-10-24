package sigma;

import java.util.List;

/*
 * Combatiente (abstracta)
 * id único e inmutable, nombre, vida y energía (actuales y máximas), equipamiento y técnicas disponibles.
 * Dos combatientes son el mismo si tienen el mismo id, sin importar el nombre.
 * Tipos iniciales:
 *     Cultivador de la Espada: potencia el daño que provoca.
 *     Monje del Cuerpo Celestial: reduce el daño que recibe.
 *     Hechicero del Vacío: gasta menos energía al ejecutar técnicas.
 * La diferencia entre tipos debe surgir del polimorfismo, no de preguntar el tipo con if o instanceof: una misma
 * técnica provoca distinto daño según quién la ejecuta, sin que la técnica conozca el tipo del combatiente. Se deben
 * poder agregar tipos nuevos sin modificar el torneo ni las técnicas existentes.
 */
public abstract class Combatiente {
    public final int id;
	public final String nombre;
    public final int energiaMaxima;
    public final List<Equipamiento> equipamiento;
    public int energia;
    public int vida;

    public Combatiente(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
		this.id = id;
		this.nombre = nombre;
		this.energiaMaxima = energiaMaxima;
		this.equipamiento = equipamiento;
		this.energia = energia;
		this.vida = vida;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Combatiente other = (Combatiente) obj;
        if (id != other.id)
            return false;
        return true;
    }

    // TODO mover a otro archivo

    // Potencia el daño que provoca
    public class CultivadorDeLaEspada extends Combatiente {
		public CultivadorDeLaEspada(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
			super(id, nombre, energiaMaxima, equipamiento, energia, vida);
		}
    }

    // Reduce el daño que recibe
    public class MonjeDelCuerpoCelestial extends Combatiente {
		public MonjeDelCuerpoCelestial(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
			super(id, nombre, energiaMaxima, equipamiento, energia, vida);
		}
    }

    // Gasta menos energia al ejecutar tecnicas
    public class HechiceroDelVacio extends Combatiente {
		public HechiceroDelVacio(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
			super(id, nombre, energiaMaxima, equipamiento, energia, vida);
		}
    }
}
