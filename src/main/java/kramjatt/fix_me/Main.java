package kramjatt.fix_me;

public class Main  {
    public static void main(String[] args) {
        Broker broker = new Broker();
        Market market = new Market();

        broker.start();
        market.start();
    }
}
