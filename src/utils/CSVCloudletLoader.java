package utils;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVCloudletLoader {

    private static final List<Cloudlet> cloudlets = new ArrayList<>();

    public static void load(String csvPath, int userId) {
        cloudlets.clear();

        UtilizationModel um = new UtilizationModelFull();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                int id = Integer.parseInt(tokens[0]);
                long length = Long.parseLong(tokens[9]);
                long fileSize = Long.parseLong(tokens[10]);
                long outputSize = Long.parseLong(tokens[11]);
                int pes = Integer.parseInt(tokens[12]);

                Cloudlet cl = new Cloudlet(
                        id,
                        length,
                        pes,
                        fileSize,
                        outputSize,
                        um,
                        um,
                        um
                );

                cl.setUserId(userId);
                cloudlets.add(cl);
            }

            Constants.NO_OF_TASKS = cloudlets.size();
            System.out.println("Loaded " + cloudlets.size() + " cloudlets from CSV");

        } catch (Exception e) {
            throw new RuntimeException("Error loading CSV cloudlets", e);
        }
    }

    public static List<Cloudlet> getCloudlets() {
        return cloudlets;
    }

    public static int getCloudletCount() {
        return cloudlets.size();
    }
}
