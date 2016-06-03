package database;

import java.io.File;

/**
 * Created by dpp on 5/5/16.
 */
public class ImageFile {

    private File file;
    private String imageName;

    public ImageFile(File file) {
        this.file = file;
        imageName = file.getName();
//        System.out.println(imageName);
    }

    public File getFile() {
        return file;
    }

    public String getImageName() {
        return imageName;
    }
}
