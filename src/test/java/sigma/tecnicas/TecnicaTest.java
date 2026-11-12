package sigma.tecnicas;

import java.util.List;

import org.junit.jupiter.api.Test;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CultivadorDeLaEspada;
import sigma.combatientes.HechiceroDelVacio;
import sigma.combatientes.MonjeDelCuerpoCelestial;

import static org.junit.jupiter.api.Assertions.*;

public class TecnicaTest {

    @Test
    public void mismaTecnicaCausaDistintoDañoSegunElEjecutor() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente hechicero = new HechiceroDelVacio(2, "Nebula", 40, List.of(), 40, 100);
        Combatiente objetivoDelCultivador = new HechiceroDelVacio(3, "Darian", 40, List.of(), 40, 100);
        Combatiente objetivoDelHechicero = new HechiceroDelVacio(4, "Lyra", 40, List.of(), 40, 100);

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
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente monje = new MonjeDelCuerpoCelestial(2, "Kael", 40, List.of(), 40, 100);

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
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente monje = new MonjeDelCuerpoCelestial(2, "Kael", 40, List.of(), 40, 1000);

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
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio", 40, List.of(), 40, 100);
        Combatiente monje = new MonjeDelCuerpoCelestial(2, "Kael", 40, List.of(), 40, 100);

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
}
