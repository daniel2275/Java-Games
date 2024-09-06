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
        this.preferences = Gdx.app.getPreferences("upgrades");
    }

    public SaveGame getSaveGame(String name) {
        return saveGames.get(name);
    }

    public void addOrUpdateSaveGame(String name, int playerScore, int playerHealth, int level, float volume) {
        saveGames.put(name, new SaveGame(playerScore, playerHealth, level, volume));
    }

    public Upgrade getUpgrade(String name) {
        return upgrades.get(name);
    }

    public void addOrUpdateUpgrade(String name, int cost, float minUpgrade, float maxUpgrade, float actualValue, int ticks, int upgradeLevel) {
        upgrades.put(name, new Upgrade(cost, minUpgrade, maxUpgrade, actualValue, ticks, upgradeLevel));
    }

    public void buyUpgrade(String name) {
        Upgrade upgrade = upgrades.get(name);
        if (upgrade != null && upgrade.getUpgradeLevel() < upgrade.getTicks()) {
            upgrade.incrementLevel();
        } else {
            System.out.println("Upgrade " + name + " is already at maximum level.");
        }
    }

    public void sellUpgrade(String name) {
        Upgrade upgrade = upgrades.get(name);
        if (upgrade != null && upgrade.getUpgradeLevel() > 0) {
            upgrade.decrementLevel();
        } else {
            System.out.println("No upgrade " + name + " to sell.");
        }
    }

    // Serialization
    public void saveToPrefs() {
        Gson gson = GsonFactory.createGson();
        String json = gson.toJson(this);

        Gdx.app.log("saveToPrefs", "SaveGame: " + getSaveGame("SaveGame"));
        preferences.putString("UpgradeManager", json);
        preferences.flush();
    }

    // Deserialization
    public boolean loadFromPrefs() {
        String upgradeManagerJson = preferences.getString("UpgradeManager", null);
        if (upgradeManagerJson == null) {
            Gdx.app.log("UpgradeManager", "No data found in Preferences.");
            return false;
        }

        Gson gson = GsonFactory.createGson();
        UpgradeManager upgradeManagerData = gson.fromJson(upgradeManagerJson, UpgradeManager.class);

        if (upgradeManagerData != null) {
            this.saveGames = upgradeManagerData.saveGames;
            this.upgrades = upgradeManagerData.upgrades;
            Gdx.app.log("LoadFromPrefs", "SaveGame: " + getSaveGame("SaveGame"));
            return true;
        } else {
            Gdx.app.log("LoadFromPrefs", "Failed to load data from LibGDX Preferences.");
            return false;
        }
    }
}

class SaveGame {
    private final int playerScore;
    private final int playerHealth;
    private final int level;
    private final float volume;

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
}


class Upgrade {
    private int cost;
    private final float minUpgrade;
    private final float maxUpgrade;
    private float actualValue;
    private final int ticks;
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

    public float getMinUpgrade() {
        return minUpgrade;
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

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void incrementLevel() {
        this.upgradeLevel++;
        this.cost += 5 * upgradeLevel;
    }

    public void decrementLevel() {
        this.cost -= 5 * upgradeLevel;
        this.upgradeLevel--;
    }

}
