package entities.player;

public class PlayerHealthManager {

    private int maxHealth = 100;
    private int playerHealth = 100;
    private Player player;

    public PlayerHealthManager(Player player){
        this.player = player;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
        player.getPlayerActor().setCurrentHealth(playerHealth);
    }
}
