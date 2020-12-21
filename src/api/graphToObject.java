package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class graphToObject implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            JsonObject object = jsonElement.getAsJsonObject();
            JsonArray nodeAr = object.get("Nodes").getAsJsonArray();
            JsonArray edgeAr = object.get("Edges").getAsJsonArray();
            directed_weighted_graph graph = new DWGraph_DS();
            for (JsonElement j : nodeAr) {
                JsonElement j2 = j.getAsJsonObject();
                String str = j2.getAsJsonObject().get("pos").getAsString();
                int index = j2.getAsJsonObject().get("id").getAsInt();
                double [] geo_loc = splitPoint3D(str);
                double x = geo_loc[0];
                double y = geo_loc[1];
                double z = geo_loc[2];
                geo_location geo = new NodeD.GeoLocation(x,y,z);
                node_data node = new NodeD(index,geo);
                graph.addNode(node);
            }
            for (JsonElement e : edgeAr) {
                JsonElement j3 = e.getAsJsonObject();
                int src = j3.getAsJsonObject().get("src").getAsInt();
                int dest = j3.getAsJsonObject().get("dest").getAsInt();
                double weight = j3.getAsJsonObject().get("w").getAsDouble();
                graph.connect(src,dest,weight);
            }
            return graph;
        }
        catch (JsonIOException exception) {
            return null;
        }
    }

    public double [] splitPoint3D (String str) {
        String point = "";
        int num = 0;
        double [] arr = new double[3];
        for (int i =0; i < str.length(); i++) {
            if (str.charAt(i) != ',')
                point += str.charAt(i);
            else {
                double n = Double.parseDouble(point);
                arr[num] = n;
                num++;
                point = "";
            }
        }
        return  arr;
    }
}
