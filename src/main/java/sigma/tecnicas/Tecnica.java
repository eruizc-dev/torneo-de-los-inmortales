package sigma.tecnicas;

import sigma.combatientes.Combatiente;

public abstract class Tecnica {
	
    private final String nombre;
    private final int costoEnergia;
    private final TipoTecnica tipoTecnica;

    protected Tecnica(String nombre, int energia, TipoTecnica tipoTecnica) {
        this.nombre = nombre;
        this.costoEnergia = energia;
        this.tipoTecnica = tipoTecnica;
    }
    
    public abstract void aplicar(Combatiente lanzador, Combatiente target);
    
    public int getCostoEnergia() {
    	return costoEnergia;
    }
    
    public String getNombre() {
    	return nombre;
    }
    
    public TipoTecnica getTipoTecnica() {
    	return tipoTecnica;
    }
}
