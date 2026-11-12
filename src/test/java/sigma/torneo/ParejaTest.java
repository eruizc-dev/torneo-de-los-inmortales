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
}
