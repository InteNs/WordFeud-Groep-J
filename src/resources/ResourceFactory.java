package resources;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class ResourceFactory {

    private HashMap<String, Image> imagesHighRes;
    private HashMap<String, Image> imagesLowRes;

    public ResourceFactory() {
        imagesHighRes = new HashMap<>();
        imagesLowRes = new HashMap<>();
    }

    public Image getImage(String name, Boolean highRes) {
        if(highRes) {
            if (!imagesHighRes.keySet().contains(name))
                loadImage(name, true);
            return imagesHighRes.get(name);
        }
        else {
            if(!imagesLowRes.keySet().contains(name))
                loadImage(name, false);
            return imagesLowRes.get(name);
        }
    }

    private void loadImage(String name, Boolean highRes) {
        try {
            File file = new File(getClass().getResource(name).getFile());
            if (highRes) imagesHighRes.put(name, new Image(new FileInputStream(file), 100, 100, true, true));
            if (!highRes) imagesLowRes.put(name, new Image(new FileInputStream(file), 100, 100, true, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}