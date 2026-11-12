package sigma.equipamiento;

import java.util.List;

import org.junit.jupiter.api.Test;
import sigma.combatientes.Combatiente;
import sigma.combatientes.HechiceroDelVacio;

import static org.junit.jupiter.api.Assertions.*;

public class EquipamientoTest {

    @Test
    public void equiparCuartoObjetoSeRechazaSinCambios() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);

        Equipamiento espada = new EspadaCelestial();
        Equipamiento armadura = new ArmaduraDorada();
        Equipamiento anillo = new AnilloEspiritual();
        Equipamiento sello = new SelloDelFenix();

        nebula.equipar(espada);
        nebula.equipar(armadura);
        nebula.equipar(anillo);

        assertThrows(IllegalStateException.class, () -> nebula.equipar(sello));
        assertEquals(List.of(espada, armadura, anillo), nebula.getEquipamiento());
        assertEquals(100, nebula.getVidaMaxima());
    }

    @Test
    public void equiparCategoriaRepetidaSeRechazaSinCambios() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);

        Equipamiento espada = new EspadaCelestial();
        Equipamiento otraEspada = new EspadaCelestial();

        nebula.equipar(espada);

        assertThrows(IllegalArgumentException.class, () -> nebula.equipar(otraEspada));
        assertEquals(List.of(espada), nebula.getEquipamiento());
        assertEquals(12, nebula.calcularDanio(10));
    }

    @Test
    public void quitarSelloDelFenixRecortaLaVidaActual() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);

        Equipamiento sello = new SelloDelFenix();
        nebula.equipar(sello);

        assertEquals(120, nebula.getVidaMaxima());
        assertEquals(100, nebula.getVida());

        nebula.recuperarVida(20);

        assertEquals(120, nebula.getVida());

        nebula.desequipar(sello);

        assertEquals(100, nebula.getVidaMaxima());
        assertEquals(100, nebula.getVida());
    }
}
