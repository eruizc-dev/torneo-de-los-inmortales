package sigma;

import java.util.List;
import java.util.Arrays;

import sigma.equipamiento.AnilloEspiritual;
import sigma.equipamiento.ArmaduraDorada;
import sigma.equipamiento.EspadaCelestial;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CreadorCombatientes;
import sigma.escuelas.Escuela;
import sigma.torneo.Torneo;
import sigma.torneo.Pareja;
import sigma.eventos.HeraldoImperial;
import sigma.eventos.CronistaDeLaSecta;
import sigma.tecnicas.*;


public class TorneoDeLosInmortales {

	  public static void main(String[] args) {

	    // Escuela = configuración: técnicas y equipamiento iniciales.

	    Escuela escuelaDeLaEspada = new Escuela(

	      "Escuela de la Espada Celestial",

	      Arrays.asList(new CorteDeLosMilAmaneceres()),

	      Arrays.asList(new EspadaCelestial()));

	    Escuela palacioDelVacio = new Escuela("Palacio del Vacío",

	      Arrays.asList(new DominioDeLaNada(), new MeditacionDelInmortal(), new ToqueDeLaSerpiente()),

	      Arrays.asList(new AnilloEspiritual()));
	    
	    Escuela monasterioDelCuerpoDorado = new Escuela("Monasterio del Cuerpo Dorado",
	    		Arrays.asList(new MuroDelCuerpoDorado(), new LaSanacionDelCielo()),
	    		Arrays.asList(new ArmaduraDorada()));
	    
	    

	    // La creación de combatientes está centralizada.

	    CreadorCombatientes creador = new CreadorCombatientes();

	    Combatiente aurelio = creador.crear("Cultivador de la Espada", "Aurelio", escuelaDeLaEspada);

	    Combatiente nebula = creador.crear("Hechicero del Vacío", "Nébula", palacioDelVacio);

	    Combatiente kael = creador.crear("Monje del Cuerpo Celestial", "Kael", monasterioDelCuerpoDorado);

	    Combatiente darian = creador.crear("Cultivador de la Espada", "Darian", escuelaDeLaEspada);
	    
	    Combatiente mei = creador.crear("Cultivador de la Espada", "Mei", escuelaDeLaEspada);

	    Combatiente shen = creador.crear("Monje del Cuerpo Celestial", "Shen", monasterioDelCuerpoDorado);

	    Combatiente akira = creador.crear("Hechicero del Vacío", "Akira", palacioDelVacio);

	    Combatiente ren = creador.crear("Monje del Cuerpo Celestial", "Ren", escuelaDeLaEspada);


	    Torneo torneo = new Torneo();
	    
	    // Registrar al Heraldo y al Cronista como interesados.
	    HeraldoImperial heraldo = new HeraldoImperial();
	    CronistaDeLaSecta cronista = new CronistaDeLaSecta();
	    
	    torneo.agregarObservador(heraldo);
	    torneo.agregarObservador(cronista);

	    // Crear los demás combatientes e inscribir las otras tres parejas.
	    Pareja primera = new Pareja(aurelio, nebula);
	    Pareja segunda = new Pareja(kael, darian);
	    Pareja tercera = new Pareja(mei, shen);
	    Pareja cuarta = new Pareja(akira, ren);

	    torneo.inscribir(primera);
	    torneo.inscribir(segunda);
	    torneo.inscribir(tercera);
	    torneo.inscribir(cuarta);

	    torneo.iniciar();
	    
	    heraldo.mostrarTablaDePosiciones(cronista.getTablaPosiciones());

	    // El Heraldo anuncia a los Dos Santos Supremos; después, el main

	    // le pide la tabla al Cronista y el Heraldo la presenta.

	  }

	}

