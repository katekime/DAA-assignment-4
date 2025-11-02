package graph.metrics;

public class Metrics {
    private long startTime;
    private long endTime;

    private int dfsVisits;
    private int edgeTraversals;
    private int stackPushes;
    private int stackPops;
    private int relaxations;

    public Metrics() {
        reset();
    }

    public void startTiming() {
        startTime = System.nanoTime();
    }

    public void stopTiming() {
        endTime = System.nanoTime();
    }

    public long getElapsedTimeNanos() {
        return endTime - startTime;
    }

    public double getElapsedTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public void reset() {
        startTime = 0;
        endTime = 0;
        dfsVisits = 0;
        edgeTraversals = 0;
        stackPushes = 0;
        stackPops = 0;
        relaxations = 0;
    }

    public void incrementDfsVisits() { dfsVisits++; }
    public void incrementEdgeTraversals() { edgeTraversals++; }
    public void incrementStackPushes() { stackPushes++; }
    public void incrementStackPops() { stackPops++; }
    public void incrementRelaxations() { relaxations++; }
    public int getDfsVisits() { return dfsVisits; }
    public int getEdgeTraversals() { return edgeTraversals; }
    public int getStackPushes() { return stackPushes; }
    public int getStackPops() { return stackPops; }
    public int getRelaxations() { return relaxations; }

    @Override
    public String toString() {
        return String.format("Metrics{time=%.2fms, dfsVisits=%d, edges=%d, pushes=%d, pops=%d, relaxations=%d}", getElapsedTimeMs(), dfsVisits, edgeTraversals, stackPushes, stackPops, relaxations
        );
    }
}
