package UI.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class UpgradeManager implements Serializable {
    private Map<String, SaveGame> saveGame;
    private Map<String, Upgrade> upgrades;
    private static final long serialVersionUID = 1L;

    public UpgradeManager() {
        this.saveGame = new HashMap<>();
        this.upgrades = new HashMap<>();
    }

    // SaveGame
    public Map<String, SaveGame> getSaveGameUpgrades() {
        return saveGame;
    }

    public void setSaveGame(Map<String, SaveGame> saveGame) {
        this.saveGame = saveGame;
    }

    public void addSaveGame(String name, int playerScore, int playerHealth, int level, float volume) {
        saveGame.put(name, new SaveGame(playerScore, playerHealth, level, volume));
    }

    public Map<String, SaveGame> getSavedValues() {
        return saveGame;
    }

    public SaveGame getSaveGame(String name) {
        return saveGame.get(name);
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

    public void resetUpgrades() {
        for (Upgrade upgrade : upgrades.values()) {
            upgrade.setUpgradeLevel(0);
            upgrade.setCost(10);
        }
        System.out.println("All upgrades have been reset.");
    }

    public void buyUpgrade(String name) {
        Upgrade upgrade = upgrades.get(name);
        if (upgrade != null && upgrade.getUpgradeLevel() < upgrade.getTicks()) {
            int newCost = upgrade.getCost() + 100 ;
            if (newCost > 0) {
                upgrade.setCost(newCost);
            }
            upgrade.setUpgradeLevel(upgrade.getUpgradeLevel() + 1);
            System.out.println("Bought upgrade: " + name + " for " + upgrade.getCost());
        } else {
            System.out.println("Upgrade " + name + " is already at maximum level.");
        }
    }

    public void sellUpgrade(String name) {
        Upgrade upgrade = upgrades.get(name);
        if (upgrade != null && upgrade.getUpgradeLevel() > 0) {
            int newCost = upgrade.getCost() - 100 ;
            if (newCost > 0) {
                upgrade.setCost(newCost);
            }
            upgrade.setUpgradeLevel(upgrade.getUpgradeLevel() - 1);
            System.out.println("Sold upgrade: " + name + " for " + upgrade.getCost());
        } else {
            System.out.println("No upgrade " + name + " to sell.");
        }
    }

    // Serialization
    public void saveToPrefs() {
        Preferences prefs = Gdx.app.getPreferences("upgrades");
        Gson gson = new Gson();
        String json = gson.toJson(this);
        prefs.putString("UpgradeManager", json);
        prefs.flush();
    }

    // Deserialization
    public boolean loadFromPrefs() {
        Preferences prefs = Gdx.app.getPreferences("Upgrades");
        String upgradeManagerJson = prefs.getString("UpgradeManager");

        Gson gson = new Gson();
        UpgradeManager upgradeManagerData = gson.fromJson(upgradeManagerJson, UpgradeManager.class);

        if (upgradeManagerData != null) {
            this.saveGame = upgradeManagerData.getSavedValues();
            this.upgrades = upgradeManagerData.getUpgrades();
            return true;
        } else {
            System.out.println("Failed to load data from LibGDX Preferences.");
            return false;
        }

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
