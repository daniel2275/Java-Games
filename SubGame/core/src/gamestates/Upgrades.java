//package gamestates;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.InputListener;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.utils.Align;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static utilz.Constants.Game.WORLD_HEIGHT;
//import static utilz.Constants.Game.WORLD_WIDTH;
//
//public class Upgrades implements Screen, InputProcessor {
//
//private Table table = new Table();;
//private Stage stage;
//
//private Label scoreLbl;
//
//private Playing playing;
//
//private int playerScore;
//private Map<String,Map> properties = new HashMap<>();
//
//
//private BitmapFont font = loadFont("fonts/SmallTypeWriting.ttf");
//    public Upgrades(Playing playing) {
//        this.playing = playing;
//        loadInitial();
//        show();
//    }
//
//    private void loadInitial() {
//
//        Map<String, Float>[] savedStateMap = new HashMap[3];
//
//        savedStateMap[0] =  new HashMap<>();
//        savedStateMap[0].put("Score", 40f);
//        properties.put("Score", savedStateMap[0]);
//
//        savedStateMap[1] =  new HashMap<>();
//        savedStateMap[1].put("BaseCost", 10f);
//        savedStateMap[1].put("CostIncrements", 2f);
//        savedStateMap[1].put("MaxUpg", 2f);
//        savedStateMap[1].put("Ticks", 40f);
//        properties.put("Speed", savedStateMap[1]);
//
//
//        savedStateMap[2] =  new HashMap<>();
//        savedStateMap[2].put("BaseCost", 10f);
//        savedStateMap[2].put("CostIncrements", 4f);
//        savedStateMap[2].put("MaxUpg", -3f);
//        savedStateMap[2].put("Ticks", 40f);
//        properties.put("FireRate", savedStateMap[2]);
//
//        System.out.println("Speed baseCost :" + properties.get("Speed").get("BaseCost"));
//        System.out.println("FireRate baseCost :" + properties.get("FireRate").get("BaseCost"));
//
//    }
//
//    @Override
//    public void show() {
//        stage = new Stage();
//        table = new Table();
//
//        table.setFillParent(true);
//        table.defaults().align(Align.left).pad(15);
//
//        // create a skin object
//        Skin skin = new Skin(Gdx.files.internal("assets/clean-crispy/skin/clean-crispy-ui.json"));
//
//        Label speedCost = new Label("00", skin);
//        Label fireRateCost = new Label("00", skin);
//
//        Window window =  new Window("Upgrades", skin);
//        table.align(Align.left);
//        Label titleLabel = window.getTitleLabel();
//        titleLabel.setAlignment(Align.center);
//
//        window.align(Align.center);
//
//        window.add(table);
//        window.setWidth(WORLD_WIDTH);
//        window.setHeight(WORLD_HEIGHT - 25);
//
//
//        table.setSkin(skin);
//        stage.addActor(window);
//
//        scoreLbl = new Label("(Score:)" + playing.getPlayer().getPlayerScore(), new Label.LabelStyle(font, Color.WHITE));
//        scoreLbl.setPosition(5,WORLD_HEIGHT - 25);
//        stage.addActor(scoreLbl);
//
//        TextButton playerSpeedUpBtn = new TextButton("(Sub Speed + )", skin);
//        TextButton playerSpeedDownBtn = new TextButton("(Sub Speed -)", skin);
//
//        TextButton playerFireRateUpBtn = new TextButton("(Sub FireRate + )", skin);
//        TextButton playerFireRateDownBtn = new TextButton("(Sub FireRate -)", skin);
//
//
//        ProgressBar playerSpeedDisplay = new ProgressBar(2,100, 1,false, skin);
//        float percent = (playing.getPlayer().getPlayerSpeed() * 100) / 2;
//        playerSpeedDisplay.setValue(percent);
//
//        ProgressBar playerFireRateDisplay = new ProgressBar(2,100, 1,false, skin);
//        float percentFireRate = (playing.getPlayer().getReloadSpeed() + (float) (properties.get("FireRate").get("MaxUpg")) * 100) / 2;
//        playerFireRateDisplay.setValue(percentFireRate);
//
//        TextButton exitBtn = new TextButton("Exit", skin);
//
//        table.add(playerSpeedUpBtn);
//        table.add(playerSpeedDownBtn);;
//        table.add(speedCost);
//        table.add(playerSpeedDisplay);
//        table.row();
//        table.add(playerFireRateUpBtn);
//        table.add(playerFireRateDownBtn);
//        table.add(fireRateCost);
//        table.add(playerFireRateDisplay);
//        table.add(exitBtn);
//
//        String property = "Speed";
//        addListeners(playerSpeedUpBtn, property, 1, speedCost, playerSpeedDisplay);
//        addListeners(playerSpeedDownBtn, property, 2, speedCost, playerSpeedDisplay);
//
//        property = "FireRate";
//        addListeners(playerFireRateUpBtn, property, 3, fireRateCost, playerFireRateDisplay);
//        addListeners(playerFireRateDownBtn, property, 4, fireRateCost, playerFireRateDisplay);
//
//
//
//        exitBtn.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
////                System.out.println("Item3 was clicked.");
//                Gamestate.state = Gamestate.PLAYING;
//            }
//        });
//
//        stage.addListener(new InputListener() {
//            @Override
//            public boolean keyDown(InputEvent event, int keycode) {
//                switch ( keycode ) {
//                    case Input.Keys.ESCAPE : {
//                        Gamestate.state = Gamestate.PLAYING;
//                    }
//                    break;
//                }
//                return super.keyDown(event, keycode);
//            }
//        });
//
//        speedCost.setText(" "  + (float) (properties.get("Speed").get("BaseCost")));
//        fireRateCost.setText(" "  + (float) (properties.get("FireRate").get("BaseCost")));
//    }
//
//    public void render(float delta) {
//        scoreLbl.setText("(Score:)" + playing.getPlayer().getPlayerScore());
//        Gdx.gl.glClearColor(1, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        stage.act(delta);
//        stage.draw();
//        Gdx.input.setInputProcessor(stage);
//    }
//
//    @Override
//    public void resize(int i, int i1) {
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//
//    }
//
//    @Override
//    public void dispose() {
//    }
//
//
//    private BitmapFont loadFont(String fontName){
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 16; // font size
//        BitmapFont font = generator.generateFont(parameter); // generate the BitmapFont
//        generator.dispose(); // dispose the generator when you're done
//        return font;
//    }
//
//    @Override
//    public boolean keyDown(int i) {
//        return false;
//    }
//
//    @Override
//    public boolean keyUp(int i) {
//        return false;
//    }
//
//    @Override
//    public boolean keyTyped(char c) {
//        return false;
//    }
//
//    @Override
//    public boolean touchDown(int i, int i1, int i2, int i3) {
//        return false;
//    }
//
//    @Override
//    public boolean touchUp(int i, int i1, int i2, int i3) {
//        return false;
//    }
//
//    @Override
//    public boolean touchDragged(int i, int i1, int i2) {
//        return false;
//    }
//
//    @Override
//    public boolean mouseMoved(int i, int i1) {
//        return false;
//    }
//
//    @Override
//    public boolean scrolled(float v, float v1) {
//        return false;
//    }
//
//
//    private void addListeners(final TextButton textButton,final String property, final int behavior, final Label value, final ProgressBar outputLbl) {
//        textButton.addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                labelBehavior(property, behavior, value, outputLbl);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//    }
//
//    private void labelBehavior(String property, int behavior, Label value, ProgressBar outputLbl) {
//        float costIncrements = (float) properties.get(property).get("CostIncrements");
//        float maxUpg = (float) properties.get(property).get("MaxUpg");
//        float upgTicks = (float) properties.get(property).get("Ticks");
//
//        float upgAmount = (Math.abs(maxUpg) / upgTicks);
//
//        switch ( behavior ) {
//            case 1: {
//                int playerScore = playing.getPlayer().getPlayerScore();
//
//                float baseCost = (float) properties.get(property).get("BaseCost");
//
//                if (playerScore - baseCost >= 0) {
//                    double speed = playing.getPlayer().getPlayerSpeed();
//                    if (speed < maxUpg) {
//                        double newSpeed = speed + upgAmount;
//                        playing.getPlayer().setPlayerSpeed((float) newSpeed);
//                        scoreLbl.setText("(Score:)" + playing.getPlayer().getPlayerScore());
//                        float percent = (playing.getPlayer().getPlayerSpeed() * 100) / maxUpg;
//                        outputLbl.setValue(percent);
//
//                        playerScore -= ((float) (properties.get(property).get("BaseCost")));
//
//                        baseCost = (float) properties.get(property).get("BaseCost") + (float) properties.get(property).get("CostIncrements");
//                        value.setText(" " + baseCost);
//
//                        Map<String, Float> state;
//                        state = properties.get(property);
//                        state.put("BaseCost", baseCost);
//
//                        playing.getPlayer().setPlayerScore(playerScore);
//                    }
//                }
//                break;
//            } case 2: {
//                int playerScore = playing.getPlayer().getPlayerScore();
//
//                float baseCost = (float) properties.get(property).get("BaseCost");
//
//                if (this.playerScore >= playerScore - baseCost) {
//                    double speed = playing.getPlayer().getPlayerSpeed();
//                    if (speed > 0) {
//                        double newSpeed = speed - upgAmount;
//                        playing.getPlayer().setPlayerSpeed((float) newSpeed);
//                        float percent = (playing.getPlayer().getPlayerSpeed() * 100) / maxUpg;
//                        outputLbl.setValue(percent);
//
//                        playerScore += ((float) (properties.get(property).get("BaseCost"))- costIncrements);
//                        playing.getPlayer().setPlayerScore(playerScore);
//
//                        baseCost = (float) properties.get(property).get("BaseCost") - (float) properties.get(property).get("CostIncrements");
//                        value.setText(" " + baseCost);
//
//                        Map<String, Float> state;
//                        state = properties.get(property);
//                        state.put("BaseCost", baseCost);
//                    }
//                }
//                break;
//            }
//            case 3: {
//                System.out.println("3");
//                int playerScore = playing.getPlayer().getPlayerScore();
//
//                float baseCost = (float) properties.get(property).get("BaseCost");
//
//                if (playerScore - baseCost >= 0) {
//                    float speed = playing.getPlayer().getReloadSpeed();
//                    System.out.println("Speed" + speed);
//                    if (speed > 0) {
//                        float newSpeed = speed - upgAmount;
//                        playing.getPlayer().setReloadSpeed(newSpeed);
//                        System.out.println("reload" + playing.getPlayer().getReloadSpeed());
//                        scoreLbl.setText("(Score:)" + playing.getPlayer().getPlayerScore());
//                        float percent = (Math.abs((playing.getPlayer().getReloadSpeed()) + maxUpg) * 100) / Math.abs(maxUpg);
//                        outputLbl.setValue(percent);
//
//                        playerScore -= ((float) (properties.get(property).get("BaseCost")));
//
//                        baseCost = (float) properties.get(property).get("BaseCost") + (float) properties.get(property).get("CostIncrements");
//                        value.setText(" " + baseCost);
//
//                        Map<String, Float> state;
//                        state = properties.get(property);
//                        state.put("BaseCost", baseCost);
//
//                        playing.getPlayer().setPlayerScore(playerScore);
//                    }
//                }
//                break;
//            }
//            case 4: {
//                System.out.println("4");
//                int playerScore = playing.getPlayer().getPlayerScore();
//
//                float baseCost = (float) properties.get(property).get("BaseCost");
//
//                if (this.playerScore >= playerScore - baseCost) {
//                    float speed = playing.getPlayer().getReloadSpeed();
//
//                    if (speed < Math.abs(maxUpg)) {
//                        float newSpeed = speed + upgAmount;
//                        System.out.println("reload" + newSpeed);
//                        playing.getPlayer().setReloadSpeed(newSpeed);
//                        float percent = (Math.abs((playing.getPlayer().getReloadSpeed()) + maxUpg) * 100) / Math.abs(maxUpg);
//                        outputLbl.setValue(percent);
//
//                        playerScore += ((float) (properties.get(property).get("BaseCost")) - costIncrements);
//                        playing.getPlayer().setPlayerScore(playerScore);
//
//                        baseCost = (float) properties.get(property).get("BaseCost") - (float) properties.get(property).get("CostIncrements");
//                        value.setText(" " + baseCost);
//
//                        Map<String, Float> state;
//                        state = properties.get(property);
//                        state.put("BaseCost", baseCost);
//                    }
//                }
//                break;
//            }
//        }
//    }
//
//
//    public int getPlayerScore() {
//        return playerScore;
//    }
//
//    public void setPlayerScore(int playerScore) {
//        this.playerScore = playerScore;
//    }
//
//}
//
//
////    To save a Map object to a file in LibGDX, you can use the Json class, which is part of the LibGDX utilities. The Json class provides methods for serializing and deserializing Java objects to and from JSON format. Here's an example:
////
////        java
////        Copy code
////        import com.badlogic.gdx.Gdx;
////        import com.badlogic.gdx.files.FileHandle;
////        import com.badlogic.gdx.utils.Json;
////        import java.util.HashMap;
////        import java.util.Map;
////
////public class MapToFileExample {
////    public static void main(String[] args) {
////        // Creating a Map object
////        Map<String, Integer> map = new HashMap<>();
////        map.put("John", 25);
////        map.put("Mary", 30);
////        map.put("Bob", 40);
////        map.put("Alice", 35);
////
////        // Saving the map to a file
////        Json json = new Json();
////        String jsonStr = json.toJson(map);
////        FileHandle file = Gdx.files.local("map.json");
////        file.writeString(jsonStr, false);
////        System.out.println("Map saved to file.");
////    }
////}
////In this example, we create a HashMap object with some key-value pairs. We then create a Json object and use its toJson() method to convert the Map object to a JSON string. We create a FileHandle object with the path and filename for the output file, and we use its writeString() method to write the JSON string to the file. Finally, we print a message to confirm that the map has been saved to the file.
////
////        The Gdx.files.local() method creates a file handle for a file in the local storage of the application. You can use other file handles depending on your requirements.
////
////        To load the Map object from the file, you can use the fromJson() method of the Json class. Here's an example:
////
////        java
////        Copy code
////        import com.badlogic.gdx.Gdx;
////        import com.badlogic.gdx.files.FileHandle;
////        import com.badlogic.gdx.utils.Json;
////        import java.util.Map;
////
////public class MapFromFileExample {
////    public static void main(String[] args) {
////        // Loading the map from a file
////        FileHandle file = Gdx.files.local("map.json");
////        String jsonStr = file.readString();
////        Json json = new Json();
////        Map<String, Integer> map = json.fromJson(HashMap.class, jsonStr);
////        System.out.println("Map loaded from file: " + map);
////    }
////}
////In this example, we create a FileHandle object for the input file, and we use its readString() method to read the contents of the file as a string. We create a Json object and use its fromJson() method to convert the JSON string to a Map object. Finally, we print a message to confirm that the map has been loaded from the file.
////
////        Note that the Json class can serialize and deserialize a wide range of Java objects, not just Map objects. You can use it to save and load other types of data as well.
//
//
