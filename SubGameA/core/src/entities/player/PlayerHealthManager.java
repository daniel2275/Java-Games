package entities.player;

public class PlayerHealthManager {

    private float maxHealth = 100f;
    private float playerHealth = 100f;
    private Player player;

    public PlayerHealthManager(Player player){
        this.player = player;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(float playerHealth) {
        this.playerHealth = playerHealth;
        player.getPlayerActor().setCurrentHealth(playerHealth);
    }
}
