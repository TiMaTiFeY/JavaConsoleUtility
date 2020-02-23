package split;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;

public class SplitLauncher {

    @Option(name = "-d", usage = "Set this if output files should have named ofile1, ofile2, ...")
    private boolean flagD;

    @Option(name = "-l", metaVar = "Num", usage = "Set size output files at strings")
    private long maxLenStrings;

    @Option(name = "-c", metaVar = "Num", usage = "Set size output files at chars", forbids = {"-l"})
    private long maxLenChars;

    @Option(name = "-n", metaVar = "Num", usage = "Set count of output files", forbids = {"-l", "-c"})
    private long maxCountFiles;

    @Option(name = "-o", metaVar = "Num", usage = "Set default name of output file")
    private String ofile;

    @Argument(required = true, metaVar = "InputName", usage = "Input file name")
    private String inputFileName;

    public static void main(String[] args) {
        new SplitLauncher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar JavaConsoleUtility.jar [-d] [-l num|-c num|-n num] [-o ofile] file");
            parser.printUsage(System.err);
            return;
        }

        if (ofile == null) ofile = "x";
            else if (ofile.equals("-")) ofile = inputFileName;

        Split splitter;
        if (maxLenStrings != 0)
            splitter = new Split(ofile, flagD, Split.TypeNum.Strings, maxLenStrings);
        else if (maxLenChars != 0)
            splitter = new Split(ofile, flagD, Split.TypeNum.Chars, maxLenChars);
        else if (maxCountFiles != 0)
            splitter = new Split(ofile, flagD, Split.TypeNum.Files, maxCountFiles);
        else
            splitter = new Split(ofile, flagD, Split.TypeNum.Strings, 100);

        try {
            int result = splitter.splitFile(inputFileName);
            System.out.println("Total of " + result + " files created");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
