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

    public void equipar(Equipamiento equipo) {
        /* TODO implementar, verificando que cumpla con las condiciones del enunciado
         *  - Un combatiente puede llevar hasta tres equipamientos, sin dos de la misma
         *    categoría (arma, armadura, anillo, sello). Los efectos se acumulan.
         *  - Se puede equipar y quitar antes del torneo y entre combates. Se debe manejar
         *    correctamente cuando un cambio intenta romper una regla.
         *  - Equipar un objeto que aumenta un máximo no aumenta el valor actual. Al
         *    quitarlo, el valor actual se recorta al nuevo máximo si lo supera.
         *  - El combatiente conoce los objetos que lleva y puede consultarlos.
        */
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

}
