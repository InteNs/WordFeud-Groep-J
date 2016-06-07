package resources;

import javafx.scene.image.Image;

import java.io.InputStream;
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
        InputStream inputStream = getClass().getResourceAsStream(name);
        if (highRes) imagesHighRes.put(name, new Image(inputStream, 100, 100, true, true));
        if (!highRes) imagesLowRes.put(name, new Image(inputStream, 100, 100, true, true));
    }
}