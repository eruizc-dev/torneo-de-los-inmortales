package sigma.combatientes;

import java.util.List;
import java.util.Map;

import sigma.equipamiento.Equipamiento;
import sigma.tecnicas.Tecnica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
	
    private final int id;
	private final String nombre;
	
	private int vida;
	private int vidaMaxima;
	private int energia;
	private int energiaMaxima;
	
	private final List<Equipamiento> equipamiento;
	private final List<Tecnica> tecnicas;
	private final Map<Tecnica, Integer> usosTecnicas;
	
    public Combatiente(int id, String nombre, int energiaMaxima, List<Equipamiento> equipamiento, int energia, int vida) {
		this.id = id;
		this.nombre = nombre;
		
		this.vida = vida;
		this.vidaMaxima = vida;
		this.energia = energia;
		this.energiaMaxima = energiaMaxima;
		
		this.equipamiento = new ArrayList<>(equipamiento);
		this.tecnicas = new ArrayList<>();
		this.usosTecnicas = new HashMap<>();
		
	}
    
    public int calcularDanio(int danioBase) {
    	if(danioBase < 0) {
    		throw new IllegalArgumentException("El daño no puede ser negativo");
    	}
    	
    	return danioBase;
    }
    
    protected int calcularDanioRecibido(int danio) {
    	if(danio < 0) {
    		throw new IllegalArgumentException("El daño no puede ser negativo");
    	}
    	
    	return danio;
    }
    
    public int calcularCostoEnergia(int costoBase) {
    	if(costoBase < 0) {
    		throw new IllegalArgumentException("El costo base de energia no puede ser negativo");
    	}
    	
    	return costoBase;
    }
    
    protected void recibirAtaque(int danio) {
    	if(danio < 0) {
    		throw new IllegalArgumentException("El daño no puede ser negativo");
    	}
    	
    	int danioFinal = calcularDanioRecibido(danio);
    	
    	vida = Math.max(0, vida - danioFinal);
    }
    
    public void recuperarVida(int curacion) {
    	if(curacion < 0) {
    		throw new IllegalArgumentException("La curacion no puede ser negativa");
    	}
    	
    	vida = Math.min(vidaMaxima, vida + curacion);
    }
    
    public void consumirEnergia(int energiaGastada) {
    	if(energiaGastada < 0) {
    		throw new IllegalArgumentException("El costo de energia no puede ser 0");
    	}
    	
    	if(energiaGastada > energia) {
    		throw new IllegalStateException("Eneriga insuficiente");
    	}
    	
    	energia -= energiaGastada;
    }
    
    public void recuperarEnergia(int energiaRecuperada) {
    	if(energiaRecuperada < 0) {
    		throw new IllegalArgumentException("La recuperacion de energia no puede ser negativa");
    	}
    	
    	energia = Math.min(energiaMaxima, energia + energiaRecuperada);
    }
    
    public void reiniciarCombate() {
    	vida = vidaMaxima;
    	energia = energiaMaxima;
    }
    
    public boolean estaVivo() {
    	return vida > 0;
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
        if (this.id != other.id)
            return false;
        return true;
    }
    
    public int getId() {
    	return id;
    }
    
    public String getNombre() {
    	return nombre;
    }
    
    public int getVida() {
    	return vida;
    }
    
    public int getVidaMaxima() {
    	return vidaMaxima;
    }
    
    public int getEnergia() {
    	return energia;
    }
    
    public int getEnergiaMaxima() {
    	return energiaMaxima;
    }
    
    public boolean tieneEnergiaSuficiente(int costoBase) {
    	return energia >= calcularCostoEnergia(costoBase);
    }
    
    public boolean puedeUsarTecnica(Tecnica tecnica) {
    	if(tecnica == null || !tecnicas.contains(tecnica)) {
    		return false;
    	}
    	
    	return estaVivo()
    			&& tieneUsosDisponibles(tecnica)
    			&& tieneEnergiaSuficiente(tecnica.getCostoEnergia());
    }
    
    public List<Tecnica> getTecnicas() {
    	return new ArrayList<>(tecnicas);
    }
    
    public void agregarTecnica(Tecnica tecnicaNueva) {
    	if(tecnicaNueva == null) {
    		throw new IllegalArgumentException("La tecnica tiene que existir");
    	}
    	
    	if(tecnicas.contains(tecnicaNueva)) {
    		throw new IllegalArgumentException("La tecnica ya existe");
    	}
    	
    	tecnicas.add(tecnicaNueva);
    	usosTecnicas.put(tecnicaNueva,0);
    }
    
    public int getUsosTecnica(Tecnica tecnica) {
    	if(!usosTecnicas.containsKey(tecnica)) {
    		throw new IllegalArgumentException("El combatiene no posee esa tecnica");
    	}
    	
    	return usosTecnicas.get(tecnica);
    }
    
    public boolean tieneUsosDisponibles(Tecnica tecnica) {
    	return getUsosTecnica(tecnica) < 5;
    }
    
    public void registrarUsoTecnica(Tecnica tecnica) {
    	if(!tieneUsosDisponibles(tecnica)) {
    		throw new IllegalStateException("La tecnina no tiene mas usos");
    	}
    	
    	usosTecnicas.put(tecnica, getUsosTecnica(tecnica) + 1);
    }

}
