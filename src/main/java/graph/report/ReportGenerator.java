package graph.report;
import graph.dagsp.DAGShortestPath;
import graph.metrics.Metrics;
import graph.model.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ReportGenerator {
    public final BufferedWriter writer;
    private final BufferedWriter csvWriter;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ReportGenerator(String outputPath, String csvPath) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(outputPath));
        this.csvWriter = new BufferedWriter(new FileWriter(csvPath));

        csvWriter.write("Dataset,Nodes,Edges,SCCs,SCC_Time_ms,Topo_Time_ms,Path_Time_ms,Type\n");
    }

    public void writeHeader(String title) throws IOException {
        writer.write("=".repeat(80) + "\n");
        writer.write(title + "\n");
        writer.write("Generated: " + LocalDateTime.now().format(formatter) + "\n");
        writer.write("=".repeat(80) + "\n\n");
    }

    public void writeGraphInfo(String fileName, Graph graph, int source) throws IOException {
        writer.write("-".repeat(80) + "\n");
        writer.write("FILE: " + fileName + "\n");
        writer.write("-".repeat(80) + "\n");
        writer.write("Vertices: " + graph.getVertices() + "\n");
        writer.write("Edges: " + graph.getEdgeCount() + "\n");
        writer.write("Source: " + source + "\n\n");
    }

    public void writeSCCResults(List<List<Integer>> sccs, Metrics metrics) throws IOException {
        writer.write("Strongly Connected Components");
        writer.write("Total SCCs: " + sccs.size() + "\n");
        for (int i = 0; i < sccs.size(); i++) {
            writer.write("SCC " + i + " (size=" + sccs.get(i).size() + "): " +
                    sccs.get(i) + "\n");
        }
        writer.write("Metrics: " + metrics + "\n\n");
    }

    public void writeTopologicalOrder(List<Integer> order, Metrics metrics) throws IOException {
        writer.write("Topological Order");
        if (order == null) {
            writer.write("Graph contains cycles - no topological order exists!\n");
        } else {
            writer.write("Order: " + order + "\n");
        }
        writer.write("Metrics: " + metrics + "\n\n");
    }

    public void writePathResults(DAGShortestPath.PathResult result, boolean isShortest, Metrics metrics) throws IOException {
        writer.write("=== " + (isShortest ? "Shortest" : "Longest") + " Paths from source " + result.source + " ===\n");

        for (int i = 0; i < result.distances.length; i++) {
            if (result.distances[i] != Integer.MAX_VALUE && result.distances[i] != Integer.MIN_VALUE) {
                writer.write("To vertex " + i + ": distance=" + result.distances[i] + ", path=" + result.getPath(i) + "\n");
            }
        }

        if (!isShortest) {
            int criticalTarget = result.getCriticalPathTarget();
            if (criticalTarget != -1) {
                writer.write("\n*** CRITICAL PATH ***\n");
                writer.write("From " + result.source + " to " + criticalTarget + ": length=" + result.distances[criticalTarget] + ", path=" + result.getPath(criticalTarget) + "\n");
            }
        }

        writer.write("Metrics: " + metrics + "\n\n");
    }

    public void writeSummaryTable(List<SummaryRow> rows) throws IOException {
        writer.write("\n" + "=".repeat(80) + "\n");
        writer.write("PERFORMANCE SUMMARY\n");
        writer.write("=".repeat(80) + "\n");
        writer.write(String.format("%-25s | %6s | %6s | %5s | %10s | %10s | %10s\n", "Dataset", "Nodes", "Edges", "SCCs", "SCC(ms)", "Topo(ms)", "Path(ms)"));
        writer.write("-".repeat(80) + "\n");

        for (SummaryRow row : rows) {
            writer.write(String.format("%-25s | %6d | %6d | %5d | %10.3f | %10.3f | %10.3f\n", row.fileName, row.nodes, row.edges, row.sccs, row.sccTime, row.topoTime, row.pathTime));
        }
        writer.write("=".repeat(80) + "\n");
    }

    public void writeCSVRow(SummaryRow row) throws IOException {
        String type = row.sccs == row.nodes ? "DAG" : "Cyclic";
        csvWriter.write(String.format(Locale.US, "%s,%d,%d,%d,%.3f,%.3f,%.3f,%s\n", row.fileName, row.nodes, row.edges, row.sccs, row.sccTime, row.topoTime, row.pathTime, type));
    }

    public void close() throws IOException {
        writer.close();
        csvWriter.close();
    }

    public static class SummaryRow {
        public String fileName;
        public int nodes;
        public int edges;
        public int sccs;
        public double sccTime;
        public double topoTime;
        public double pathTime;

        public SummaryRow(String fileName, int nodes, int edges, int sccs, double sccTime, double topoTime, double pathTime) {
            this.fileName = fileName;
            this.nodes = nodes;
            this.edges = edges;
            this.sccs = sccs;
            this.sccTime = sccTime;
            this.topoTime = topoTime;
            this.pathTime = pathTime;
        }
    }
}
