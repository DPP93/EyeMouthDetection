package pl.oi.method.annotations;

/**
 * Created by dpp on 5/4/16.
 */
public class Face {

    private String pictureName;
    private FacePoint leftEye;
    private FacePoint rightEye;
    private FacePoint leftMouthCorner;
    private FacePoint rightMouthCorner;

    public Face(String pictureName, FacePoint leftEye, FacePoint rightEye, FacePoint leftMouthCorner, FacePoint rightMouthCorner) {
        this.pictureName = pictureName;
        this.leftEye = leftEye;
        this.rightEye = rightEye;
        this.leftMouthCorner = leftMouthCorner;
        this.rightMouthCorner = rightMouthCorner;
    }

    public String getPictureName() {
        return pictureName;
    }

    public FacePoint getLeftEye() {
        return leftEye;
    }

    public FacePoint getRightEye() {
        return rightEye;
    }

    public FacePoint getLeftMouthCorner() {
        return leftMouthCorner;
    }

    public FacePoint getRightMouthCorner() {
        return rightMouthCorner;
    }
}
