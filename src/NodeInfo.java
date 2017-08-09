import com.google.gson.Gson;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Georg Plaz.
 */
public class NodeInfo {
    private static Gson gson = new Gson();
    public static final String NODE_INFO_NAME = "node_info";
    private String imageSource;
    private String link;
    private List<String> childrenFolder;

    public NodeInfo() {

    }

    public NodeInfo(String imageSource, String link, Collection<String> childrenFolder) {
        this.imageSource = imageSource;
        this.link = link;
        this.childrenFolder = new LinkedList<>(childrenFolder);
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getLink() {
        return link;
    }

    public List<String> getChildrenFolder() {
        return childrenFolder;
    }

    public void toJson(File nodeFolder, String nodeInfoName) throws IOException {
        FileWriter writer;
        File jsonFile = new File(nodeFolder, nodeInfoName+".json");
        writer = new FileWriter(jsonFile);
        writer.write(gson.toJson(this));
        writer.close();
    }

    public static NodeInfo read(File parentFolder) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(new File(parentFolder, NodeInfo.NODE_INFO_NAME+".json")));
        return gson.fromJson(br, NodeInfo.class);
    }
}
