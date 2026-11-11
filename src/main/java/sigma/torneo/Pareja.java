package sigma.torneo;

import sigma.combatientes.Combatiente;

public class Pareja {
    private final int id;
	private final Combatiente combatiente1;
    private final Combatiente combatiente2;

	public Pareja(int id, Combatiente combatiente1, Combatiente combatiente2) {
		this.id = id;
		this.combatiente1 = combatiente1;
		this.combatiente2 = combatiente2;
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
		Pareja other = (Pareja) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
