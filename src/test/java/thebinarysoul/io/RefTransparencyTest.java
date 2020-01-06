package thebinarysoul.io;

import com.thebinarysoul.io.IO;
import org.junit.Assert;
import org.junit.Test;

public class RefTransparencyTest {
    int externalVariable = 0;

    private int addAndIncrementExtVar(final int a, final int b){
        final var result = a + b;
        externalVariable += 1;
        return result;
    }

    @Test
    public void willNotBeReferentiallyTransparent() {
        final var sumOfTwiceMethodCall = addAndIncrementExtVar(1, 1) + addAndIncrementExtVar(1, 1);
        final var extVarByCallingMethodTwice = externalVariable;
        externalVariable = 0;

        final var value = addAndIncrementExtVar(1, 1);
        final var sumOfValues = value + value;
        final var extVarByConvertCallToValue = externalVariable;
        externalVariable = 0;

        Assert.assertEquals(sumOfTwiceMethodCall, sumOfValues);
        Assert.assertNotEquals(extVarByCallingMethodTwice, extVarByConvertCallToValue);
    }

    @Test
    public void willBeReferentiallyTransparent() {
        final var sumOfTwiceMethodCall = IO.apply(() -> addAndIncrementExtVar(1, 1))
                                                 .map(x -> x + addAndIncrementExtVar(1, 1))
                                                 .unsafeRun();

        final var extVarByCallingMethodTwice = externalVariable;
        externalVariable = 0;

        final var value = IO.apply(() -> addAndIncrementExtVar(1, 1));
        final var sumOfValues = value.flatMap(x -> value.map(y -> x + y)).unsafeRun();
        final var extVarByConvertCallToValue = externalVariable;
        externalVariable = 0;

        Assert.assertEquals(sumOfTwiceMethodCall, sumOfValues);
        Assert.assertEquals(extVarByCallingMethodTwice, extVarByConvertCallToValue);
    }
}
