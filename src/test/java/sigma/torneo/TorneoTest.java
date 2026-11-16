package sigma.torneo;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import sigma.combatientes.Combatiente;
import sigma.combatientes.CreadorCombatientes;
import sigma.combatientes.HechiceroDelVacio;
import sigma.equipamiento.AnilloEspiritual;
import sigma.equipamiento.ArmaduraDorada;
import sigma.equipamiento.EspadaCelestial;
import sigma.escuelas.Escuela;
import sigma.eventos.Acontecimiento;
import sigma.eventos.CronistaDeLaSecta;
import sigma.eventos.PosicionPareja;
import sigma.eventos.ResultadoCombate;
import sigma.eventos.TipoAcontecimiento;
import sigma.tecnicas.CorteDeLosMilAmaneceres;
import sigma.tecnicas.DominioDeLaNada;
import sigma.tecnicas.MeditacionDelInmortal;
import sigma.tecnicas.MuroDelCuerpoDorado;
import sigma.tecnicas.RugidoDeLaBestiaPrimordial;

import static org.junit.jupiter.api.Assertions.*;

public class TorneoTest {

    private static final String[] TIPOS = {
            "Cultivador de la Espada", "Hechicero del Vacío", "Monje del Cuerpo Celestial" };

    private static class Escenario {
        final Torneo torneo = new Torneo();
        final CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        final List<Pareja> parejas = new ArrayList<>();

        Escenario(int cantidadParejas) {
            Escuela espada = new Escuela("Escuela de la Espada Celestial",
                    Arrays.asList(new CorteDeLosMilAmaneceres(), new RugidoDeLaBestiaPrimordial()),
                    Arrays.asList(new EspadaCelestial()));
            Escuela palacio = new Escuela("Palacio del Vacío",
                    Arrays.asList(new DominioDeLaNada(), new MeditacionDelInmortal()),
                    Arrays.asList(new AnilloEspiritual()));
            Escuela monasterio = new Escuela("Monasterio del Cuerpo Dorado",
                    Arrays.asList(new MuroDelCuerpoDorado(), new MeditacionDelInmortal()),
                    Arrays.asList(new ArmaduraDorada()));
            Escuela[] escuelas = { espada, palacio, monasterio };

            CreadorCombatientes creador = new CreadorCombatientes();
            torneo.agregarObservaodr(cronista);
            for (int i = 0; i < cantidadParejas; i++) {
                Combatiente uno = creador.crear(TIPOS[i % 3], "C" + (2 * i + 1), escuelas[i % 3]);
                Combatiente dos = creador.crear(TIPOS[(i + 1) % 3], "C" + (2 * i + 2), escuelas[(i + 2) % 3]);
                Pareja pareja = new Pareja(uno, dos);
                parejas.add(pareja);
                torneo.inscribir(pareja);
            }
        }

