package shenkar.finalProject.googleDocs.MVVC;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * documentation: https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions
 *
 * look at the import, org.junit.jupiter.api.Assertions.(function name)
 * there is a lot of functions, take a look at documentations
 *
 */
public class TestModel {

    @Test
    void addition() {
            assertEquals(2,1+1 ,"here we can set Error message if test fail");
    }

}
