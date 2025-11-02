import graph.dagsp.DAGShortestPath;
import graph.model.Graph;
import graph.scc.CondensationGraph;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopologicalSort;
import util.JSONLoader;
import graph.report.ReportGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("SMART CITY SCHEDULING - Assignment 4");
        System.out.println("SCC, Topological Sort, and DAG Shortest Paths");
        System.out.println("=".repeat(70));

        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        String reportPath = "output/analysis_report.txt";
        String csvPath = "output/performance_results.csv";
        try {
            ReportGenerator report = new ReportGenerator(reportPath, csvPath);
            report.writeHeader("SMART CITY SCHEDULING - ANALYSIS REPORT");

            List<ReportGenerator.SummaryRow> summaryRows = new ArrayList<>();

            String[] dataSizes = {"small", "medium", "large"};

            for (String size : dataSizes) {
                System.out.println("\n" + "=".repeat(70));
                System.out.println("Processing " + size.toUpperCase() + " datasets");
                System.out.println("=".repeat(70));
                report.writeHeader("Processing " + size.toUpperCase() + " datasets");
                File folder = new File("data/" + size);
                if (!folder.exists() || !folder.isDirectory()) {
                    String msg = "Folder not found: " + folder.getPath();
                    System.out.println(msg);
                    report.writer.write(msg + "\n");
                    continue;
                }
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
                if (files == null || files.length == 0) {
                    String msg = "No JSON files found in " + folder.getPath();
                    System.out.println(msg);
                    report.writer.write(msg + "\n");
                    continue;
                }
                for (File file : files) {
                    ReportGenerator.SummaryRow row = processGraphFile(file.getPath(), report);
                    if (row != null) {
                        summaryRows.add(row);
                        report.writeCSVRow(row);
                    }
                }
            }
            report.writeSummaryTable(summaryRows);
            report.close();

            System.out.println("\n" + "=".repeat(70));
            System.out.println("Analysis Complete!");
            System.out.println("Text Report: " + reportPath);
            System.out.println("CSV Results: " + csvPath);
            System.out.println("=".repeat(70));

        } catch (Exception e) {
            System.err.println("Error creating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ReportGenerator.SummaryRow processGraphFile(String filePath, ReportGenerator report) {
        try {
            System.out.println("\n" + "-".repeat(70));
            System.out.println("FILE: " + filePath);
            System.out.println("-".repeat(70));

            JSONLoader.GraphData data = JSONLoader.loadFromJSON(filePath);
            Graph graph = data.graph;

            String fileName = new File(filePath).getName();
            report.writeGraphInfo(fileName, graph, data.source);

            System.out.println("Description: " + data.description);
            System.out.println("Vertices: " + graph.getVertices());
            System.out.println("Edges: " + graph.getEdgeCount());
            System.out.println("Source: " + data.source);

            TarjanSCC tarjan = new TarjanSCC(graph);
            List<List<Integer>> sccs = tarjan.findSCCs();
            tarjan.printSCCs(sccs);
            report.writeSCCResults(sccs, tarjan.getMetrics());

            double sccTime = tarjan.getMetrics().getElapsedTimeMs();

            CondensationGraph condensation = new CondensationGraph(graph, sccs);
            Graph dag = condensation.buildCondensation();
            condensation.printCondensation();

            KahnTopologicalSort topoSort = new KahnTopologicalSort(dag);
            List<Integer> topoOrder = topoSort.sort();
            topoSort.printTopologicalOrder(topoOrder);
            report.writeTopologicalOrder(topoOrder, topoSort.getMetrics());

            double topoTime = topoSort.getMetrics().getElapsedTimeMs();
            double pathTime = 0.0;

            if (sccs.size() == graph.getVertices()) {
                System.out.println("Graph is a pure DAG - computing paths");

                DAGShortestPath dagSP = new DAGShortestPath(graph);

                DAGShortestPath.PathResult shortestPaths = dagSP.findShortestPaths(data.source);
                shortestPaths.printPaths();
                report.writePathResults(shortestPaths, true, dagSP.getMetrics());

                pathTime = dagSP.getMetrics().getElapsedTimeMs();

                DAGShortestPath.PathResult longestPaths = dagSP.findLongestPaths(data.source);
                longestPaths.printPaths();
                report.writePathResults(longestPaths, false, dagSP.getMetrics());

            } else {
                System.out.println("Graph contains cycles - DAG path algorithms skipped");
            }

            return new ReportGenerator.SummaryRow(fileName, graph.getVertices(), graph.getEdgeCount(), sccs.size(), sccTime, topoTime, pathTime
            );

        } catch (Exception e) {
            System.err.println("Error processing " + filePath + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
