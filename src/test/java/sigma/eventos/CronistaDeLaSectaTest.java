package sigma.eventos;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CronistaDeLaSectaTest {

    private static Acontecimiento resultado(int ronda, int uno, int dos, int ganadora, int vidaUno, int vidaDos) {
        return new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE, ronda, "Resultado",
                new ResultadoCombate(ronda, uno, dos, ganadora, vidaUno, vidaDos));
    }

    private static List<Integer> ids(List<PosicionPareja> tabla) {
        List<Integer> ids = new ArrayList<>();
        for (PosicionPareja posicion : tabla) {
            ids.add(posicion.getIdPareja());
        }
        return ids;
    }

    @Test
    public void laTablaSeOrdenaPorUltimaRondaVidaFinalEIdentificador() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        // Ronda 1
        cronista.recibirAcontecimiento(resultado(1, 1, 2, 1, 150, 0));
        cronista.recibirAcontecimiento(resultado(1, 3, 4, 3, 120, 0));
        cronista.recibirAcontecimiento(resultado(1, 5, 6, 6, 0, 90));
        cronista.recibirAcontecimiento(resultado(1, 7, 8, 7, 30, 0));
        // Ronda 2
        cronista.recibirAcontecimiento(resultado(2, 1, 3, 3, 0, 80));
        cronista.recibirAcontecimiento(resultado(2, 6, 7, 6, 60, 0));
        // Final
        cronista.recibirAcontecimiento(resultado(3, 3, 6, 6, 0, 40));

        List<PosicionPareja> tabla = cronista.getTablaPosiciones();

        // 6 y 3 llegaron a la ronda 3 (6 termino con mas vida);
        // 1 y 7 a la ronda 2, ambos con 0 de vida -> por id;
        // 2, 4, 5 y 8 cayeron en la ronda 1 con 0 de vida -> por id.
        assertEquals(List.of(6, 3, 1, 7, 2, 4, 5, 8), ids(tabla));
        assertEquals(3, tabla.get(0).getUltimaRonda());
        assertEquals(40, tabla.get(0).getVidaFinal());
    }

    @Test
    public void enLaMismaRondaQuedaPrimeroQuienTerminoConMasVida() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        cronista.recibirAcontecimiento(resultado(1, 1, 2, 1, 50, 20));
        cronista.recibirAcontecimiento(resultado(1, 3, 4, 3, 70, 45));

        List<PosicionPareja> tabla = cronista.getTablaPosiciones();

        assertEquals(List.of(3, 1, 4, 2), ids(tabla));
    }

    @Test
    public void laTablaUsaLaVidaDelUltimoCombateDeCadaPareja() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        cronista.recibirAcontecimiento(resultado(1, 1, 2, 1, 200, 0));
        cronista.recibirAcontecimiento(resultado(2, 1, 3, 3, 5, 50));

        List<PosicionPareja> tabla = cronista.getTablaPosiciones();

        PosicionPareja pareja1 = tabla.stream().filter(p -> p.getIdPareja() == 1).findFirst().orElseThrow();
        assertEquals(2, pareja1.getUltimaRonda());
        assertEquals(5, pareja1.getVidaFinal());
    }

    @Test
    public void elCronistaRegistraTodosLosAcontecimientosEnOrden() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Acontecimiento inicio = new Acontecimiento(TipoAcontecimiento.INICIO_COMBATE, 1, "Inicio");
        Acontecimiento accion = new Acontecimiento(TipoAcontecimiento.ACCION, 1, "Accion");

        cronista.recibirAcontecimiento(inicio);
        cronista.recibirAcontecimiento(accion);

        assertEquals(List.of(inicio, accion), cronista.getHistorial());
        assertTrue(cronista.getTablaPosiciones().isEmpty()); // sin resultados no hay tabla
    }

    @Test
    public void elHistorialNoSePuedeModificarDesdeAfuera() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        cronista.recibirAcontecimiento(new Acontecimiento(TipoAcontecimiento.ACCION, 1, "Accion"));

        cronista.getHistorial().clear();

        assertEquals(1, cronista.getHistorial().size());
    }

    @Test
    public void sePuedenSumarNuevosInteresadosSinCambiarElPublicador() {
        PublicadorAcontecimientos publicador = new PublicadorAcontecimientos();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        List<Acontecimiento> recibidosPorOtro = new ArrayList<>();
        publicador.agregarObservador(cronista);
        publicador.agregarObservador(recibidosPorOtro::add);

        Acontecimiento accion = new Acontecimiento(TipoAcontecimiento.ACCION, 1, "Accion");
        publicador.notificar(accion);

        assertEquals(List.of(accion), cronista.getHistorial());
        assertEquals(List.of(accion), recibidosPorOtro);
    }

    @Test
    public void unObservadorNoSePuedeRegistrarDosVeces() {
        PublicadorAcontecimientos publicador = new PublicadorAcontecimientos();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        publicador.agregarObservador(cronista);

        assertThrows(IllegalArgumentException.class, () -> publicador.agregarObservador(cronista));
    }

    @Test
    public void unResultadoConDatosInvalidosSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> new ResultadoCombate(0, 1, 2, 1, 10, 0));
        assertThrows(IllegalArgumentException.class, () -> new ResultadoCombate(1, 1, 1, 1, 10, 0));
        assertThrows(IllegalArgumentException.class, () -> new ResultadoCombate(1, 1, 2, 3, 10, 0));
        assertThrows(IllegalArgumentException.class, () -> new ResultadoCombate(1, 1, 2, 1, -1, 0));
    }

    @Test
    public void elAcontecimientoGuardaLosDatosDelMomentoEnQueOcurrio() {
        ResultadoCombate resultado = new ResultadoCombate(2, 1, 3, 3, 10, 80);
        Acontecimiento acontecimiento = new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE, 2, "Fin", resultado);

        assertEquals(2, acontecimiento.getRonda());
        assertEquals(3, acontecimiento.getResultadoCombate().getIdParejaGanadora());
        assertEquals(80, acontecimiento.getResultadoCombate().getVidaFinalParejaDos());
        assertThrows(IllegalArgumentException.class,
                () -> new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE, 2, "Sin datos"));
    }
}