        List<ResultadoCombate> resultados() {
            List<ResultadoCombate> resultados = new ArrayList<>();
            for (Acontecimiento a : cronista.getHistorial()) {
                if (a.getTipoAcontecimiento() == TipoAcontecimiento.RESULTADO_COMBATE) {
                    resultados.add(a.getResultadoCombate());
                }
            }
            return resultados;
        }

  
        int indice(int idPareja) {
            for (int i = 0; i < parejas.size(); i++) {
                if (parejas.get(i).getId() == idPareja) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * Historial expresado sin depender del id global de las parejas,
         * para poder comparar dos torneos armados igual.
         */
        List<String> historialComparable() {
            List<String> lineas = new ArrayList<>();
            for (Acontecimiento a : cronista.getHistorial()) {
                String linea = a.getTipoAcontecimiento() + "|ronda " + a.getRonda();
                if (a.getTipoAcontecimiento() == TipoAcontecimiento.RESULTADO_COMBATE) {
                    ResultadoCombate r = a.getResultadoCombate();
                    linea += "|" + indice(r.getIdParejaUno()) + " vs " + indice(r.getIdParejaDos())
                            + "|gana " + indice(r.getIdParejaGanadora())
                            + "|" + r.getVidaFinalParejaUno() + "-" + r.getVidaFinalParejaDos();
                } else if (a.getTipoAcontecimiento() != TipoAcontecimiento.PROCLAMACION) {
                    linea += "|" + a.getDescripcion();
                }
                lineas.add(linea);
            }
            return lineas;
        }
    }

    private static Pareja parejaSimple(int idUno, int idDos) {
        return new Pareja(new HechiceroDelVacio(idUno, "C" + idUno, 40, Arrays.asList(), 40, 100),
                new HechiceroDelVacio(idDos, "C" + idDos, 40, Arrays.asList(), 40, 100));
    }

 

    @Test
    public void unaParejaNoPuedeInscribirseDosVeces() {
        Torneo torneo = new Torneo();
        Pareja pareja = parejaSimple(1, 2);
        torneo.inscribir(pareja);

        assertThrows(IllegalArgumentException.class, () -> torneo.inscribir(pareja));
    }

    @Test
    public void unCombatienteNoPuedeInscribirseDosVecesEnDistintasParejas() {
        Torneo torneo = new Torneo();
        Combatiente aurelio = new HechiceroDelVacio(1, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula", 40, Arrays.asList(), 40, 100);
        Combatiente kael = new HechiceroDelVacio(3, "Kael", 40, Arrays.asList(), 40, 100);
        torneo.inscribir(new Pareja(aurelio, nebula));

        assertThrows(IllegalArgumentException.class, () -> torneo.inscribir(new Pareja(kael, aurelio)));
    }

    @Test
    public void unaInscripcionRechazadaNoDejaRegistradoANingunIntegrante() {
        Torneo torneo = new Torneo();
        Combatiente aurelio = new HechiceroDelVacio(1, "Aurelio", 40, Arrays.asList(), 40, 100);
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula", 40, Arrays.asList(), 40, 100);
        Combatiente kael = new HechiceroDelVacio(3, "Kael", 40, Arrays.asList(), 40, 100);
        Combatiente darian = new HechiceroDelVacio(4, "Darian", 40, Arrays.asList(), 40, 100);
        torneo.inscribir(new Pareja(aurelio, nebula));

        assertThrows(IllegalArgumentException.class, () -> torneo.inscribir(new Pareja(kael, aurelio)));
        assertDoesNotThrow(() -> torneo.inscribir(new Pareja(kael, darian)));
    }

    @Test
    public void elTorneoNoSeIniciaConUnaCantidadDeParejasDistintaDe4u8() {
        for (int cantidad : new int[] { 0, 1, 2, 3, 5, 6, 7 }) {
            Torneo torneo = new Torneo();
            for (int i = 0; i < cantidad; i++) {
                torneo.inscribir(parejaSimple(2 * i + 1, 2 * i + 2));
            }
            assertThrows(IllegalStateException.class, torneo::iniciar, "con " + cantidad + " parejas");
        }
    }

    @Test
    public void noSePuedeInscribirUnaNovenaPareja() {
        Escenario escenario = new Escenario(8);

        assertThrows(IllegalStateException.class, () -> escenario.torneo.inscribir(parejaSimple(101, 102)));
    }

    @Test
    public void noSePuedeInscribirNiReiniciarUnTorneoYaIniciado() {
        Escenario escenario = new Escenario(4);
        escenario.torneo.iniciar();

        assertThrows(IllegalStateException.class, () -> escenario.torneo.inscribir(parejaSimple(101, 102)));
        assertThrows(IllegalStateException.class, escenario.torneo::iniciar);
    }



    @Test
    public void conCuatroParejasLosCrucesRespetanElOrdenDeInscripcion() {
        Escenario escenario = new Escenario(4);
        List<Pareja> p = escenario.parejas;

        Pareja campeona = escenario.torneo.iniciar();

        List<ResultadoCombate> r = escenario.resultados();
        assertEquals(3, r.size());
        // Ronda 1: primera vs segunda, tercera vs cuarta
        assertEquals(1, r.get(0).getRonda());
        assertEquals(p.get(0).getId(), r.get(0).getIdParejaUno());
        assertEquals(p.get(1).getId(), r.get(0).getIdParejaDos());
        assertEquals(1, r.get(1).getRonda());
        assertEquals(p.get(2).getId(), r.get(1).getIdParejaUno());
        assertEquals(p.get(3).getId(), r.get(1).getIdParejaDos());
        // Ronda 2: ganadora del cruce 1 vs ganadora del cruce 2
        assertEquals(2, r.get(2).getRonda());
        assertEquals(r.get(0).getIdParejaGanadora(), r.get(2).getIdParejaUno());
        assertEquals(r.get(1).getIdParejaGanadora(), r.get(2).getIdParejaDos());
        assertEquals(campeona.getId(), r.get(2).getIdParejaGanadora());
    }

    @Test
    public void conOchoParejasSeJueganTresRondasRespetandoElOrdenDeLosCruces() {
        Escenario escenario = new Escenario(8);
        List<Pareja> p = escenario.parejas;

        Pareja campeona = escenario.torneo.iniciar();

        List<ResultadoCombate> r = escenario.resultados();
        assertEquals(7, r.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(1, r.get(i).getRonda());
            assertEquals(p.get(2 * i).getId(), r.get(i).getIdParejaUno());
            assertEquals(p.get(2 * i + 1).getId(), r.get(i).getIdParejaDos());
        }
        assertEquals(2, r.get(4).getRonda());
        assertEquals(r.get(0).getIdParejaGanadora(), r.get(4).getIdParejaUno());
        assertEquals(r.get(1).getIdParejaGanadora(), r.get(4).getIdParejaDos());
        assertEquals(r.get(2).getIdParejaGanadora(), r.get(5).getIdParejaUno());
        assertEquals(r.get(3).getIdParejaGanadora(), r.get(5).getIdParejaDos());
        assertEquals(3, r.get(6).getRonda());
        assertEquals(r.get(4).getIdParejaGanadora(), r.get(6).getIdParejaUno());
        assertEquals(r.get(5).getIdParejaGanadora(), r.get(6).getIdParejaDos());
        assertEquals(campeona.getId(), r.get(6).getIdParejaGanadora());
    }

    @Test
    public void elTorneoTerminaProclamandoALaUnicaParejaQueQuedaEnPie() {
        Escenario escenario = new Escenario(4);

        Pareja campeona = escenario.torneo.iniciar();

        List<Acontecimiento> historial = escenario.cronista.getHistorial();
        Acontecimiento ultimo = historial.get(historial.size() - 1);
        assertEquals(TipoAcontecimiento.PROCLAMACION, ultimo.getTipoAcontecimiento());
        assertTrue(ultimo.getDescripcion().contains(String.valueOf(campeona.getId())));
        assertEquals(1, historial.stream()
                .filter(a -> a.getTipoAcontecimiento() == TipoAcontecimiento.PROCLAMACION).count());
    }

   
    @Test
    public void laTablaDelTorneoEncabezaLaCampeonaYQuedaOrdenada() {
        Escenario escenario = new Escenario(8);

        Pareja campeona = escenario.torneo.iniciar();

        List<PosicionPareja> tabla = escenario.cronista.getTablaPosiciones();
        assertEquals(8, tabla.size());
        assertEquals(campeona.getId(), tabla.get(0).getIdPareja());
        for (int i = 0; i < tabla.size() - 1; i++) {
            PosicionPareja actual = tabla.get(i);
            PosicionPareja siguiente = tabla.get(i + 1);
            assertTrue(actual.getUltimaRonda() >= siguiente.getUltimaRonda());
            if (actual.getUltimaRonda() == siguiente.getUltimaRonda()) {
                assertTrue(actual.getVidaFinal() >= siguiente.getVidaFinal());
                if (actual.getVidaFinal() == siguiente.getVidaFinal()) {
                    assertTrue(actual.getIdPareja() < siguiente.getIdPareja());
                }
            }
        }
    }

    @Test
    public void dosTorneosArmadosIgualProducenElMismoCampeonYElMismoHistorial() {
        Escenario primero = new Escenario(8);
        Escenario segundo = new Escenario(8);

        Pareja campeonaUno = primero.torneo.iniciar();
        Pareja campeonaDos = segundo.torneo.iniciar();

        assertEquals(primero.indice(campeonaUno.getId()), segundo.indice(campeonaDos.getId()));
        assertEquals(campeonaUno.getCombatienteUno().getNombre(), campeonaDos.getCombatienteUno().getNombre());
        assertEquals(campeonaUno.getCombatienteDos().getNombre(), campeonaDos.getCombatienteDos().getNombre());
        assertFalse(primero.historialComparable().isEmpty());
        assertEquals(primero.historialComparable(), segundo.historialComparable());
    }
}
