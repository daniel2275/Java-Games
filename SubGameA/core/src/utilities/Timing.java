package utilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

// handles fire rate
public class Timing {
    private float startTime;
    private float pausedTime = 0;
    private float timeRemaining = 0;
    private float duration;
    private float pausedTimeRemaining = 0;
    private boolean pause;

    public Timing(float interval) {
        this.startTime = 0;
        this.duration = interval;
        this.pause = false;
    }

    public void reset() {
        startTime = pausedTime = timeRemaining = pausedTimeRemaining = 0;
        pause = false;
    }

    // initialize/refresh timer (create method)
    public void init() {
        timeRemaining = 0.0f;
        startTime = TimeUtils.nanoTime();
    }

//     update current - handle count down - (render method)
    public void update() {
        float currentTime = TimeUtils.nanoTime();
        float elapsedSeconds = MathUtils.nanoToSec * (currentTime - startTime);
        timeRemaining = duration - elapsedSeconds;
        if (timeRemaining <= 0.0f) {
            timeRemaining = 0.0f;
        }
    }

    // handle timer adjustment for paused time (render method - pause triggered: accumulate time - not paused: consume accumulated time) sets pause bool value (call before update)
    public boolean checkPaused(boolean paused) {
        boolean wasPaused = this.pause;
        if (paused && !wasPaused && startTime > 0) {
            pausedTimeRemaining = timeRemaining;
            pausedTime = TimeUtils.nanoTime();
        } else if (!paused && pausedTimeRemaining > 0 && startTime > 0) {
            long resumedTime = TimeUtils.nanoTime();
            startTime += resumedTime - pausedTime;
            pausedTimeRemaining = 0;
            pausedTime = 0;
        }
        this.pause = paused;
        return wasPaused;
    }

    public float getStartTime() {
        return startTime;
    }

    public boolean isPause() {
        return pause;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
