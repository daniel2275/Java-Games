package UI.upgrades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

public class GsonFactory {

    public static Gson createGson() {
        return new GsonBuilder()
            .registerTypeAdapter(File.class, new FileTypeAdapter())
            .create();
    }
}

