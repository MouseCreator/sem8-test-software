package mouse.univ;

public interface UserInput {
    int getInteger(String prompt);
    int getRangedInteger(String prompt, int range);
    int getInteger();
}
