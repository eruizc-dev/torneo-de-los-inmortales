package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public abstract class Tecnica {

	public enum Tipo { OFENSIVA, DEFENSIVA, RECUPERACION }

	private final String nombre;
	private final int costoEnergia;
	private final Tipo tipo;

	protected Tecnica(String nombre, int costoEnergia, Tipo tipo) {
		if(costoEnergia < 0) {
			throw new IllegalArgumentException("El costo de energia no puede ser negativo");
		}

		if(tipo == null) {
			throw new IllegalArgumentException("El tipo de tecnica no puede ser nulo");
		}

		this.nombre = nombre;
		this.costoEnergia = costoEnergia;
		this.tipo = tipo;
	}

	public abstract void aplicar(Combatiente lanzador, Combatiente objetivo);

	public String getNombre() {
		return nombre;
	}

	public int getCostoEnergia() {
		return costoEnergia;
	}

	public Tipo getTipo() {
		return tipo;
	}

	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}

		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}

		return nombre.equals(((Tecnica) obj).nombre);
	}

	@Override
	public int hashCode() {
		return nombre.hashCode();
	}
}
