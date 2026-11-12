package sigma.combatientes;

import java.util.List;

import org.junit.jupiter.api.Test;
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
}
