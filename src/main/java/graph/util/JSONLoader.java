package util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import graph.model.Graph;

import java.io.FileReader;
import java.io.IOException;

public class JSONLoader {
    public static class GraphData {
        public Graph graph;
        public int source;
        public String weightModel;
        public String description;

        public GraphData(Graph graph, int source, String weightModel, String description) {
            this.graph = graph;
            this.source = source;
            this.weightModel = weightModel;
            this.description = description;
        }
    }
    public static GraphData loadFromJSON(String filePath) throws IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(new FileReader(filePath), JsonObject.class);

        int n = jsonObject.get("n").getAsInt();
        Graph graph = new Graph(n);

        JsonArray edges = jsonObject.getAsJsonArray("edges");
        for (int i = 0; i < edges.size(); i++) {
            JsonObject edge = edges.get(i).getAsJsonObject();
            int u = edge.get("u").getAsInt();
            int v = edge.get("v").getAsInt();
            int w = edge.has("w") ? edge.get("w").getAsInt() : 1;
            graph.addEdge(u, v, w);
        }

        int source = jsonObject.has("source") ? jsonObject.get("source").getAsInt() : 0;
        String weightModel = jsonObject.has("weight_model") ?
                jsonObject.get("weight_model").getAsString() : "edge";
        String description = jsonObject.has("description") ?
                jsonObject.get("description").getAsString() : "";

        return new GraphData(graph, source, weightModel, description);
    }
}
