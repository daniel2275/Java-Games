package objects.depthChage;

import utilities.Timing;

public class ChargeDeployer {
    private Timing timing;

    public ChargeDeployer() {
        timing = new Timing(2); // Initialize Timing with a 2-second interval
        timing.start(); // Start the timing
    }

    // Method to deploy charges with a delay of at least 2 seconds
    public boolean deployCharges() {
        if (!timing.isPaused()) {
            timing.update(); // Update timing
        }

        if (timing.getTimeRemaining() <= 0) {
            // If at least 2 seconds have passed, proceed with deployment
            java.util.Random rnd = new java.util.Random();
            int launch = rnd.nextInt(3000);
            return launch < 10;
        } else {
            // If less than 2 seconds have passed, prevent deployment
            return false;
        }
    }

    // Method to pause the deployment process
    public void pauseDeployment(boolean pause) {
        timing.pause(pause); // Pause or resume the timing
    }
}

