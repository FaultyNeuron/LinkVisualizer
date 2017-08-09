import com.google.gson.Gson;

import java.io.*;

/**
 * Created by Georg Plaz.
 */
public class Settings {
    private static final String SETTINGS_NAME = "settings.json";
    private static Gson gson = new Gson();

    private boolean drawText = true;
    private double repulsiveness;

    public void toJson() throws IOException {
        toJson(null);
    }

    public void toJson(File folder) throws IOException {
        File target;
        if(folder==null){
            target = new File(SETTINGS_NAME);
        }else{
            target = new File(folder, SETTINGS_NAME);
        }
        FileWriter writer = new FileWriter(target);
        writer.write(new Gson().toJson(this));
        writer.close();
    }

    public boolean isDrawingText() {
        return drawText;
    }

    public void setDrawText(boolean drawText) {
        this.drawText = drawText;
    }
    public static Settings read(File folder) throws IOException {
        File settingsFile = new File(folder, SETTINGS_NAME);
        if(!settingsFile.exists()){
            Settings toReturn = new Settings();
            toReturn.toJson(folder);
            return toReturn;
        }
        BufferedReader br = new BufferedReader(new FileReader(settingsFile));
        return gson.fromJson(br, Settings.class);
    }

    public double getRepulsiveness() {
        return repulsiveness;
    }

    public void setRepulsiveness(double repulsiveness) {
        this.repulsiveness = repulsiveness;
    }
}
