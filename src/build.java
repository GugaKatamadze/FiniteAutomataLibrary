import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class build {
    private NFA constructNFAFromInput(String input, Scanner scanner) {
        NFA nfa = new NFA(input);

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
        String inputsPath = "/Users/hp/Documents/Theoretical Informatics/assignment1/Public tests/reg";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputsPath))) {
            int file_counter = 10;
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    Scanner scanner = new Scanner(path);
                    String regex = scanner.nextLine();
                    String output = NFAOperations.createFromRegex(regex).toOutputForm();
                    String outputPath = "/Users/hp/Documents/Theoretical Informatics/assignment1/P1 Outputs/" + file_counter++;
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
                    writer.write(output);
                    writer.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> inputs = new ArrayList<>();
        List<String> outputs = new ArrayList<>();

        inputsPath = "/Users/hp/Documents/Theoretical Informatics/assignment1/Public tests/P2/In (Public)";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputsPath))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    Scanner scanner = new Scanner(path);
                    inputs.add(scanner.nextLine());
                    scanner = new Scanner(path);
                    outputs.add(constructNFAFromInput(scanner.nextLine(), scanner).simulate());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int fileCounter = 0;
        inputsPath = "/Users/hp/Documents/Theoretical Informatics/assignment1/P1 Outputs";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputsPath))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    Scanner scanner = new Scanner(path);
                    NFA nfa = constructNFAFromInput(inputs.get(fileCounter), scanner);
                    System.out.print(nfa.simulate().equals(outputs.get(fileCounter++)) + " ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void singleInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(NFAOperations.createFromRegex(scanner.nextLine()).toOutputForm());
    }

    public static void main(String[] args) {
        build build = new build();
        build.singleInput();
    }
}
