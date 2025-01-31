package mouse.univ.app;

public interface UserIO {
    String getString();
    String getString(String prompt);
    void print(String message);
    void println(String message);
}
