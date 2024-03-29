package entities.enemies;

public class EnemyHealthManager {
    private float enemyHealth = 100f;
    private float maxHealth = 100f;

    private Enemy enemy;

    public EnemyHealthManager(Enemy enemy) {
        this.enemy = enemy;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getEnemyHealth() {
        return enemyHealth;
    }

    public void setEnemyHealth(float playerHealth) {
        this.enemyHealth = playerHealth;
        enemy.getEnemyActor().setCurrentHealth(playerHealth);
    }




}
