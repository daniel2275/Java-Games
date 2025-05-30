package UI.upgrades;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UpgradeManager implements Serializable {
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
        SaveGame save = saveGames.get(name);
        if (save == null) {
            Gdx.app.log("getSaveGame", "No save found for: " + name);
        }
        return save;
    }

//    public void addOrUpdateSaveGame(String name, int playerScore, int playerHealth,  float volume) {
//        saveGames.put(name, new SaveGame(playerScore, playerHealth,  volume));
//    }

    public void addOrUpdateSaveGame(String name, int playerScore, int playerHealth, float volume) {
        SaveGame existingSave = saveGames.get(name);

        if (existingSave != null) {
            // Update existing save instead of replacing it
            existingSave.updateSave(playerScore, playerHealth, volume);
        } else {
            // Create a new save if one doesn't exist
            saveGames.put(name, new SaveGame(playerScore, playerHealth, volume));
        }
    }

    // Advance level in a specific stage
    public void advanceLevel(String saveName, int stageId) {
        SaveGame save = saveGames.get(saveName);
        if (save != null) {
            int newLevel = save.getStageLevel(stageId) + 1;
            save.setStageLevel(stageId, newLevel);
            Gdx.app.log("Advance Level", "Stage: " + stageId + ", Level: " + newLevel);
            saveToPrefs();
        }
    }


    public void setStageLevel(String saveName, int stageId, int level) {
        SaveGame save = saveGames.get(saveName);
        if (save != null) {
            save.setStageLevel(stageId, level);
            Gdx.app.log("Set Stage Level", "Stage " + stageId + " set to Level " + level);
            saveToPrefs();
        } else {
            Gdx.app.log("Set Stage Level", "Save not found: " + saveName);
        }
    }


    // Get the highest level reached in a stage
    public int getStageLevel(String saveName, int stageId) {
        SaveGame save = saveGames.get(saveName);
        return (save != null) ? save.getStageLevel(stageId) : 1;
    }

    public Map<Integer, Integer> getAllStageLevels(String saveName) {
        SaveGame save = saveGames.get(saveName);
        return (save != null) ? save.getStageLevels() : new HashMap<>();
    }

    public void resetAllStages(String saveName) {
        SaveGame save = saveGames.get(saveName);
        if (save != null) {
            for (Integer stageId : save.getStageLevels().keySet()) {
                save.resetStageLevel(stageId);
            }
            Gdx.app.log("Reset All Stages", "All stages reset to Level 1 for save: " + saveName);
            saveToPrefs();
        } else {
            Gdx.app.log("Reset All Stages", "Save not found: " + saveName);
        }
    }

    public void resetStage(String saveName, int stageId) {
        SaveGame save = saveGames.get(saveName);
        if (save != null) {
            save.resetStageLevel(stageId);
            Gdx.app.log("Reset Stage", "Stage " + stageId + " reset to Level 1");
        }
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

class SaveGame implements Serializable {
    private static final long serialVersionUID = 1L;
    private int playerScore;
    private int playerHealth;
    private float volume;
    private Map<Integer, Integer> stageLevels;


    public SaveGame(int playerScore, int playerHealth, float volume) {
        this.playerScore = playerScore;
        this.playerHealth = playerHealth;
        this.volume = volume;
        this.stageLevels = new HashMap<>();

        // Initialize all stages to level 1
        for (int i = 1; i <= 4; i++) {
            this.stageLevels.put(i , 0);
        }
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public float getVolume() {
        return volume;
    }

    public int getStageLevel(int stageId) {
        return stageLevels.getOrDefault(stageId, 1);
    }

//    public void setStageLevel(int stageId, int level) {
//        stageLevels.put(stageId, Math.max(stageLevels.getOrDefault(stageId, 1), level));
//    }

    public void setStageLevel(int stageId, int level) {
        int currentLevel = stageLevels.getOrDefault(stageId, 1);
        if (level > currentLevel) {
            stageLevels.put(stageId, level);
        }
    }

    public Map<Integer, Integer> getStageLevels() {
        return stageLevels;
    }

    public void resetStageLevel(int stageId) {
        stageLevels.put(stageId, 0);
    }

    public void updateSave(int playerScore, int playerHealth, float volume) {
        this.playerScore = playerScore;
        this.playerHealth = playerHealth;
        this.volume = volume;
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
