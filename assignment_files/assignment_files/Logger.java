import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger implements ObservableCacheSearcher.Observer {

    private static Logger instance;
    private String filename;
    private BufferedWriter writer;

    private Logger(String filename, ObservableCacheSearcher observableCacheSearcher) {
        this.filename = filename;
        observableCacheSearcher.addObserver(this);
        try {
            writer = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Logger getInstance(String filename, ObservableCacheSearcher observableCacheSearcher) {
        if (instance == null) {
            instance = new Logger(filename, observableCacheSearcher);
        }
        return instance;
    }

    @Override
    public synchronized void update(ObservableCacheSearcher observable, String about) {
        try {
            writer.write(about + " " + observable.search("").size() + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
