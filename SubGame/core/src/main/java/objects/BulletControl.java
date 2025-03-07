package objects;

import utilities.Timing;
import java.util.Random;

public class BulletControl {
    private Timing timing;
    private Random random;
    private static final int LAUNCH_PROBABILITY_THRESHOLD = 700;
    private static final int LAUNCH_PROBABILITY_MAX = 1400;
    private static final int TIMING_INTERVAL_SECONDS = 2; // Define interval



    public BulletControl() {
        timing = new Timing(TIMING_INTERVAL_SECONDS); // Initialize Timing
        random = new Random(); // Reuse the same Random instance
        timing.start(); // Start the timing
    }

    // When deployAttack is called and returns true, the timing is RESET
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

