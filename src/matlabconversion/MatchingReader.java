package matlabconversion;

import frechet.Matching;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by max on 28-4-14.
 */
public class MatchingReader {
    public static Matching readMatching(String filePath) {
        try {
            InputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (Matching) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Matching matching = readMatching("/tmp/foo.tmp");
        System.out.println(matching);
        System.out.println(matching.getTrajectory1()[0][1]);
    }
}
