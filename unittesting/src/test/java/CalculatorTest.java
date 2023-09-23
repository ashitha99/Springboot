import org.junit.Calculator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorTest {
   @BeforeAll
   public static void setupInitial(){
       System.out.println("Setting up Calculator");
   }
   @BeforeEach
   public void setup(){
       System.out.println("Setting Up.....");

    }
    @AfterAll
    public static void end(){
       System.out.println("Success");
    }
    @AfterEach
    public void calculate(){
       System.out.println("Calculating....");
    }


    @ParameterizedTest
    @CsvSource({"1,2,3",
                "2, 3, 5",
                "5, 5, 10"
    })
    public void testAdd(int param1,int param2, int expected){
       Calculator calculator=new Calculator();
       int sum=calculator.add(param1,param2);
       assertEquals(expected,sum);
    }


    @Test
    public void testSubtract(){
        Calculator calculator=new Calculator();
        int subtract=calculator.subtract(8,4);
    }
    @Test
    public void testMultiply(){
        Calculator calculator=new Calculator();
        int multiple=calculator.multiple(8,4);
    }
    @Test
    public void testDivide(){
        Calculator calculator=new Calculator();
        int devide=calculator.divide(200,10);

    }



}
