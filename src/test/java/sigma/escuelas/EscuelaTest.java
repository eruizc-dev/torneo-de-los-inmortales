package sigma.escuelas;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CreadorCombatientes;
import sigma.equipamiento.AnilloEspiritual;
import sigma.equipamiento.ArmaduraDorada;
import sigma.equipamiento.Equipamiento;
import sigma.equipamiento.EspadaCelestial;
import sigma.tecnicas.CorteDeLosMilAmaneceres;
import sigma.tecnicas.DominioDeLaNada;
import sigma.tecnicas.MeditacionDelInmortal;
import sigma.tecnicas.MuroDelCuerpoDorado;
import sigma.tecnicas.Tecnica;

class EscuelaTest {

	@Test
	public void laEscuelaPreparaAlCombatienteConSusTecnicasYEquipamientoIniciales() {
		Tecnica dominio = new DominioDeLaNada();
		Tecnica meditacion = new MeditacionDelInmortal();
		Equipamiento anillo = new AnilloEspiritual();
		Escuela palacio = new Escuela("Palacio del Vacío", List.of(dominio, meditacion), List.of(anillo));
		CreadorCombatientes creador = new CreadorCombatientes();

		Combatiente nebula = creador.crear("Hechicero del Vacío", "Nebula", palacio);

		assertEquals(List.of(dominio, meditacion), nebula.getTecnicas());
		assertEquals(List.of(anillo), nebula.getEquipamiento());
		assertEquals(50, nebula.getEnergiaMaxima()); // 40 + anillo
		assertEquals(40, nebula.getEnergia()); // equipar no sube el actual
	}

	@Test
	public void elMismoTipoPreparadoPorDistintasEscuelasTieneDistintaPreparacion() {
		Escuela espada = new Escuela("Escuela de la Espada Celestial", List.of(new CorteDeLosMilAmaneceres()),
				List.of(new EspadaCelestial()));
		Escuela monasterio = new Escuela("Monasterio del Cuerpo Dorado",
				List.of(new MuroDelCuerpoDorado(), new MeditacionDelInmortal()), List.of(new ArmaduraDorada()));
		CreadorCombatientes creador = new CreadorCombatientes();

		Combatiente aurelio = creador.crear("Cultivador de la Espada", "Aurelio", espada);
		Combatiente darian = creador.crear("Cultivador de la Espada", "Darian", monasterio);

		assertEquals(1, aurelio.getTecnicas().size());
		assertEquals(2, darian.getTecnicas().size());
		assertEquals(18, aurelio.calcularDanio(10)); // 10 +20% espada = 12, x1.5 cultivador
		assertEquals(15, darian.calcularDanio(10));
	}

	@Test
	public void unaEscuelaPuedePrepararCombatientesDeCualquierTipo() {
		Escuela palacio = new Escuela("Palacio del Vacío", List.of(new DominioDeLaNada()), List.of());
		CreadorCombatientes creador = new CreadorCombatientes();

		Combatiente cultivador = creador.crear("Cultivador de la Espada", "Aurelio", palacio);
		Combatiente monje = creador.crear("Monje del Cuerpo Celestial", "Kael", palacio);
		Combatiente hechicero = creador.crear("Hechicero del Vacío", "Nebula", palacio);

		assertEquals(1, cultivador.getTecnicas().size());
		assertEquals(1, monje.getTecnicas().size());
		assertEquals(1, hechicero.getTecnicas().size());
	}

	@Test
	public void agregarUnaEscuelaNuevaEsSoloCrearOtroObjeto() {
		Escuela nueva = new Escuela("Secta del Trueno", List.of(new CorteDeLosMilAmaneceres()),
				List.of(new ArmaduraDorada()));
		CreadorCombatientes creador = new CreadorCombatientes();

		Combatiente kael = creador.crear("Monje del Cuerpo Celestial", "Kael", nueva);

		assertEquals("Secta del Trueno", nueva.getNombre());
		assertEquals(1, kael.getTecnicas().size());
		assertEquals(1, kael.getEquipamiento().size());
	}

	@Test
	public void laEscuelaNoSeModificaSiCambianLasListasOriginales() {
		List<Tecnica> tecnicas = new ArrayList<>(List.of(new CorteDeLosMilAmaneceres()));
		Escuela escuela = new Escuela("Escuela", tecnicas, List.of());

		tecnicas.add(new DominioDeLaNada());

		assertEquals(1, escuela.getTecnicasIniciales().size());
	}

	@Test
	public void unaEscuelaConDatosInvalidosSeRechaza() {
		assertThrows(IllegalArgumentException.class, () -> new Escuela("", List.of(), List.of()));
		assertThrows(IllegalArgumentException.class, () -> new Escuela("Escuela", null, List.of()));
		assertThrows(IllegalArgumentException.class, () -> new Escuela("Escuela", List.of(), null));
	}

}
