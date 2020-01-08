package thebinarysoul.io;

import com.thebinarysoul.io.IO;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class SuperficialLawsTest {
    private IO<String> IOMonad = IO.apply(() -> "123");
    private Function<String, IO<Integer>> f = str -> IO.apply(str::length);
    private Function<Integer, IO<String>> g = integer -> IO.apply(integer::toString);

    @Test
    public void checkLeftIdentityLaw(){
        Assert.assertEquals(
                IO.apply(() -> "123").flatMap(f).unsafeRun(),
                f.apply("123").unsafeRun()
        );
    }

    @Test
    public void checkRightIdentityLaw(){
        Assert.assertEquals(
                IOMonad.flatMap(str -> IO.apply(() -> str)).unsafeRun(),
                IOMonad.unsafeRun()
        );
    }

    @Test
    public void checkAssociativityLaw(){
        Assert.assertEquals(
                IOMonad.flatMap(f).flatMap(g).unsafeRun(),
                IOMonad.flatMap(x -> f.apply(x).flatMap(g)).unsafeRun()
        );
    }
}
