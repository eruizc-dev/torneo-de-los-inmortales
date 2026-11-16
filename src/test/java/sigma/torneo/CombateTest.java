package sigma.torneo;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CultivadorDeLaEspada;
import sigma.combatientes.HechiceroDelVacio;
import sigma.eventos.Acontecimiento;
import sigma.eventos.CronistaDeLaSecta;
import sigma.eventos.PublicadorAcontecimientos;
import sigma.eventos.ResultadoCombate;
import sigma.eventos.TipoAcontecimiento;
import sigma.tecnicas.CorteDeLosMilAmaneceres;
import sigma.tecnicas.MeditacionDelInmortal;
import sigma.tecnicas.MuroDelCuerpoDorado;
import sigma.tecnicas.RugidoDeLaBestiaPrimordial;
import sigma.tecnicas.Tecnica;

import static org.junit.jupiter.api.Assertions.*;

public class CombateTest {

    private PublicadorAcontecimientos publicador;
    private CronistaDeLaSecta cronista;

    @BeforeEach
    public void setUp() {
        publicador = new PublicadorAcontecimientos();
        cronista = new CronistaDeLaSecta();
        publicador.agregarObservador(cronista);
    }

    private static Combatiente hechicero(int id, String nombre, int vida) {
        return new HechiceroDelVacio(id, nombre, 40, Arrays.asList(), 40, vida);
    }

    private List<String> acciones() {
        List<String> acciones = new ArrayList<>();
        for (Acontecimiento a : cronista.getHistorial()) {
            if (a.getTipoAcontecimiento() == TipoAcontecimiento.ACCION) {
                acciones.add(a.getDescripcion());
            }
        }
        return acciones;
    }

    private long contar(TipoAcontecimiento tipo) {
        return cronista.getHistorial().stream().filter(a -> a.getTipoAcontecimiento() == tipo).count();
    }

    private ResultadoCombate ultimoResultado() {
        List<Acontecimiento> historial = cronista.getHistorial();
        for (int i = historial.size() - 1; i >= 0; i--) {
            if (historial.get(i).getTipoAcontecimiento() == TipoAcontecimiento.RESULTADO_COMBATE) {
                return historial.get(i).getResultadoCombate();
            }
        }
        return null;
    }


    @Test
    public void elObjetivoEsElRivalConMenosVidaYEnEmpateElDeMenorId() {
        Combatiente a1 = hechicero(1, "A1", 100);
        Combatiente a2 = hechicero(2, "A2", 100);
        Combatiente b1 = hechicero(3, "B1", 100);
        Combatiente b2 = hechicero(4, "B2", 60);
        Pareja a = new Pareja(a1, a2);
        Pareja b = new Pareja(b1, b2);

        new Combate(a, b, 1, publicador, 1).ejecutar();

        assertEquals(Arrays.asList(
                "A1 realizo un ataque basico contra B2",   // B2 tiene menos vida
                "A2 realizo un ataque basico contra B2",
                "B1 realizo un ataque basico contra A1",   // empate 100-100: menor id
                "B2 realizo un ataque basico contra A1"),  // A1 ya tiene menos vida
                acciones());
        assertEquals(40, b2.getVida());
        assertEquals(100, b1.getVida());
        assertEquals(80, a1.getVida());
        assertEquals(100, a2.getVida());
    }

    @Test
    public void empiezaLaParejaDeMenorIdYDentroDeEllaElCombatienteDeMenorId() {
        Combatiente a1 = hechicero(1, "A1", 100);
        Combatiente a2 = hechicero(2, "A2", 100);
        Combatiente b1 = hechicero(3, "B1", 100);
        Combatiente b2 = hechicero(4, "B2", 100);
        Pareja a = new Pareja(a2, a1); // integrantes en orden invertido
        Pareja b = new Pareja(b2, b1);

        new Combate(b, a, 1, publicador, 1).ejecutar(); // parejas en orden invertido

        List<String> acciones = acciones();
        assertTrue(acciones.get(0).startsWith("A1 "));
        assertTrue(acciones.get(1).startsWith("A2 "));
        assertTrue(acciones.get(2).startsWith("B1 "));
        assertTrue(acciones.get(3).startsWith("B2 "));
    }

