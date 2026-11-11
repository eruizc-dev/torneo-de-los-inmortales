package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public abstract class Tecnica {
	
    private final String nombre;
    private final int costoEnergia;

    protected Tecnica(String nombre, int energia) {
        this.nombre = nombre;
        this.costoEnergia = energia;
    }
    
    public abstract void aplicar(Combatiente lanzador, Combatiente target);
    
    public int getCostoEnergia() {
    	return costoEnergia;
    }
    
    public String getNombre() {
    	return nombre;
    }
}
