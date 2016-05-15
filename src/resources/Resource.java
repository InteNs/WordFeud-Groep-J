package resources;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class Resource {

    private HashMap<String, Image> images;

    public Resource() {
        images = new HashMap<>();
    }



    public Image getImage(String name) {
        if(!images.keySet().contains(name))
            try {
                File file = new File(getClass().getResource(name).getFile());
                images.put(name, new Image(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        return images.get(name);
    }
}
