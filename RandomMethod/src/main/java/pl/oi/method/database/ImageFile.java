package pl.oi.method.database;

import java.io.File;

/**
 * Created by dpp on 5/5/16.
 */
public class ImageFile {

    private File file;
    private String imageName;

    public ImageFile(File file) {
        this.file = file;
//        System.out.println(file.getParentFile().getName());
        imageName = file.getParentFile().getName()+"/"+file.getName();
    }

    public File getFile() {
        return file;
    }

    public String getImageName() {
        return imageName;
    }
}
