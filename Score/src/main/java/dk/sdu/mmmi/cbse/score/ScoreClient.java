package dk.sdu.mmmi.cbse.score;

import dk.sdu.mmmi.cbse.common.services.ScoreSPI;
import org.springframework.web.client.RestTemplate;

public class ScoreClient implements ScoreSPI {

    private static final String BASE = "http://localhost:8080/scores";

    private final RestTemplate rest = new RestTemplate();

    @Override
    public void submit(double score) {
        try {
            rest.postForObject(BASE + "?score=" + score, null, String.class);
        } catch (Exception ignored) {
        }
    }

    @Override
    public double best() {
        return read(BASE + "/best");
    }

    @Override
    public double last() {
        return read(BASE + "/last");
    }

    private double read(String url) {
        try {
            String body = rest.getForObject(url, String.class);
            return body == null ? 0 : Double.parseDouble(body.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
