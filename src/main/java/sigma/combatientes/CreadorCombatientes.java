package sigma.combatientes;

import java.util.List;
import sigma.escuelas.Escuela;
import sigma.equipamiento.Equipamiento;
import sigma.tecnicas.Tecnica;

public class CreadorCombatientes {
	
	private int proximoId = 1;
	
	public Combatiente crear(String tipo, String nombre, Escuela escuela) {
		
		if(nombre == null || nombre.trim().isEmpty() || escuela == null) {
			throw new IllegalArgumentException("Datos de combatiende invalidos");
		}
		
		int id = proximoId;
		int vidaInicial = 100;
		int energiaInicial = 40;
		
		List<Equipamiento> equipo = escuela.getEquipamientoInicial();
		
		Combatiente combatiente;
		
		switch(tipo) {
		case "Cultivador de la Espada":
			combatiente = new CultivadorDeLaEspada(id, nombre, energiaInicial, equipo, energiaInicial, vidaInicial);
			break;
		case "Monje del Cuerpo Celestial":
			combatiente = new MonjeDelCuerpoCelestial(id, nombre, energiaInicial, equipo, energiaInicial, vidaInicial);
			break;
		case "Hechicero del Vacío":
			combatiente = new HechiceroDelVacio(id, nombre, energiaInicial, equipo, energiaInicial, vidaInicial);
			break;
			
			default: throw new IllegalArgumentException("Tipo de combatiente desconocido");
		}
		
		for(Tecnica tecnica : escuela.getTecnicasIniciales()) {
			combatiente.agregarTecnica(tecnica);
		}
		
		proximoId++;
		
		return combatiente;
		
	}
}
