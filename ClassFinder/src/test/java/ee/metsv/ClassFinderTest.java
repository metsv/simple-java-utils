package ee.metsv;

import ee.metsv.ClassFinder;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created with IntelliJ IDEA.
 * User: metsv
 * Date: 8/15/13
 * Time: 8:51 PM
 */
public class ClassFinderTest {

    ClassFinder classFinderTestSet1;

    @Before
    public void setUp() throws Exception {
        classFinderTestSet1 = new ClassFinder(IOUtils.toInputStream("a.b.FooBarBaz\n" +"c.d.FooBar\n" + "Test\n" + " \n" + "Blah "));
    }

    @Test
    public void testCamelCaseMatchingWithFilterFBB() throws Exception {
        Collection foundClassNames = classFinderTestSet1.findMatching("FBB");
        assertEquals(Arrays.asList("a.b.FooBarBaz"), foundClassNames);
        assertEquals(1, foundClassNames.size());
    }

    public void testCamelCaseMatchingWithFilterfbb() throws Exception {
        Collection foundClassNames = classFinderTestSet1.findMatching("fbb");
        assertEquals(Arrays.asList("a.b.FooBarBaz"), foundClassNames);
        assertEquals(1, foundClassNames.size());
    }


    @Test
    public void testCamelCaseMatchingWithFilterFB() throws Exception {
        Collection foundClassNames = classFinderTestSet1.findMatching("FB");
        assertEquals(Arrays.asList("c.d.FooBar","a.b.FooBarBaz"), foundClassNames);
        assertEquals(2, foundClassNames.size());
    }

    @Test
    public void testCamelCaseMatchingWithFilterFoBa() throws Exception {
        Collection foundClassNames = classFinderTestSet1.findMatching("FoBa");
        assertEquals(Arrays.asList("c.d.FooBar","a.b.FooBarBaz"), foundClassNames);
        assertEquals(2, foundClassNames.size());
    }

    @Test
    public void testCamelCaseMatchingWithFilterFBar() throws Exception {
        Collection foundClassNames = classFinderTestSet1.findMatching("FBar");
        assertEquals(Arrays.asList("c.d.FooBar","a.b.FooBarBaz"), foundClassNames);
        assertEquals(2, foundClassNames.size());
    }

    @Test
    public void testCamelCaseMatchingWithFilterFBarWithSpace() throws Exception {
        Collection foundClassNames = classFinderTestSet1.findMatching("FBar ");
        assertEquals(Arrays.asList("c.d.FooBar"), foundClassNames);
        assertEquals(1, foundClassNames.size());
    }

    @Test
    public void testCamelCaseMatchingWithFilterFBazWithWildCard() throws Exception {
        Collection foundClassNames = classFinderTestSet1.findMatching("F*Baz");
        assertEquals(Arrays.asList("a.b.FooBarBaz"), foundClassNames);
        assertEquals(1, foundClassNames.size());
    }

    @Test
    public void testCamelCaseMatchingWithFilterBBWithWildCard() throws Exception {
        Collection foundClassNames = classFinderTestSet1.findMatching("*BB");
        assertEquals(Arrays.asList("a.b.FooBarBaz"), foundClassNames);
        assertEquals(1, foundClassNames.size());
    }


    @Test
    public void testSortClassNamesWithOutPackageName() throws Exception {

        List sorted = Arrays.asList("a.b.A", "c.D", "c.d.FaooBar", "a.b.FooBarBaz", "K" );
        List notSorted = Arrays.asList("K", "a.b.A", "a.b.FooBarBaz", "c.d.FaooBar", "c.D");
        classFinderTestSet1.sortClassNamesWithOutPackageName(notSorted);
        assertEquals(sorted, notSorted);
    }

    @Test
    public void testGetClassName() throws Exception {
        assertEquals("FaooBar", classFinderTestSet1.getClassName("c.d.FaooBar"));

    }

    @Test
    public void testGetPackageName() throws Exception {
        assertEquals("c.d.", classFinderTestSet1.getPackageName("c.d.FaooBar"));
    }

}
