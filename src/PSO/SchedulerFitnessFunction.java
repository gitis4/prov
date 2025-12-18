package PSO;

import net.sourceforge.jswarm_pso.FitnessFunction;
import org.cloudbus.cloudsim.Cloudlet;
import utils.CSVCloudletLoader;
import utils.Constants;

import java.util.List;

public class SchedulerFitnessFunction extends FitnessFunction {

    public SchedulerFitnessFunction() {
        super(false); // minimize
    }

    @Override
    public double evaluate(double[] position) {

        List<Cloudlet> cloudlets = CSVCloudletLoader.getCloudlets();
        double[] dcTime = new double[Constants.NO_OF_DATA_CENTERS];

        for (int i = 0; i < cloudlets.size(); i++) {
            int dc = (int) position[i];
            Cloudlet cl = cloudlets.get(i);

            double execTime = cl.getCloudletLength() / (double) Constants.VM_MIPS;
            dcTime[dc] += execTime;
        }

        double makespan = 0;
        for (double t : dcTime) {
            makespan = Math.max(makespan, t);
        }

        return makespan;
    }
}
