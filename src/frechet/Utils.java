package frechet;

public class Utils {
    public PrintStream bugs;
    public PrintStream progress;

    public static void check(boolean statement) {
        check(statement, "");
    }

    public static void check(boolean statement, String msg) {
        if (!statement) {
            throw new AssertionError(msg); 
        }
    }

///    public static void debug(Str

}