    // ---------- Combatientes caídos ----------

    @Test
    public void unCaidoNoActuaNiEsElegidoComoObjetivo() {
        Combatiente a1 = hechicero(1, "A1", 100);
        Combatiente a2 = hechicero(2, "A2", 5);
        Combatiente b1 = hechicero(3, "B1", 100);
        Combatiente b2 = hechicero(4, "B2", 100);
        Pareja a = new Pareja(a1, a2);
        Pareja b = new Pareja(b1, b2);

        new Combate(a, b, 2, publicador, 1).ejecutar();

        assertFalse(a2.estaVivo());
        long accionesDeA2 = acciones().stream().filter(d -> d.startsWith("A2 ")).count();
        long ataquesContraA2 = acciones().stream().filter(d -> d.endsWith("contra A2")).count();
        assertEquals(1, accionesDeA2);    // solo actuo en el asalto 1, antes de caer
        assertEquals(1, ataquesContraA2); // despues de caer nadie lo vuelve a elegir
        assertEquals(1, contar(TipoAcontecimiento.ELIMINACION));
        assertEquals(70, a1.getVida());   // recibio todos los golpes posteriores
        assertEquals(70, b1.getVida());   // en el asalto 2 solo lo golpeo A1
    }

    @Test
    public void elCombateTerminaCuandoUnaParejaQuedaSinIntegrantesEnPie() {
        Combatiente a1 = hechicero(1, "A1", 5);
        Combatiente a2 = hechicero(2, "A2", 5);
        Combatiente b1 = hechicero(3, "B1", 100);
        Combatiente b2 = hechicero(4, "B2", 100);
        Pareja a = new Pareja(a1, a2);
        Pareja b = new Pareja(b1, b2);

        Pareja ganadora = new Combate(a, b, 20, publicador, 1).ejecutar();

        assertSame(b, ganadora);
        assertFalse(a.estaEnPie());
        assertEquals(4, acciones().size()); // termino en el primer asalto, no siguio hasta 20
        assertEquals(2, contar(TipoAcontecimiento.ELIMINACION));
    }

    // ---------- Límite de asaltos y desempate ----------

    @Test
    public void alLlegarAlLimiteGanaLaParejaConMasVidaTotal() {
        Combatiente a1 = hechicero(1, "A1", 100);
        Combatiente a2 = hechicero(2, "A2", 100);
        Combatiente b1 = hechicero(3, "B1", 200);
        Combatiente b2 = hechicero(4, "B2", 100);
        Pareja a = new Pareja(a1, a2);
        Pareja b = new Pareja(b1, b2);

        Pareja ganadora = new Combate(a, b, 1, publicador, 1).ejecutar();

        assertSame(b, ganadora);
        assertTrue(a.estaEnPie()); // nadie cayo: gano por vida total
        ResultadoCombate resultado = ultimoResultado();
        assertEquals(b.getId(), resultado.getIdParejaGanadora());
        assertEquals(180, resultado.getVidaFinalParejaUno());
        assertEquals(280, resultado.getVidaFinalParejaDos());
    }

    @Test
    public void conIgualVidaTotalAlLimiteGanaLaParejaDeMenorId() {
        Combatiente a1 = hechicero(1, "A1", 100);
        Combatiente a2 = hechicero(2, "A2", 100);
        Combatiente b1 = hechicero(3, "B1", 100);
        Combatiente b2 = hechicero(4, "B2", 100);
        Pareja a = new Pareja(a1, a2);
        Pareja b = new Pareja(b1, b2);

        Pareja ganadora = new Combate(b, a, 1, publicador, 1).ejecutar(); // A va como segunda

        assertEquals(a.calcularVidaTotal(), b.calcularVidaTotal());
        assertSame(a, ganadora);
        assertEquals(a.getId(), ultimoResultado().getIdParejaGanadora());
    }


