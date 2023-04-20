package gamestates;

import java.io.Serializable;

public class Upgrade  implements Serializable {
    private String name;
    private float cost;
    private float costIncrements;
    private float minUpg;
    private float maxUpg;
    private int upgTicks;
    private int level;
    private static final long serialVersionUID = 1L;

    public Upgrade() {
        level = 0;
    }

    public Upgrade(String name, float cost, float costIncrements, float minUpg,float maxUpg, int ticks, int level) {
        this.name = name;
        this.cost = cost;
        this.costIncrements = costIncrements;
        this.minUpg = minUpg;
        this.maxUpg = maxUpg;
        this.upgTicks = ticks;
        this.level = level;
    }

    public void reset(String name, float cost, float costIncrements, float minUpg,float maxUpg, int ticks, int level) {
        this.name = name;
        this.cost = cost;
        this.costIncrements = costIncrements;
        this.minUpg = minUpg;
        this.maxUpg = maxUpg;
        this.upgTicks = ticks;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getCostIncrements() {
        return costIncrements;
    }

    public void setCostIncrements(float costIncrements) {
        this.costIncrements = costIncrements;
    }

    public float getMinUpg() {
        return minUpg;
    }

    public void setMinUpg(float minUpg) {
        this.minUpg = minUpg;
    }

    public float getMaxUpg() {
        return maxUpg;
    }

    public void setMaxUpg(float maxUpg) {
        this.maxUpg = maxUpg;
    }

    public int getUpgTicks() {
        return upgTicks;
    }

    public void setUpgTicks(int upgTicks) {
        this.upgTicks = upgTicks;
    }

    public void upgrade() {
        this.level++;
        this.cost += this.costIncrements;
    }

    public void downgrade() {
        this.level--;
        this.cost -= this.costIncrements;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

//    @Override
//    public String toString() {
//        System.out.println("name:" + this.name +
//        "\ncost: " + this.cost +
//        "\ncostIncrements:" + this.costIncrements +
//        "\nminUpg:" + this.minUpg +
//        "\nmaxUpg:" + this.maxUpg +
//        "\nupgTicks:" + this.upgTicks +
//        "\nlevel: " + this.level );
//        return super.toString();
//    }
}
