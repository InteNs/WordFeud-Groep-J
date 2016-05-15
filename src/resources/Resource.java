package resources;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class Resource {

    public Resource() {initialize();}

    private HashMap<String, Image> images = new HashMap<>();

    public Image getImage(String name) {
        return images.get(name);
    }

    public void initialize() {
        File dir = new File("src/resources");
        File[] directoryListing = dir.listFiles();
        if(directoryListing!=null)
        {
            for (File child : directoryListing) {
                String name = child.getName();
                if(name.endsWith(".png")) {
                    try {
                        images.put(name, new Image(new FileInputStream(child), 80, 80, true, true));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
