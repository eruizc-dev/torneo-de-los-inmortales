package sigma;

import java.util.List;

import sigma.equipamiento.AnilloEspiritual;
import sigma.equipamiento.Equipamiento;
import sigma.equipamiento.EspadaCelestial;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CreadorCombatientes;
import sigma.escuelas.Escuela;
import sigma.tecnicas.CorteDeLosMilAmaneceres;
import sigma.tecnicas.DominioDeLaNada;
import sigma.tecnicas.MeditacionDelInmortal;
import sigma.torneo.Pareja;

public class TorneoDeLosInmortales {

	  public static void main(String[] args) {

	    // Escuela = configuración: técnicas y equipamiento iniciales.

	    Escuela escuelaDeLaEspada = new Escuela(

	      "Escuela de la Espada Celestial",

	      List.of(new CorteDeLosMilAmaneceres()),

	      List.of(new EspadaCelestial()));

	    Escuela palacioDelVacio = new Escuela("Palacio del Vacío",

	      List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),

	      List.of(new AnilloEspiritual()));


	    // La creación de combatientes está centralizada.

	    CreadorCombatientes creador = new CreadorCombatientes();

	    Combatiente aurelio = creador.crear(

	      "Cultivador de la Espada", "Aurelio", escuelaDeLaEspada);

	    Combatiente nebula = creador.crear(

	      "Hechicero del Vacío", "Nébula", palacioDelVacio);

	    Combatiente kael = creador.crear(

	      "Monje del Cuerpo Celestial", "Kael", palacioDelVacio);

	    Combatiente darian = creador.crear(

	      "Cultivador de la Espada", "Darian", escuelaDeLaEspada);


	    //Torneo torneo = new Torneo();

	    // Registrar al Heraldo y al Cronista como interesados.

	    Pareja primera = new Pareja(aurelio, nebula);

	    //torneo.inscribir(primera);

	    // Crear los demás combatientes e inscribir las otras tres parejas.

	    //torneo.iniciar();

	    // El Heraldo anuncia a los Dos Santos Supremos; después, el main

	    // le pide la tabla al Cronista y el Heraldo la presenta.

	  }

	}

