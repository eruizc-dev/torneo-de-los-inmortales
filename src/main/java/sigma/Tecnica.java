package sigma;

/**
 * Tecnica
 */
public abstract class Tecnica {
    private final String nombre;
    private final int costoEnergia;

    protected Tecnica(String nombre, int energia) {
        this.nombre = nombre;
        this.costoEnergia = energia;
    }
    
    public int getCostoEnergia() {
    	return costoEnergia;
    }

    abstract void aplicar(Combatiente lanzador, Combatiente target);

    public class CorteDeLosMilAmaneceres extends Tecnica {
        public CorteDeLosMilAmaneceres() {
            super("Corte de los Mil Amaneceres", 20);
        }

		@Override
		void aplicar(Combatiente lanzador, Combatiente target) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'aplicar'");
		}
    }

    public class MuroDelCuerpoDorado extends Tecnica {
		public MuroDelCuerpoDorado() {
			super("Muro del Cuerpo Dorado", 10);
		}

		@Override
		void aplicar(Combatiente lanzador, Combatiente target) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'aplicar'");
		}
    }

    public class DominioDeLaNada extends Tecnica {

		protected DominioDeLaNada(String nombre, int energia) {
			super("Dominio de la Nada", 10);
		}

		@Override
		void aplicar(Combatiente lanzador, Combatiente target) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'aplicar'");
		}
    }

    public class RugidoDeLaBestiaPrimordial extends Tecnica {

		protected RugidoDeLaBestiaPrimordial(String nombre, int energia) {
			super("Rugido de la Bestia Primordial", 10);
		}

		@Override
		void aplicar(Combatiente lanzador, Combatiente target) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'aplicar'");
		}
    }

    public class MeditaciónDelInmortal extends Tecnica {

		protected MeditaciónDelInmortal(String nombre, int energia) {
			super("Meditación del Inmortal", 10);
		}

		@Override
		void aplicar(Combatiente lanzador, Combatiente target) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'aplicar'");
		}
    }
}
