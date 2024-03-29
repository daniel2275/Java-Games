package entities.enemies;


import com.mygdx.sub.SubGame;
import gamestates.GamePlayScreen;
import utilities.HelpMethods;
import utilities.Timing;

public class EnemyBuilder {
    private String name;
    private long delay = 0;
    private int spawnPosX = 0;
    private int spawnPosY = 0;
    private int flipX = 1;
    private String spriteAtlas = "";
    private float speed = 0f;
    private boolean aggro = false;
    private float currentHealth = 0f;
    private float maxHealth = 0f;
    private boolean sub = false;
    private int enemyPoints = 10;
    private int enemyWidth = 64;
    private int enemyHeight = 32;
    private SubGame subGame;

    private GamePlayScreen gamePlayScreen;

    public EnemyBuilder(GamePlayScreen gamePlayScreen) {
        this.gamePlayScreen = gamePlayScreen;
    }

    public EnemyBuilder withName(String name){
        this.name = name;
        return this;
    }
    public EnemyBuilder withDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public EnemyBuilder withSpawnPosX(int spawnPosX) {
        this.spawnPosX = spawnPosX;
        return this;
    }

    public EnemyBuilder withSpawnPosY(int spawnPosY) {
        this.spawnPosY = spawnPosY;
        return this;
    }

    public EnemyBuilder withFlipX(int flipX) {
        this.flipX = flipX;
        return this;
    }

    public EnemyBuilder withSpriteAtlas(String spriteAtlas) {
        this.spriteAtlas = spriteAtlas;
        return this;
    }

    public EnemyBuilder withSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public EnemyBuilder withAggro(boolean aggro) {
        this.aggro = aggro;
        return this;
    }

    public EnemyBuilder withCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
        return this;
    }

    public EnemyBuilder withMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }

    public EnemyBuilder withSub(boolean sub) {
        this.sub = sub;
        return this;
    }

    public EnemyBuilder withEnemyPoints(int enemyPoints) {
        this.enemyPoints = enemyPoints;
        return this;
    }

    public EnemyBuilder withEnemyWidth(int enemyWidth) {
        this.enemyWidth = enemyWidth;
        return this;
    }

    public EnemyBuilder withEnemyHeight(int enemyHeight) {
        this.enemyHeight = enemyHeight;
        return this;
    }

    public Enemy build() {
        Enemy enemy = new Enemy(gamePlayScreen, name, delay, spawnPosX, spawnPosY, flipX, spriteAtlas, speed, aggro, currentHealth, maxHealth, sub, enemyPoints, enemyWidth, enemyHeight, subGame);
        enemy.setFadingAnimation(new HelpMethods.FadingAnimation(200));
        enemy.setFadeDelay(new Timing(7));
        return enemy;
    }
}


