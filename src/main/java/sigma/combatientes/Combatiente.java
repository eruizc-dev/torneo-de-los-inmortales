package sigma.combatientes;

import java.util.List;
import java.util.Map;

import sigma.equipamiento.Equipamiento;
import sigma.tecnicas.Tecnica;

import java.util.ArrayList;
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
	public static final int DANIO_ATAQUE_BASICO = 10;
	
	private int reduccionDefensiva;
	
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
		
		this.equipamiento = new ArrayList<>();
		this.tecnicas = new ArrayList<>();
		this.usosTecnicas = new HashMap<>();
		
		for(Equipamiento equipo : equipamiento) {
			equipar(equipo);
		}
	}
    
    public int calcularDanio(int danioBase) {
    	if(danioBase < 0) {
    		throw new IllegalArgumentException("El daño no puede ser negativo");
    	}
    	
    	int bonoTotal = 0;
    	
    	for(Equipamiento equipo : equipamiento) {
    		bonoTotal += equipo.getBonoDanio();
    	}
    	
    	return (int) (danioBase *(1 + bonoTotal / 100.0));
    }
    
    protected int calcularDanioRecibido(int danio) {
    	if(danio < 0) {
    		throw new IllegalArgumentException("El daño no puede ser negativo");
    	}
    	
    	int reduccionTotal = reduccionDefensiva;
    	
    	for(Equipamiento equipo : equipamiento) {
    		reduccionTotal += equipo.getReduccionDanio();
    	}
    	
    	reduccionTotal = Math.min(100, reduccionTotal);
    	
    	return (int) (danio * (1 - reduccionTotal / 100.0));
    }
    
    public int calcularCostoEnergia(int costoBase) {
    	if(costoBase < 0) {
    		throw new IllegalArgumentException("El costo base de energia no puede ser negativo");
    	}
    	
    	return costoBase;
    }
    
    public void perderEnergia(int cantidad) {
    	if(cantidad < 0) {
    		throw new IllegalArgumentException("No se puede perder energia negativa");
    	}
    	
    	energia = Math.max(0, energia - cantidad);
    }
    
    public void recibirDanio(int danio) {
    	recibirAtaque(danio);
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
    
    public void activarDefensa(int porcentaje) {
    	if(porcentaje <= 0 || porcentaje > 100) {
    		throw new IllegalArgumentException("Porcentaje de defensa invalido");
    	}
    	
    	if(reduccionDefensiva > 0) {
    		throw new IllegalStateException("Ya tiene una defensa activa");
    	}
    	
    	reduccionDefensiva = porcentaje;
    }
    
    public void reiniciarCombate() {
    	vida = vidaMaxima;
    	energia = energiaMaxima;
    	reduccionDefensiva = 0;
    }
    
    public boolean estaVivo() {
    	return vida > 0;
    }

    public void equipar(Equipamiento equipo) {
    	if(equipo == null) {
    		throw new IllegalArgumentException("El equipamiento no puede ser nulo");
    	}
    	
    	if(equipamiento.size() >= 3) {
    		throw new IllegalStateException("No puede equipar mas de 3 objetos");
    	}
    	
    	for(Equipamiento actual : equipamiento) {
    		if(actual.getCategoriaEquipamiento() == equipo.getCategoriaEquipamiento()) {
    			throw new IllegalArgumentException("Ya tiene un equipamiento de esa categoria");
    		}
    	}
    	
    	equipamiento.add(equipo);
    	
    	vidaMaxima += equipo.getBonoVidaMaxima();
    	energiaMaxima += equipo.getBonoEnergiaMaxima();
    }
    
    public void desequipar(Equipamiento equipo) {
    	if(equipo == null) {
    		throw new IllegalArgumentException("El equipamiento no puede ser nulo");
    	}
    	
    	if(!equipamiento.remove(equipo)) {
    		throw new IllegalArgumentException("El combatiente no tiene ese equipamiento");
    	}
    	
    	vidaMaxima -= equipo.getBonoVidaMaxima();
    	energiaMaxima -= equipo.getBonoEnergiaMaxima();
    	
    	vida = Math.min(vida, vidaMaxima);
    	energia = Math.min(energia, energiaMaxima);
    	
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
    
    public boolean tieneDefensaActiva() {
    	return reduccionDefensiva > 0;
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
    
    public List<Equipamiento> getEquipamiento() {
    	return new ArrayList<>(equipamiento);
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
    
    public void usarTecnica(Tecnica tecnica, Combatiente objetivo) {
        if (!puedeUsarTecnica(tecnica)) {
            throw new IllegalStateException(nombre + " no puede usar " + tecnica.getNombre());
        }
        
        int costo = calcularCostoEnergia(tecnica.getCostoEnergia());
        
        tecnica.aplicar(this, objetivo);
        
        consumirEnergia(costo);
        registrarUsoTecnica(tecnica);
    }
    
    public void atacar(Combatiente objetivo) {
        if (!estaVivo()) {
            throw new IllegalStateException(nombre + " está fuera de combate");
        }
        objetivo.recibirDanio(calcularDanio(DANIO_ATAQUE_BASICO));
    }
}
