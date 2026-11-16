package sigma.tecnicas;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CultivadorDeLaEspada;
import sigma.combatientes.HechiceroDelVacio;
import sigma.combatientes.MonjeDelCuerpoCelestial;

import static org.junit.jupiter.api.Assertions.*;

public class TecnicaTest {

    @Test
    public void mismaTecnicaCausaDistintoDañoSegunElEjecutor() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente hechicero = new HechiceroDelVacio(2, "Nebula", 40, Arrays.asList(), 40, 100);
        Combatiente objetivoDelCultivador = new HechiceroDelVacio(3, "Darian", 40, Arrays.asList(), 40, 100);
        Combatiente objetivoDelHechicero = new HechiceroDelVacio(4, "Lyra", 40, Arrays.asList(), 40, 100);

        Tecnica corte = new CorteDeLosMilAmaneceres();
        cultivador.agregarTecnica(corte);
        hechicero.agregarTecnica(corte);

        cultivador.usarTecnica(corte, objetivoDelCultivador);
        hechicero.usarTecnica(corte, objetivoDelHechicero);

        assertEquals(55, objetivoDelCultivador.getVida());
        assertEquals(70, objetivoDelHechicero.getVida());
    }

    @Test
    public void noSeEjecutaTecnicaSinEnergiaSuficiente() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente monje = new MonjeDelCuerpoCelestial(2, "Kael", 40, Arrays.asList(), 40, 100);

        Tecnica corte = new CorteDeLosMilAmaneceres();
        cultivador.agregarTecnica(corte);

        cultivador.perderEnergia(30);

        assertFalse(cultivador.puedeUsarTecnica(corte));
        assertThrows(IllegalStateException.class, () -> cultivador.usarTecnica(corte, monje));
        assertEquals(10, cultivador.getEnergia());
        assertEquals(0, cultivador.getUsosTecnica(corte));
        assertEquals(100, monje.getVida());
    }

    @Test
    public void noSeEjecutaTecnicaSinUsosRestantesAunqueTengaEnergia() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente monje = new MonjeDelCuerpoCelestial(2, "Kael", 40, Arrays.asList(), 40, 1000);

        Tecnica corte = new CorteDeLosMilAmaneceres();
        cultivador.agregarTecnica(corte);

        for (int i = 0; i < 5; i++) {
            cultivador.recuperarEnergia(40);
            cultivador.usarTecnica(corte, monje);
        }

        cultivador.recuperarEnergia(40);

        assertEquals(5, cultivador.getUsosTecnica(corte));
        assertEquals(40, cultivador.getEnergia());
        assertFalse(cultivador.puedeUsarTecnica(corte));
        assertThrows(IllegalStateException.class, () -> cultivador.usarTecnica(corte, monje));
        assertEquals(40, cultivador.getEnergia());
    }

    @Test
    public void usosSeConservanEntreCombatesYVidaYEnergiaSeRecuperan() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente monje = new MonjeDelCuerpoCelestial(2, "Kael", 40, Arrays.asList(), 40, 100);

        Tecnica corte = new CorteDeLosMilAmaneceres();
        cultivador.agregarTecnica(corte);

        cultivador.usarTecnica(corte, monje);
        monje.atacar(cultivador);

        assertEquals(1, cultivador.getUsosTecnica(corte));
        assertEquals(90, cultivador.getVida());
        assertEquals(20, cultivador.getEnergia());

        cultivador.reiniciarCombate();

        assertEquals(1, cultivador.getUsosTecnica(corte));
        assertEquals(100, cultivador.getVida());
        assertEquals(40, cultivador.getEnergia());
    }
    
    @Test
    public void dominioDeLaNadaDaniaYQuitaEnergia() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula", 40, Arrays.asList(), 40, 100);
        Combatiente rival = new HechiceroDelVacio(2, "Lyra", 40, Arrays.asList(), 40, 100);
        Tecnica dominio = new DominioDeLaNada();
        hechicero.agregarTecnica(dominio);

        hechicero.usarTecnica(dominio, rival);

        assertEquals(90, rival.getVida());
        assertEquals(30, rival.getEnergia());
    }

    @Test
    public void muroDelCuerpoDoradoReduceElDanioRecibidoHastaQueTermineElCombate() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula", 40, Arrays.asList(), 40, 100);
        Tecnica muro = new MuroDelCuerpoDorado();
        hechicero.agregarTecnica(muro);

        hechicero.usarTecnica(muro, hechicero);
        assertTrue(hechicero.tieneDefensaActiva());

        hechicero.recibirDanio(40);
        assertEquals(70, hechicero.getVida()); // 40 - 25% = 30

        hechicero.recibirDanio(40);
        assertEquals(40, hechicero.getVida()); // sigue activa en el mismo combate

        hechicero.reiniciarCombate();
        assertFalse(hechicero.tieneDefensaActiva());
        hechicero.recibirDanio(40);
        assertEquals(60, hechicero.getVida()); // en el combate siguiente ya no reduce
    }

    @Test
    public void meditacionDelInmortalRecuperaVidaSinSuperarElMaximo() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula", 40, Arrays.asList(), 40, 100);
        Tecnica meditacion = new MeditacionDelInmortal();
        hechicero.agregarTecnica(meditacion);

        hechicero.recibirDanio(30);
        hechicero.usarTecnica(meditacion, hechicero);

        assertEquals(100, hechicero.getVida());
    }

    @Test
    public void rugidoDeLaBestiaPrimordialEsMasPoderosoQueElCorte() {
        Combatiente lanzador = new HechiceroDelVacio(1, "Nebula", 40, Arrays.asList(), 40, 100);
        Combatiente objetivoCorte = new HechiceroDelVacio(2, "A", 40, Arrays.asList(), 40, 100);
        Combatiente objetivoRugido = new HechiceroDelVacio(3, "B", 40, Arrays.asList(), 40, 100);
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Tecnica rugido = new RugidoDeLaBestiaPrimordial();
        lanzador.agregarTecnica(corte);
        lanzador.agregarTecnica(rugido);

        lanzador.usarTecnica(corte, objetivoCorte);
        lanzador.recuperarEnergia(40);
        lanzador.usarTecnica(rugido, objetivoRugido);

        assertTrue(objetivoRugido.getVida() < objetivoCorte.getVida());
        assertEquals(50, objetivoRugido.getVida());
    }

    @Test
    public void usarTecnicaDescuentaElCostoDeEnergiaYRegistraElUso() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente rival = new HechiceroDelVacio(2, "Lyra", 40, Arrays.asList(), 40, 100);
        Tecnica corte = new CorteDeLosMilAmaneceres();
        cultivador.agregarTecnica(corte);

        cultivador.usarTecnica(corte, rival);

        assertEquals(20, cultivador.getEnergia());
        assertEquals(1, cultivador.getUsosTecnica(corte));
    }

    @Test
    public void losUsosSeRegistranPorCombatienteAunqueCompartanLaTecnica() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente darian = new CultivadorDeLaEspada(2, "Darian", 40, Arrays.asList(), 40, 100);
        Combatiente rival = new HechiceroDelVacio(3, "Lyra", 40, Arrays.asList(), 40, 1000);
        Tecnica corte = new CorteDeLosMilAmaneceres(); // misma instancia para los dos
        aurelio.agregarTecnica(corte);
        darian.agregarTecnica(corte);

        aurelio.usarTecnica(corte, rival);

        assertEquals(1, aurelio.getUsosTecnica(corte));
        assertEquals(0, darian.getUsosTecnica(corte));
    }

    @Test
    public void noSePuedeUsarUnaTecnicaQueElCombatienteNoTiene() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula", 40, Arrays.asList(), 40, 100);
        Combatiente rival = new HechiceroDelVacio(2, "Lyra", 40, Arrays.asList(), 40, 100);
        Tecnica corte = new CorteDeLosMilAmaneceres();

        assertFalse(hechicero.puedeUsarTecnica(corte));
        assertThrows(IllegalStateException.class, () -> hechicero.usarTecnica(corte, rival));
        assertEquals(100, rival.getVida());
    }
    
}
