package sigma.efectos;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CultivadorDeLaEspada;
import sigma.combatientes.HechiceroDelVacio;
import sigma.combatientes.MonjeDelCuerpoCelestial;
import sigma.equipamiento.ArmaduraDorada;
import sigma.tecnicas.LaSanacionDelCielo;
import sigma.tecnicas.MuroDelCuerpoDorado;
import sigma.tecnicas.Tecnica;
import sigma.tecnicas.ToqueDeLaSerpiente;

import static org.junit.jupiter.api.Assertions.*;

public class EfectoTemporalTest {

    private static Combatiente hechicero(int id, String nombre, int vida) {
        return new HechiceroDelVacio(id, nombre, 40, Arrays.asList(), 40, vida);
    }

    @Test
    public void elVenenoQuitaVidaEnCadaTurnoYTerminaAlAgotarSuDuracion() {
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.aplicarEfecto(new Veneno(6, 3));

        nebula.procesarEfectosDelTurno();
        assertEquals(94, nebula.getVida());
        nebula.procesarEfectosDelTurno();
        assertEquals(88, nebula.getVida());
        assertTrue(nebula.tieneEfecto(Veneno.NOMBRE));

        nebula.procesarEfectosDelTurno();
        assertEquals(82, nebula.getVida());
        assertFalse(nebula.tieneEfecto(Veneno.NOMBRE));

        nebula.procesarEfectosDelTurno();
        assertEquals(82, nebula.getVida());
    }

    @Test
    public void laDuracionSeDescuentaPorTurnoDelAfectado() {
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.aplicarEfecto(new Veneno(6, 3));

        nebula.procesarEfectosDelTurno();

        assertEquals(2, nebula.getEfectosActivos().get(0).getTurnosRestantes());
    }

    @Test
    public void elVenenoIgnoraArmaduraDefensaYResistenciaDelMonje() {
        Combatiente monje = new MonjeDelCuerpoCelestial(1, "Kael", 40, Arrays.asList(new ArmaduraDorada()), 40, 100);
        Tecnica muro = new MuroDelCuerpoDorado();
        monje.agregarTecnica(muro);
        monje.usarTecnica(muro, monje);
        monje.aplicarEfecto(new Veneno(10, 2));

        monje.procesarEfectosDelTurno();

        assertEquals(90, monje.getVida());
    }

    @Test
    public void laRegeneracionCuraPorTurnoSinSuperarElMaximo() {
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.recibirDanio(15);
        nebula.aplicarEfecto(new Regeneracion(10, 3));

        nebula.procesarEfectosDelTurno();
        assertEquals(95, nebula.getVida());
        nebula.procesarEfectosDelTurno();
        assertEquals(100, nebula.getVida());
    }

    @Test
    public void aplicarElMismoEfectoRenuevaLaDuracionSinAcumular() {
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.aplicarEfecto(new Veneno(6, 3));
        nebula.procesarEfectosDelTurno();

        nebula.aplicarEfecto(new Veneno(6, 3));

        assertEquals(1, nebula.getEfectosActivos().size());
        assertEquals(3, nebula.getEfectosActivos().get(0).getTurnosRestantes());
        nebula.procesarEfectosDelTurno();
        assertEquals(88, nebula.getVida());
    }

    @Test
    public void efectosDistintosConvivenYSeAplicanEnOrdenDeAplicacion() {
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.recibirDanio(20);
        nebula.aplicarEfecto(new Regeneracion(10, 2));
        nebula.aplicarEfecto(new Veneno(6, 2));

        List<EfectoTemporal> aplicados = nebula.procesarEfectosDelTurno();

        assertEquals(2, aplicados.size());
        assertEquals(Regeneracion.NOMBRE, aplicados.get(0).getNombre());
        assertEquals(Veneno.NOMBRE, aplicados.get(1).getNombre());
        assertEquals(84, nebula.getVida());
    }

    @Test
    public void elVenenoPuedeDejarFueraDeCombateYCortaLosEfectosSiguientes() {
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.recibirDanio(95);
        nebula.aplicarEfecto(new Veneno(6, 3));
        nebula.aplicarEfecto(new Regeneracion(10, 3));

        List<EfectoTemporal> aplicados = nebula.procesarEfectosDelTurno();

        assertFalse(nebula.estaVivo());
        assertEquals(1, aplicados.size());
        assertEquals(0, nebula.getVida());
    }

    @Test
    public void noSePuedeAplicarUnEfectoAUnCaido() {
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.recibirDanio(1000);

        assertThrows(IllegalStateException.class, () -> nebula.aplicarEfecto(new Veneno(6, 3)));
    }

    @Test
    public void losEfectosTerminanAlComenzarOtroCombate() {
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.aplicarEfecto(new Veneno(6, 3));

        nebula.reiniciarCombate();

        assertTrue(nebula.getEfectosActivos().isEmpty());
        nebula.procesarEfectosDelTurno();
        assertEquals(100, nebula.getVida());
    }

    @Test
    public void unEfectoConValoresInvalidosSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> new Veneno(6, 0));
        assertThrows(IllegalArgumentException.class, () -> new Veneno(0, 3));
        assertThrows(IllegalArgumentException.class, () -> new Regeneracion(10, -1));
    }

    @Test
    public void toqueDeLaSerpienteDaniaYEnvenenaSegunElTipoDelLanzador() {
        Tecnica toque = new ToqueDeLaSerpiente();
        Combatiente hechicero = hechicero(1, "Nebula", 100);
        Combatiente cultivador = new CultivadorDeLaEspada(2, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente objetivoHechicero = hechicero(3, "A", 100);
        Combatiente objetivoCultivador = hechicero(4, "B", 100);
        hechicero.agregarTecnica(toque);
        cultivador.agregarTecnica(toque);

        hechicero.usarTecnica(toque, objetivoHechicero);
        cultivador.usarTecnica(toque, objetivoCultivador);

        assertEquals(95, objetivoHechicero.getVida());
        assertEquals(93, objetivoCultivador.getVida());
        Veneno venenoHechicero = (Veneno) objetivoHechicero.getEfectosActivos().get(0);
        Veneno venenoCultivador = (Veneno) objetivoCultivador.getEfectosActivos().get(0);
        assertEquals(6, venenoHechicero.getDanioPorTurno());
        assertEquals(9, venenoCultivador.getDanioPorTurno());
        assertEquals(28, hechicero.getEnergia());
    }

    @Test
    public void laSanacionDelCieloAplicaRegeneracionAlEjecutor() {
        Tecnica loto = new LaSanacionDelCielo();
        Combatiente nebula = hechicero(1, "Nebula", 100);
        nebula.agregarTecnica(loto);
        nebula.recibirDanio(40);

        nebula.usarTecnica(loto, nebula);

        assertEquals(60, nebula.getVida());
        assertTrue(nebula.tieneEfecto(Regeneracion.NOMBRE));
        nebula.procesarEfectosDelTurno();
        assertEquals(70, nebula.getVida());
    }
}
