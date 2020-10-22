import java.util.HashMap;
import java.io.InputStream;
import java.io.FileNotFoundException;

//import javafx.scene.image.Image;
public class Image extends javafx.scene.image.Image {
    private static String[] imagePaths;
    private static int counter = 0, numImgs = 0;
    private static HashMap<String, Integer> imgId;
    private static boolean isLoaded = false;

    public Image(String fileName) {
        super(getPath(fileName));
    }

    // mostly likely to be null since paths are wrong so will increment instead
    public Image(InputStream is) {
        super(getPath("" + counter));
    }

    public static void loadImages() {
        if (!isLoaded) {
            isLoaded = true;
            counter = 0;
            String folderPath = File.find("images");
            File[] imageFiles = File.create(folderPath).listFiles();
            numImgs = imageFiles.length;
            imagePaths = new String[numImgs];
            for (int i = 0; i < numImgs; i++) {
                imagePaths[i] = folderPath + "/" + imageFiles[i].getName();
            }
            imgId = new HashMap<>();
        }
    }

    public static String getPath(String key) {
        loadImages();
        if (imgId.get(key) == null)
            imgId.put(key, counter = (counter + 1) % numImgs);
        return "file:" + imagePaths[imgId.get(key)];
    }

}