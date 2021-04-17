package generation.layer.random.tunnel;

import java.util.Random;

public class Tunnel {
    public static void main(String args[]){
        Random rand = new Random(1); //instance of random class
        int upperbound = 25;
        //generate random values from 0-24
        int int_random = rand.nextInt(upperbound);
        double double_random=rand.nextDouble();
        float float_random=rand.nextFloat();
        System.out.println(int_random);
        System.out.println(double_random);
        System.out.println(float_random);
        System.out.println(rand.nextInt(upperbound));
        System.out.println(rand.nextInt(upperbound));
        System.out.println(rand.nextInt(upperbound));
        System.out.println(rand.nextInt(upperbound));
    }
}
