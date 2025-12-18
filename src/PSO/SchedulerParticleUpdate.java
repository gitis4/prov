package PSO;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;
import utils.Constants;

public class SchedulerParticleUpdate extends ParticleUpdate {

    private static final double W = 0.7;
    private static final double C = 1.5;

    public SchedulerParticleUpdate(Particle p) {
        super(p);
    }

    @Override
    public void update(Swarm swarm, Particle particle) {

        double[] x = particle.getPosition();
        double[] v = particle.getVelocity();
        double[] pbest = particle.getBestPosition();
        double[] gbest = swarm.getBestPosition();

        for (int i = 0; i < x.length; i++) {

            v[i] = W * v[i]
                 + C * Math.random() * (pbest[i] - x[i])
                 + C * Math.random() * (gbest[i] - x[i]);

            x[i] = Math.round(x[i] + v[i]);

            if (x[i] < 0) x[i] = 0;
            if (x[i] >= Constants.NO_OF_DATA_CENTERS)
                x[i] = Constants.NO_OF_DATA_CENTERS - 1;
        }
    }
}
