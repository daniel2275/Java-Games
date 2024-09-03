package UI.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class UpgradeManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient Preferences preferences;

    private Map<String, SaveGame> saveGames;
    private Map<String, Upgrade> upgrades;

    public UpgradeManager() {
        this.saveGames = new HashMap<>();
        this.upgrades = new HashMap<>();
    }

    // SaveGame
    public Map<String, SaveGame> getSaveGameUpgrades() {
        return saveGames;
    }

    public Map<String, SaveGame> getSavedValues() {
        return saveGames;
    }

    public SaveGame getSaveGame(String name) {
        return saveGames.get(name);
    }

    public void addOrUpdateSaveGame(String name, int playerScore, int playerHealth, int level, float volume) {
        // Check if the SaveGame object exists
        SaveGame saveGame = this.saveGames.get(name);

        if (saveGame != null) {
            // Update the SaveGame fields with new values
            saveGame.setPlayerScore(playerScore);
            saveGame.setPlayerHealth(playerHealth);
            saveGame.setLevel(level);
            saveGame.setVolume(volume);
        } else {
            // Create a new SaveGame object and put it into the map
            saveGame = new SaveGame(playerScore, playerHealth, level, volume);
            this.saveGames.put(name, saveGame);
        }
    }

    // Upgrades
    public Map<String, Upgrade> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(Map<String, Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    public Upgrade getUpgrade(String name) {
        return upgrades.get(name);
    }

    public void addUpgrade(String name, int cost, float minUpgrade, float maxUpgrade, float actualValue, int ticks, int upgradeLevel) {
        upgrades.put(name, new Upgrade(cost, minUpgrade ,maxUpgrade, actualValue, ticks, upgradeLevel));
    }

    public void addOrUpdateUpgrade(String name, int cost, float minUpgrade, float maxUpgrade, float actualValue, int ticks, int upgradeLevel) {
        // Check if the Upgrade object exists
        Upgrade upgrade = this.upgrades.get(name);
        if (upgrade != null) {
            // Update the Upgrade fields with new values
            upgrade.setCost(cost);
            upgrade.setMinUpgrade(minUpgrade);
            upgrade.setMaxUpgrade(maxUpgrade);
            upgrade.setActualValue(actualValue);
            upgrade.setTicks(ticks);
            upgrade.setUpgradeLevel(upgradeLevel);
        } else {
            // Create a new Upgrade object and put it into the map
            upgrade = new Upgrade(cost, minUpgrade, maxUpgrade, actualValue, ticks, upgradeLevel);
            this.upgrades.put(name, upgrade);
        }
    }

    public void buyUpgrade(String name) {
        Upgrade upgrade = upgrades.get(name);
        if (upgrade != null && upgrade.getUpgradeLevel() < upgrade.getTicks()) {
            int increment = upgrade.getCost() * 50 / 100;
            int newCost = upgrade.getCost() + increment;
            if (newCost > 0) {
                upgrade.setCost(newCost);
            }
            upgrade.setUpgradeLevel(upgrade.getUpgradeLevel() + 1);
            //System.out.println("Bought upgrade: " + name + " for " + (upgrade.getCost()-100));
        } else {
            System.out.println("Upgrade " + name + " is already at maximum level.");
        }
    }

    public void sellUpgrade(String name) {
        Upgrade upgrade = upgrades.get(name);
        if (upgrade != null && upgrade.getUpgradeLevel() > 0) {
            int decrement = upgrade.getCost() * 50 / 100;
            int newCost = upgrade.getCost() - decrement ;
            if (newCost > 0) {
                upgrade.setCost(newCost);
            }
            upgrade.setUpgradeLevel(upgrade.getUpgradeLevel() - 1);
            //System.out.println("Sold upgrade: " + name + " for " + (upgrade.getCost()+100));
        } else {
            System.out.println("No upgrade " + name + " to sell.");
        }
    }

    // Serialization
    public void saveToPrefs() {
        Gson gson = GsonFactory.createGson();
        String json = gson.toJson(this);

        Gdx.app.log("saveToPrefs", "SaveGame: " + getSaveGame("SaveGame"));

        getPrefs().putString("UpgradeManager", json);
        getPrefs().flush();
    }

    // Deserialization
    public boolean loadFromPrefs() {
        String upgradeManagerJson = getPrefs().getString("UpgradeManager");

        Gson gson = GsonFactory.createGson();
        UpgradeManager upgradeManagerData = gson.fromJson(upgradeManagerJson, UpgradeManager.class);

        if (upgradeManagerData != null) {
            this.saveGames = upgradeManagerData.getSavedValues();
            this.upgrades = upgradeManagerData.getUpgrades();

            Gdx.app.log("LoadFromPrefs", "SaveGame: " + getSaveGame("SaveGame"));

            return true;
        } else {
            Gdx.app.log("LoadFromPrefs", "Failed to load data from LibGDX Preferences.");
            return false;
        }
    }

    protected Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences("upgrades");
        return preferences;
    }
}


class SaveGame {
    private int playerScore;
    private int playerHealth;
    private int level;
    private float volume;

    public SaveGame(int playerScore, int playerHealth, int level, float volume) {
        this.playerScore = playerScore;
        this.playerHealth = playerHealth;
        this.level = level;
        this.volume = volume;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getLevel() {
        return level;
    }

    public float getVolume() {
        return volume;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "SaveGame{" +
                "playerScore=" + playerScore +
                ", playerHealth=" + playerHealth +
                ", level=" + level +
                ", volume=" + volume +
                '}';
    }
}


class Upgrade {
    private int cost;
    private float minUpgrade;
    private float maxUpgrade;
    private float actualValue;
    private int ticks;
    private int upgradeLevel;

    public Upgrade(int cost, float minUpgrade, float maxUpgrade, float actualValue, int ticks, int upgradeLevel) {
        this.cost = cost;
        this.minUpgrade = minUpgrade;
        this.maxUpgrade = maxUpgrade;
        this.actualValue = actualValue;
        this.ticks = ticks;
        this.upgradeLevel = upgradeLevel;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public float getMinUpgrade() {
        return minUpgrade;
    }

    public void setMinUpgrade(float minUpgrade) {
        this.minUpgrade = minUpgrade;
    }

    public void setMaxUpgrade(float maxUpgrade) {
        this.maxUpgrade = maxUpgrade;
    }

    public float getActualValue() {
        return actualValue;
    }

    public void setActualValue(float actualValue) {
        this.actualValue = actualValue;
    }

    public float getMaxUpgrade() {
        return maxUpgrade;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(int upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

}
