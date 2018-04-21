package test.java;

public class Playground {

    private Strategy currentStrategy;

    private enum Strategy {
        STRATEGY1, STRATEGY2, STRATEGY3
    }

    public void selectStrategy1() {
        currentStrategy = Strategy.STRATEGY1;
    }

    public void selectStrategy2() {
        currentStrategy = Strategy.STRATEGY2;
    }

    public void selectStrategy3() {
        currentStrategy = Strategy.STRATEGY3;
    }

    public void startSearching() {
        switch (currentStrategy) {
            case STRATEGY1:

                break;
            case STRATEGY2:

                break;
            case STRATEGY3:

                break;
        }
    }
}
