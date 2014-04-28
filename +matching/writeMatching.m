function writeMatching(javaMatchingObject,fileName)
import java.io.*
fos = java.io.FileOutputStream(fileName);
oos = java.io.ObjectOutputStream(fos);
oos.writeObject(javaMatchingObject);
oos.close();
end
