package objects;

import utilities.Timing;
import java.util.Random;

public class BulletControl {
    private Timing timing;
    private Random random;
    private static final int LAUNCH_PROBABILITY_THRESHOLD = 700;
    private static final int LAUNCH_PROBABILITY_MAX = 2100;
    private static final int TIMING_INTERVAL_SECONDS = 3; // Define interval duration for flexibility

    public BulletControl() {
        timing = new Timing(TIMING_INTERVAL_SECONDS); // Initialize Timing with a 3-second interval
        random = new Random(); // Reuse the same Random instance
        timing.start(); // Start the timing
    }

    // Method to deploy charges with a delay of at least 2 seconds
    public boolean deployAttack() {
        if (!timing.isPaused()) {
            timing.update(); // Update the timer if it's not paused
        }

        if (timing.getTimeRemaining() <= 0) {
            // Deploy the attack if the interval has passed
            int launch = random.nextInt(LAUNCH_PROBABILITY_MAX);

            timing.start(); // Reset the timer for the next attack
            return launch < LAUNCH_PROBABILITY_THRESHOLD;
        }
        return false; // Prevent deployment if the time hasn't passed
    }

    // Method to pause the deployment process
    public void pauseDeployment(boolean pause) {
        timing.pause(pause); // Pause or resume the timing
    }
}

