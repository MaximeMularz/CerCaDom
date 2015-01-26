package lpa.aumont.cercadom;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by max on 12/12/2014.
 */
public class FullTestSuite extends TestSuite{

    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public FullTestSuite(){
        super();
    }

}
