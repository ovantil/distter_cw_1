import java.nio.file.LinkPermission;
import java.time.Instant;

public class Requests {
    public Requests() {

    }

    public void process(String request) {

    }

    public String input(String type) {
        String response = null;

        switch (type) {
            case "WHEN?":
                long unixTimestamp = Instant.now().getEpochSecond();
                response = "NOW " + unixTimestamp;
                break;

        }
        return response;
    }
}