package split;

import org.junit.jupiter.api.Test;
import static org.apache.commons.io.FileUtils.contentEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

public class SplitTest {
    @Test
    public void splitFile() throws IOException {
        Split splitter;

        splitter = new Split("out_test1", true, Split.TypeNum.Files, 2);
        splitter.splitFile("testResources/in1.txt");
        assertTrue(contentEquals(
                new File("testResources/out11.txt"),
                new File("out_test11")
        ));
        assertTrue(contentEquals(
                new File("testResources/out12.txt"),
                new File("out_test12")
        ));

        splitter = new Split("out_test2", true, Split.TypeNum.Strings, 1);
        splitter.splitFile("testResources/in2.txt");
        assertTrue(contentEquals(
                new File("testResources/out21.txt"),
                new File("out_test21")
        ));
        assertTrue(contentEquals(
                new File("testResources/out22.txt"),
                new File("out_test22")
        ));
        assertTrue(contentEquals(
                new File("testResources/out23.txt"),
                new File("out_test23")
        ));

        splitter = new Split("out_test3", true, Split.TypeNum.Chars, 2);
        splitter.splitFile("testResources/in1.txt");
        assertTrue(contentEquals(
                new File("testResources/out11.txt"),
                new File("out_test31")
        ));
        assertTrue(contentEquals(
                new File("testResources/out12.txt"),
                new File("out_test32")
        ));
    }
}