    @Test
    public void cadaAsaltoLosCombatientesEnPieRecuperanEnergia() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "A1", 40, Arrays.asList(), 40, 100);
        cultivador.agregarTecnica(new CorteDeLosMilAmaneceres());
        Pareja a = new Pareja(cultivador, hechicero(2, "A2", 100));
        Pareja b = new Pareja(hechicero(3, "B1", 1000), hechicero(4, "B2", 1000));

        new Combate(a, b, 2, publicador, 1).ejecutar();

        // asalto 1: 40 (tope) - 20 = 20 ; asalto 2: 20 + 10 - 20 = 10
        assertEquals(10, cultivador.getEnergia());
    }

    @Test
    public void losUsosSeConservanEntreCombatesYVidaYEnergiaSeRecuperan() {
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Combatiente cultivador = new CultivadorDeLaEspada(1, "A1", 40, Arrays.asList(), 40, 100);
        Combatiente compañero = hechicero(2, "A2", 100);
        cultivador.agregarTecnica(corte);
        Pareja a = new Pareja(cultivador, compañero);
        Pareja b = new Pareja(hechicero(3, "B1", 1000), hechicero(4, "B2", 1000));

        new Combate(a, b, 2, publicador, 1).ejecutar();
        assertEquals(2, cultivador.getUsosTecnica(corte));

        // Entre combates queda dañado, sin energia y con el compañero caido.
        cultivador.recibirDanio(50);
        cultivador.perderEnergia(40);
        compañero.recibirDanio(1000);

        int[] estadoAlIniciar = new int[3];
        publicador.agregarObservador(acontecimiento -> {
            if (acontecimiento.getTipoAcontecimiento() == TipoAcontecimiento.INICIO_COMBATE) {
                estadoAlIniciar[0] = cultivador.getVida();
                estadoAlIniciar[1] = cultivador.getEnergia();
                estadoAlIniciar[2] = compañero.getVida();
            }
        });

        new Combate(a, b, 2, publicador, 2).ejecutar();

        assertEquals(100, estadoAlIniciar[0]);
        assertEquals(40, estadoAlIniciar[1]);
        assertEquals(100, estadoAlIniciar[2]); // el caido tambien vuelve
        assertEquals(4, cultivador.getUsosTecnica(corte)); // 2 + 2: no se reinician
    }

    // ---------- Técnicas en combate ----------

    @Test
    public void elCompañeroNoUsaLaTecnicaQueElPrimeroTodaviaPuedeUsar() {
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Combatiente a1 = new CultivadorDeLaEspada(1, "A1", 40, Arrays.asList(), 40, 100);
        Combatiente a2 = new CultivadorDeLaEspada(2, "A2", 40, Arrays.asList(), 40, 100);
        a1.agregarTecnica(corte);
        a2.agregarTecnica(corte);
        Pareja a = new Pareja(a1, a2);
        Pareja b = new Pareja(hechicero(3, "B1", 1000), hechicero(4, "B2", 1000));

        new Combate(a, b, 1, publicador, 1).ejecutar();

        assertEquals(1, a1.getUsosTecnica(corte));
        assertEquals(0, a2.getUsosTecnica(corte));
        assertTrue(acciones().get(1).startsWith("A2 realizo un ataque basico"));
    }

    @Test
    public void usaUnaTecnicaDeRecuperacionSiLeFaltaVida() {
        Combatiente b1 = hechicero(3, "B1", 100);
        b1.agregarTecnica(new CorteDeLosMilAmaneceres());
        b1.agregarTecnica(new MeditacionDelInmortal());
        Pareja a = new Pareja(hechicero(1, "A1", 100), hechicero(2, "A2", 100));
        Pareja b = new Pareja(b1, hechicero(4, "B2", 100));

        new Combate(a, b, 1, publicador, 1).ejecutar();

        // A1 y A2 golpean primero a B1, que despues prefiere curarse antes que atacar.
        assertTrue(acciones().contains("B1 utilizo la tecnica Meditacion del Inmortal contra B1"));
    }

    @Test
    public void usaUnaDefensivaSiNoTieneActivaYDespuesLaOfensiva() {
        Combatiente a1 = hechicero(1, "A1", 100);
        a1.agregarTecnica(new CorteDeLosMilAmaneceres());
        a1.agregarTecnica(new MuroDelCuerpoDorado());
        Pareja a = new Pareja(a1, hechicero(2, "A2", 100));
        Pareja b = new Pareja(hechicero(3, "B1", 1000), hechicero(4, "B2", 1000));

        new Combate(a, b, 2, publicador, 1).ejecutar();

        List<String> deA1 = acciones().stream().filter(d -> d.startsWith("A1 ")).collect(java.util.stream.Collectors.toList());
        assertEquals("A1 utilizo la tecnica Muro del Cuerpo Dorado contra A1", deA1.get(0));
        assertTrue(deA1.get(1).startsWith("A1 utilizo la tecnica Corte de los Mil Amaneceres"));
        assertTrue(a1.tieneDefensaActiva());
    }

    @Test
    public void sinEnergiaSuficienteRealizaUnAtaqueBasico() {
        Tecnica rugido = new RugidoDeLaBestiaPrimordial(); // cuesta 30 (24 para el hechicero)
        Combatiente a1 = new HechiceroDelVacio(1, "A1", 20, Arrays.asList(), 20, 100);
        a1.agregarTecnica(rugido);
        Pareja a = new Pareja(a1, hechicero(2, "A2", 100));
        Pareja b = new Pareja(hechicero(3, "B1", 100), hechicero(4, "B2", 100));

        new Combate(a, b, 1, publicador, 1).ejecutar();

        assertEquals(0, a1.getUsosTecnica(rugido));
        assertEquals("A1 realizo un ataque basico contra B1", acciones().get(0));
    }

   

    @Test
    public void elCombateComunicaInicioAccionesYResultadoConSuRonda() {
        Pareja a = new Pareja(hechicero(1, "A1", 100), hechicero(2, "A2", 100));
        Pareja b = new Pareja(hechicero(3, "B1", 100), hechicero(4, "B2", 100));

        new Combate(a, b, 3, publicador, 2).ejecutar();

        List<Acontecimiento> historial = cronista.getHistorial();
        assertEquals(TipoAcontecimiento.INICIO_COMBATE, historial.get(0).getTipoAcontecimiento());
        assertEquals(TipoAcontecimiento.RESULTADO_COMBATE, historial.get(historial.size() - 1).getTipoAcontecimiento());
        assertEquals(12, contar(TipoAcontecimiento.ACCION)); // 3 asaltos x 4 acciones
        for (Acontecimiento acontecimiento : historial) {
            assertEquals(2, acontecimiento.getRonda());
        }
    }

    @Test
    public void unCombateNoSePuedeEjecutarDosVeces() {
        Pareja a = new Pareja(hechicero(1, "A1", 100), hechicero(2, "A2", 100));
        Pareja b = new Pareja(hechicero(3, "B1", 100), hechicero(4, "B2", 100));
        Combate combate = new Combate(a, b, 1, publicador, 1);

        combate.ejecutar();

        assertThrows(IllegalStateException.class, combate::ejecutar);
    }

    @Test
    public void unCombateConDatosInvalidosSeRechaza() {
        Pareja a = new Pareja(hechicero(1, "A1", 100), hechicero(2, "A2", 100));
        Pareja b = new Pareja(hechicero(3, "B1", 100), hechicero(4, "B2", 100));

        assertThrows(IllegalArgumentException.class, () -> new Combate(a, a, 1, publicador, 1));
        assertThrows(IllegalArgumentException.class, () -> new Combate(a, b, 0, publicador, 1));
        assertThrows(IllegalArgumentException.class, () -> new Combate(a, b, 1, null, 1));
        assertThrows(IllegalArgumentException.class, () -> new Combate(a, b, 1, publicador, 0));
    }    
}
