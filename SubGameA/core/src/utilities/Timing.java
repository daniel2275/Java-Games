package utilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

// Handles timing and countdown
public class Timing {
    private float startTime;
    private float pausedTime;
    private float timeRemaining;
    private float duration;
    private boolean paused;

    public Timing(float interval) {
        reset();
        this.duration = interval;
    }

    // Resets the timing variables
    public void reset() {
        startTime = pausedTime = timeRemaining = 0;
        paused = false;
    }

    // Starts or restarts the timing
    public void start() {
        timeRemaining = duration;
        startTime = TimeUtils.nanoTime();
    }

    // Updates the timing based on elapsed time
    public void update() {
        if (!paused) {
            long currentTime = TimeUtils.nanoTime();
            float elapsedSeconds = MathUtils.nanoToSec * (currentTime - startTime);
            timeRemaining = Math.max(0, duration - elapsedSeconds);
        }
    }

    // Pauses or resumes the timing
    public boolean pause(boolean pause) {
        boolean wasPaused = paused;
        if (pause && !wasPaused && startTime > 0) {
            pausedTime = TimeUtils.nanoTime();
        } else if (!pause && wasPaused && startTime > 0) {
            long resumedTime = TimeUtils.nanoTime();
            startTime += resumedTime - pausedTime;
        }
        paused = pause;
        return wasPaused;
    }

    // Getters and setters
    public float getStartTime() {
        return startTime;
    }

    public boolean isPaused() {
        return paused;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
