import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class run {
    private NFA constructNFAFromInput(Scanner scanner) {
        NFA nfa = new NFA(scanner.nextLine());

        int stateAmount = scanner.nextInt();
        int acceptStateAmount = scanner.nextInt();
        int transitionAmount = scanner.nextInt();

        List<Integer> acceptStateIndices = new ArrayList<>();

        while (acceptStateAmount-- > 0) {
            acceptStateIndices.add(scanner.nextInt());
        }

        for (int stateIndex = 0; stateIndex < stateAmount; stateIndex++) {
            nfa.addState(new State(stateIndex, stateIndex == 0, acceptStateIndices.contains(stateIndex)));
        }

        for (int stateIndex = 0; stateIndex < stateAmount; stateIndex++) {
            int stateTransitionAmount = scanner.nextInt();

            while (stateTransitionAmount-- > 0) {
                char symbol = scanner.next().charAt(0);
                int to = scanner.nextInt();
                Transition transition = new Transition(nfa.states.get(stateIndex), nfa.states.get(to), symbol);
                nfa.states.get(stateIndex).addTransition(transition);
            }
        }

        return nfa;
    }

    private void testAll() {
        List<String> outputs = new ArrayList<>();
        String inputsPath = "/Users/hp/Documents/Theoretical Informatics/assignment1/Public tests/P2/Out (Public)";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputsPath))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    Scanner scanner = new Scanner(path);
                    outputs.add(scanner.nextLine());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String inputsPaths = "/Users/hp/Documents/Theoretical Informatics/ass1/Public tests/P2/In (Public)";

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputsPaths))) {
            int cnt = 0;

            for (Path path : stream) {
                if (!Files.isDirectory(path) && path.toString().charAt(path.toString().length() - 7) == 'n') {
                    File inputPath = new File(path.toString());
                    Scanner scanner = new Scanner(inputPath);
                    System.out.print(constructNFAFromInput(scanner).simulate().equals(outputs.get(cnt++)) + " ");
                    scanner.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void singleInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(constructNFAFromInput(scanner).simulate());
    }

    public static void main(String[] args) {
        run run = new run();
        run.singleInput();
    }
}
