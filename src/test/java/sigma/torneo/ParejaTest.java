package sigma.torneo;

import java.util.List;

import org.junit.jupiter.api.Test;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CultivadorDeLaEspada;
import sigma.combatientes.HechiceroDelVacio;
import sigma.tecnicas.CorteDeLosMilAmaneceres;
import sigma.tecnicas.Tecnica;

import static org.junit.jupiter.api.Assertions.*;

public class ParejaTest {

    @Test
    public void compañeroNoPuedeUsarTecnicaMientrasElPrimeroConserveUsos() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);

        Tecnica corte = new CorteDeLosMilAmaneceres();
        aurelio.agregarTecnica(corte);
        nebula.agregarTecnica(corte);

        Pareja pareja = new Pareja(aurelio, nebula);

        pareja.registrarUsuarioTecnica(aurelio, corte);

        assertFalse(pareja.permiteUsarTecnica(nebula, corte));
        assertThrows(IllegalStateException.class, () -> pareja.registrarUsuarioTecnica(nebula, corte));
        assertTrue(pareja.permiteUsarTecnica(aurelio, corte));
    }
    
    @Test
    public void compañeroPuedeUsarLaTecnicaCuandoElPrimeroAgotaSusUsos() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);
        Combatiente rival = new HechiceroDelVacio(3, "Lyra", 40, List.of(), 40, 10000);
        Tecnica corte = new CorteDeLosMilAmaneceres();
        aurelio.agregarTecnica(corte);
        nebula.agregarTecnica(corte);
        Pareja pareja = new Pareja(aurelio, nebula);

        for (int i = 0; i < 5; i++) {
            aurelio.recuperarEnergia(40);
            aurelio.usarTecnica(corte, rival);
            pareja.registrarUsuarioTecnica(aurelio, corte);
        }

        assertTrue(pareja.permiteUsarTecnica(nebula, corte));
    }

    @Test
    public void compañeroPuedeUsarLaTecnicaCuandoElPrimeroQuedaFueraDeCombate() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);
        Tecnica corte = new CorteDeLosMilAmaneceres();
        aurelio.agregarTecnica(corte);
        nebula.agregarTecnica(corte);
        Pareja pareja = new Pareja(aurelio, nebula);
        pareja.registrarUsuarioTecnica(aurelio, corte);

        aurelio.recibirDanio(1000);

        assertTrue(pareja.permiteUsarTecnica(nebula, corte));
    }

    @Test
    public void laRestriccionDelCompañeroTerminaConElCombate() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);
        Tecnica corte = new CorteDeLosMilAmaneceres();
        aurelio.agregarTecnica(corte);
        nebula.agregarTecnica(corte);
        Pareja pareja = new Pareja(aurelio, nebula);
        pareja.registrarUsuarioTecnica(aurelio, corte);

        pareja.reiniciarRegistroTecnicas();

        assertTrue(pareja.permiteUsarTecnica(nebula, corte));
    }

    @Test
    public void unaParejaNoPuedeTenerDosVecesAlMismoCombatiente() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente mismoId = new CultivadorDeLaEspada(1, "Clon", 40, List.of(), 40, 100);

        assertThrows(IllegalArgumentException.class, () -> new Pareja(aurelio, aurelio));
        assertThrows(IllegalArgumentException.class, () -> new Pareja(aurelio, mismoId));
        assertThrows(IllegalArgumentException.class, () -> new Pareja(aurelio, null));
    }

    @Test
    public void cadaParejaRecibeUnIdUnicoAlCrearse() {
        Pareja primera = new Pareja(new CultivadorDeLaEspada(1, "A", 40, List.of(), 40, 100),
                new HechiceroDelVacio(2, "B", 40, List.of(), 40, 100));
        Pareja segunda = new Pareja(new CultivadorDeLaEspada(3, "C", 40, List.of(), 40, 100),
                new HechiceroDelVacio(4, "D", 40, List.of(), 40, 100));

        assertNotEquals(primera.getId(), segunda.getId());
        assertTrue(primera.getId() < segunda.getId());
        assertNotEquals(primera, segunda);
    }

    @Test
    public void unaParejaPierdeCuandoSusDosIntegrantesQuedanFueraDeCombate() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);
        Pareja pareja = new Pareja(aurelio, nebula);

        aurelio.recibirDanio(1000);
        assertTrue(pareja.estaEnPie());

        nebula.recibirDanio(1000);
        assertFalse(pareja.estaEnPie());
    }

    @Test
    public void laVidaTotalSumaLaVidaActualYUnCaidoSumaCero() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);
        Pareja pareja = new Pareja(aurelio, nebula);

        aurelio.recibirDanio(1000);
        nebula.recibirDanio(30);

        assertEquals(70, pareja.calcularVidaTotal());
    }
}
