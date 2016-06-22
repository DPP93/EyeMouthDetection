/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.oi.testing.recent;

/**
 *
 * @author Daniel
 */
public class ConfusionMatrix {
    private double truePositives = 0;
    private double trueNegatives = 0;
    private double falsePositives = 0;
    private double falseNegatives = 0;

    public void incrementTP(){
        ++truePositives;
    }
    
    public void incrementTN(){
        ++trueNegatives;
    }
    
    public void incrementFP(){
        ++falsePositives;
    }
    
    public void incrementFN(){
        ++falseNegatives;
    }
    
    public double getTruePositives() {
        return truePositives;
    }

    public void setTruePositives(double truePositives) {
        this.truePositives = truePositives;
    }

    public double getTrueNegatives() {
        return trueNegatives;
    }

    public void setTrueNegatives(double trueNegatives) {
        this.trueNegatives = trueNegatives;
    }

    public double getFalsePositives() {
        return falsePositives;
    }

    public void setFalsePositives(double falsePositives) {
        this.falsePositives = falsePositives;
    }

    public double getFalseNegatives() {
        return falseNegatives;
    }

    public void setFalseNegatives(double falseNegatives) {
        this.falseNegatives = falseNegatives;
    }
    
    public double computePrecision(){
        return truePositives / (truePositives + falsePositives);
    }
    
    public double computeSensitivity(){
        return truePositives / (truePositives + falseNegatives);
    }
    
    public double computeSpecifity(){
        return trueNegatives / (trueNegatives + falsePositives);
    }
    
    public double computeFalseNegativeRate(){
        return 1.0 - computeSensitivity();
    }
    
    public double computeFalseDiscoveryRate(){
        return 1.0 - computePrecision();
    }
    
    public double computeAccuracy(){
        return (trueNegatives + trueNegatives) / (trueNegatives + truePositives + falseNegatives + falsePositives);
    }

    public String computeAllElements(){
        if (falseNegatives == 0 && falsePositives == 0 && trueNegatives == 0 && truePositives == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("True positives: "+ truePositives + "\n");
        sb.append("True negatives: "+ trueNegatives + "\n");
        sb.append("False positives: "+ falsePositives + "\n");
        sb.append("False negatives: "+ falseNegatives + "\n");
        sb.append("Precision: "+computePrecision() + "\n");
        sb.append("Sensitivity: "+computeSensitivity() + "\n");
        sb.append("Specificity: "+computeSpecifity()+ "\n");
        sb.append("Accuracy: "+computeAccuracy() + "\n");
        sb.append("False Negative Rate: "+computeFalseNegativeRate()+ "\n");
        sb.append("False Discovery Rate: "+computeFalseDiscoveryRate()+ "\n");

        return sb.toString();
    }
}
