package PSO;

import net.sourceforge.jswarm_pso.Particle;
import utils.CSVCloudletLoader;
import utils.Constants;

import java.util.Random;

public class SchedulerParticle extends Particle {

    public SchedulerParticle() {
        super(CSVCloudletLoader.getCloudletCount());

        Random r = new Random();
        double[] pos = new double[getDimension()];
        double[] vel = new double[getDimension()];

        for (int i = 0; i < pos.length; i++) {
            pos[i] = r.nextInt(Constants.NO_OF_DATA_CENTERS);
            vel[i] = r.nextDouble();
        }

        setPosition(pos);
        setVelocity(vel);
    }
}
