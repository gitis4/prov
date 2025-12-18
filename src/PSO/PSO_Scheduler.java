package PSO;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import utils.CSVCloudletLoader;
import utils.Constants;
import utils.DatacenterCreator;

import java.text.DecimalFormat;
import java.util.*;

public class PSO_Scheduler {

    private static List<Vm> vmList;
    private static PSO pso;
    private static double[] mapping;

    public static void main(String[] args) {

        Log.printLine("Starting PSO Scheduler with CSV Cloudlets...");

        try {
            int numUser = 1;
            CloudSim.init(numUser, Calendar.getInstance(), false);

            // 1. Create Datacenters
            Datacenter[] datacenters = new Datacenter[Constants.NO_OF_DATA_CENTERS];
            for (int i = 0; i < Constants.NO_OF_DATA_CENTERS; i++) {
                datacenters[i] = DatacenterCreator.createDatacenter("DC_" + i);
            }

            // 2. Create Broker
            PSODatacenterBroker broker = new PSODatacenterBroker("Broker_0");
            int brokerId = broker.getId();

            // 3. Load CSV Cloudlets
            CSVCloudletLoader.load("intel_lab_1k_cloudlets.csv", brokerId);

            // 4. Create VMs (1 VM per Datacenter, VM ID == DC ID)
            vmList = createVMs(brokerId);
            broker.submitVmList(vmList);

            // 5. Run PSO
            pso = new PSO();
            mapping = pso.run();
            broker.setMapping(mapping);

            // 6. Submit Cloudlets
            broker.submitCloudletList(CSVCloudletLoader.getCloudlets());

            // 7. Start Simulation
            CloudSim.startSimulation();

            // 8. Get Results
            List<Cloudlet> resultList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            // 9. Print Results (CloudSim style)
            printCloudletList(resultList);

            // 10. Print Best Fitness (Makespan)
            System.out.println("\nBest fitness (makespan): " + pso.getBestFitness());

            Log.printLine("PSO Scheduler finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= VM CREATION =================
    private static List<Vm> createVMs(int userId) {
        List<Vm> list = new ArrayList<>();

        for (int i = 0; i < Constants.NO_OF_DATA_CENTERS; i++) {
            Vm vm = new Vm(
                    i,                     // VM ID == Datacenter ID
                    userId,
                    Constants.VM_MIPS,
                    1,
                    512,
                    1000,
                    10000,
                    "Xen",
                    new CloudletSchedulerSpaceShared()
            );
            list.add(vm);
        }
        return list;
    }

    // ================= OUTPUT PRINTING =================
    private static void printCloudletList(List<Cloudlet> list) {

        String indent = "    ";
        DecimalFormat dft = new DecimalFormat("###.##");

        System.out.println();
        System.out.println("========== OUTPUT ==========");
        System.out.println(
                "Cloudlet ID" + indent +
                "STATUS" + indent +
                "Data center ID" + indent +
                "VM ID" + indent +
                "Time" + indent +
                "Start Time" + indent +
                "Finish Time"
        );

        for (Cloudlet cloudlet : list) {

            System.out.print(
                    String.format("%10d", cloudlet.getCloudletId()) + indent
            );

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {

                System.out.println(
                        "SUCCESS" + indent +
                        String.format("%15d", cloudlet.getResourceId()) + indent +
                        String.format("%6d", cloudlet.getVmId()) + indent +
                        String.format("%8s", dft.format(cloudlet.getActualCPUTime())) + indent +
                        String.format("%10s", dft.format(cloudlet.getExecStartTime())) + indent +
                        String.format("%11s", dft.format(cloudlet.getFinishTime()))
                );
            }
        }
    }
}
