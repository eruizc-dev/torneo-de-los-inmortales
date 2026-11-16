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
    
    @Test
    public void equipandoCategoriaRepetidaNoCambiaLosMaximos() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        nebula.equipar(new SelloDelFenix());

        assertThrows(IllegalArgumentException.class, () -> nebula.equipar(new SelloDelFenix()));
        assertEquals(120, nebula.getVidaMaxima());
        assertEquals(1, nebula.getEquipamiento().size());
    }

    @Test
    public void losEfectosDelEquipamientoSeAcumulan() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);

        nebula.equipar(new SelloDelFenix());
        nebula.equipar(new AnilloEspiritual());
        nebula.equipar(new ArmaduraDorada());

        assertEquals(120, nebula.getVidaMaxima());
        assertEquals(50, nebula.getEnergiaMaxima());
        nebula.recibirDanio(50);
        assertEquals(60, nebula.getVida()); // 50 - 20% = 40
    }

    @Test
    public void espadaCelestialAumentaElDanioProvocado() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        Combatiente rival = new HechiceroDelVacio(2, "Lyra", 40, List.of(), 40, 100);
        nebula.equipar(new EspadaCelestial());

        nebula.atacar(rival);

        assertEquals(88, rival.getVida()); // 10 + 20%
    }

    @Test
    public void equiparUnAumentoDeMaximoNoAumentaElValorActual() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);

        nebula.equipar(new SelloDelFenix());
        nebula.equipar(new AnilloEspiritual());

        assertEquals(120, nebula.getVidaMaxima());
        assertEquals(100, nebula.getVida());
        assertEquals(50, nebula.getEnergiaMaxima());
        assertEquals(40, nebula.getEnergia());
    }

    @Test
    public void quitarAnilloEspiritualRecortaLaEnergiaActual() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        Equipamiento anillo = new AnilloEspiritual();
        nebula.equipar(anillo);
        nebula.recuperarEnergia(10);
        assertEquals(50, nebula.getEnergia());

        nebula.desequipar(anillo);

        assertEquals(40, nebula.getEnergiaMaxima());
        assertEquals(40, nebula.getEnergia());
    }

    @Test
    public void quitarUnMaximoNoRecortaSiElActualNoLoSupera() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        Equipamiento sello = new SelloDelFenix();
        nebula.equipar(sello);
        nebula.recibirDanio(50);

        nebula.desequipar(sello);

        assertEquals(50, nebula.getVida());
    }

    @Test
    public void alQuitarUnEquipamientoSeLiberaElLugar() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        Equipamiento espada = new EspadaCelestial();
        nebula.equipar(espada);
        nebula.equipar(new ArmaduraDorada());
        nebula.equipar(new AnilloEspiritual());

        nebula.desequipar(espada);
        Equipamiento sello = new SelloDelFenix();
        nebula.equipar(sello);

        assertEquals(3, nebula.getEquipamiento().size());
        assertTrue(nebula.getEquipamiento().contains(sello));
        assertFalse(nebula.getEquipamiento().contains(espada));
    }

    @Test
    public void quitarUnEquipamientoQueNoLlevaSeRechaza() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);

        assertThrows(IllegalArgumentException.class, () -> nebula.desequipar(new EspadaCelestial()));
        assertThrows(IllegalArgumentException.class, () -> nebula.equipar(null));
    }

    @Test
    public void elCombatienteConoceSuEquipamientoYNoSePuedeModificarDesdeAfuera() {
        Combatiente nebula = new HechiceroDelVacio(1, "Nebula", 40, List.of(), 40, 100);
        Equipamiento espada = new EspadaCelestial();
        nebula.equipar(espada);

        nebula.getEquipamiento().clear();

        assertEquals(List.of(espada), nebula.getEquipamiento());
    }

    @Test
    public void unEquipamientoConValoresInvalidosSeRechaza() {
        assertThrows(IllegalArgumentException.class,
                () -> new Equipamiento("Roto", CategoriaEquipamiento.ARMA, -5, 0, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Equipamiento("Roto", CategoriaEquipamiento.ARMADURA, 0, 150, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Equipamiento("Roto", null, 0, 0, 0, 0));
    }
    
    
}
