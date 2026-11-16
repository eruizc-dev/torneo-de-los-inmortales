package sigma.combatientes;

import java.util.List;

import org.junit.jupiter.api.Test;

import sigma.equipamiento.ArmaduraDorada;
import sigma.escuelas.Escuela;
import sigma.tecnicas.CorteDeLosMilAmaneceres;
import sigma.tecnicas.Tecnica;

import static org.junit.jupiter.api.Assertions.*;

public class CombatienteTest {

    @Test
    public void combatienteCaidoNoActua() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        Combatiente aurelio = new CultivadorDeLaEspada(2, "Aurelio", 40, List.of(), 40, 100);

        Tecnica corte = new CorteDeLosMilAmaneceres();
        nebula.agregarTecnica(corte);

        nebula.recibirDanio(500);

        assertFalse(nebula.estaVivo());
        assertEquals(0, nebula.getVida());

        assertThrows(IllegalStateException.class, () -> nebula.atacar(aurelio));
        assertThrows(IllegalStateException.class, () -> nebula.usarTecnica(corte, aurelio));

        assertEquals(100, aurelio.getVida());
    }
    
    @Test
    public void dosCombatientesConElMismoIdSonElMismoAunqueTenganDistintoNombre() {
        Combatiente uno = new HechiceroDelVacio(7, "Nebula", 40, List.of(), 40, 100);
        Combatiente otro = new HechiceroDelVacio(7, "Otro nombre", 40, List.of(), 40, 100);
        Combatiente distinto = new HechiceroDelVacio(8, "Nebula", 40, List.of(), 40, 100);

        assertEquals(uno, otro);
        assertEquals(uno.hashCode(), otro.hashCode());
        assertNotEquals(uno, distinto);
    }


    @Test
    public void cultivadorDeLaEspadaPotenciaElDanioDelAtaqueBasico() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente hechicero = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);
        Combatiente objetivo1 = new HechiceroDelVacio(3, "A", 40, List.of(), 40, 100);
        Combatiente objetivo2 = new HechiceroDelVacio(4, "B", 40, List.of(), 40, 100);

        cultivador.atacar(objetivo1);
        hechicero.atacar(objetivo2);

        assertEquals(85, objetivo1.getVida());
        assertEquals(90, objetivo2.getVida());
    }

    @Test
    public void monjeDelCuerpoCelestialRecibeMenosDanio() {
        Combatiente monje = new MonjeDelCuerpoCelestial(1, "Kael", 40, List.of(), 40, 100);
        Combatiente hechicero = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);

        monje.recibirDanio(50);
        hechicero.recibirDanio(50);

        assertEquals(70, monje.getVida());
        assertEquals(50, hechicero.getVida());
    }

    @Test
    public void hechiceroDelVacioGastaMenosEnergiaAlUsarTecnicas() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        Combatiente cultivador = new CultivadorDeLaEspada(2, "Aurelio", 40, List.of(), 40, 100);
        Combatiente rival = new HechiceroDelVacio(3, "Lyra", 40, List.of(), 40, 1000);
        Tecnica corte = new CorteDeLosMilAmaneceres();
        hechicero.agregarTecnica(corte);
        cultivador.agregarTecnica(corte);

        hechicero.usarTecnica(corte, rival);
        cultivador.usarTecnica(corte, rival);

        assertEquals(24, hechicero.getEnergia());
        assertEquals(20, cultivador.getEnergia());
    }

    @Test
    public void losEfectosDeTipoYEquipamientoSeCombinan() {
        Combatiente monje = new MonjeDelCuerpoCelestial(1, "Kael", 40, List.of(new ArmaduraDorada()), 40, 100);

        monje.recibirDanio(100); // 100 -20% armadura = 80, x0.6 del monje = 48

        assertEquals(52, monje.getVida());
    }


    @Test
    public void alLlegarACeroDeVidaQuedaFueraDeCombateSinVidaNegativa() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);

        hechicero.recibirDanio(100);

        assertEquals(0, hechicero.getVida());
        assertFalse(hechicero.estaVivo());
    }

    @Test
    public void recuperarEnergiaNoSuperaElMaximo() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);

        hechicero.perderEnergia(5);
        hechicero.recuperarEnergia(10);

        assertEquals(40, hechicero.getEnergia());
    }

    @Test
    public void reiniciarCombateDevuelveVidaYEnergiaAlMaximoInclusoAUnCaido() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        hechicero.recibirDanio(500);
        hechicero.perderEnergia(40);

        hechicero.reiniciarCombate();

        assertTrue(hechicero.estaVivo());
        assertEquals(100, hechicero.getVida());
        assertEquals(40, hechicero.getEnergia());
    }


    @Test
    public void elCreadorAsignaIdsEnOrdenDeCreacion() {
        CreadorCombatientes creador = new CreadorCombatientes();
        Escuela escuela = new Escuela("Escuela", List.of(), List.of());

        Combatiente primero = creador.crear("Cultivador de la Espada", "Aurelio", escuela);
        Combatiente segundo = creador.crear("Hechicero del Vacío", "Nebula", escuela);
        Combatiente tercero = creador.crear("Monje del Cuerpo Celestial", "Kael", escuela);

        assertEquals(1, primero.getId());
        assertEquals(2, segundo.getId());
        assertEquals(3, tercero.getId());
    }

    @Test
    public void elCreadorDevuelveElTipoPedidoConValoresIniciales() {
        CreadorCombatientes creador = new CreadorCombatientes();
        Escuela escuela = new Escuela("Escuela", List.of(), List.of());

        Combatiente kael = creador.crear("Monje del Cuerpo Celestial", "Kael", escuela);

        assertTrue(kael instanceof MonjeDelCuerpoCelestial);
        assertEquals("Kael", kael.getNombre());
        assertEquals(100, kael.getVida());
        assertEquals(100, kael.getVidaMaxima());
        assertEquals(40, kael.getEnergia());
        assertEquals(40, kael.getEnergiaMaxima());
    }

    @Test
    public void elCreadorRechazaTiposDesconocidosSinConsumirId() {
        CreadorCombatientes creador = new CreadorCombatientes();
        Escuela escuela = new Escuela("Escuela", List.of(), List.of());

        assertThrows(IllegalArgumentException.class, () -> creador.crear("Pirata", "Jack", escuela));
        Combatiente siguiente = creador.crear("Hechicero del Vacío", "Nebula", escuela);

        assertEquals(1, siguiente.getId());
    }

    @Test
    public void elCreadorRechazaNombreVacioOEscuelaNula() {
        CreadorCombatientes creador = new CreadorCombatientes();
        Escuela escuela = new Escuela("Escuela", List.of(), List.of());

        assertThrows(IllegalArgumentException.class, () -> creador.crear("Hechicero del Vacío", "  ", escuela));
        assertThrows(IllegalArgumentException.class, () -> creador.crear("Hechicero del Vacío", "Nebula", null));
    }
    
}
