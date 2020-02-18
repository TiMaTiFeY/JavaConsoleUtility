package split;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;

public class Split {

    @Option(name = "-d", usage = "Set this if output files should have named ofile1, ofile2, ...")
    private String namingFilesType;

    @Option(name = "-l", metaVar = "Num", usage = "Set size output files at strings")
    private int maxLenStrings;

    @Option(name = "-c", metaVar = "Num", usage = "Set size output files at chars", forbids = {"-l"})
    private int maxLenChars;

    @Option(name = "-n", metaVar = "Num", usage = "Set count of output files", forbids = {"-l", "-c"})
    private int maxCountFiles;

    @Option(name = "-o", metaVar = "Num", usage = "Set default name of output file")
    private String ofile;

    @Argument(required = true, metaVar = "InputName", usage = "Input file name")
    private String inputFileName;

    public static void main(String[] args) {
        new Split().launch(args);
    }
    
    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar split.jar [-d] [-l num|-c num|-n num] [-o ofile] file");
            parser.printUsage(System.err);
            return;
        }

    }

}
