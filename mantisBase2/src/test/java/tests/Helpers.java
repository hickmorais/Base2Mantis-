package tests;

public class Helpers {
    public void espera(double segundos){
        try {
            Thread.sleep((long)(segundos * 1000));
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
