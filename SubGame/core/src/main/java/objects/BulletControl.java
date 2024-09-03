package objects;

import utilities.Timing;

public class BulletControl {
    private Timing timing;

    public BulletControl() {
        timing = new Timing(3); // Initialize Timing with a 2-second interval
        timing.start(); // Start the timing
    }

    // Method to deploy charges with a delay of at least 2 seconds
    public boolean deployAttack() {
        if (!timing.isPaused()) {
            timing.update(); // Update timing
        }

        if (timing.getTimeRemaining() <= 0) {
            // If at least 2 seconds have passed, proceed with deployment
            java.util.Random rnd = new java.util.Random();
            int launch = rnd.nextInt(2100);

            timing.start(); // Reset timing after deployment
            return launch < 700;
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
