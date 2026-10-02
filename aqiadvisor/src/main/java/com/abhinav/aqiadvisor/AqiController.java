package com.abhinav.aqiadvisor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
public class AqiController {

    private static final String WAQI_TOKEN = System.getenv().getOrDefault("WAQI_TOKEN", "");

    @GetMapping("/api/aqi")
    public Map<String, Object> getAqiAdvice(
            @RequestParam double lat,
            @RequestParam double lon) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.waqi.info/feed/geo:" + lat + ";" + lon + "/?token=" + WAQI_TOKEN;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        int aqi = (int) data.get("aqi");

        String advice = getAdviceForAqi(aqi);

        return Map.of(
                "aqi", aqi,
                "category", getCategoryForAqi(aqi),
                "advice", advice
        );
    }

    private String getCategoryForAqi(int aqi) {
        if (aqi <= 50) return "Good";
        if (aqi <= 100) return "Satisfactory";
        if (aqi <= 200) return "Moderate";
        if (aqi <= 300) return "Poor";
        if (aqi <= 400) return "Very Poor";
        return "Severe";
    }

    private String getAdviceForAqi(int aqi) {
        if (aqi <= 50) return "Air quality is good. Enjoy normal outdoor activities.";
        if (aqi <= 100) return "Air is acceptable. Sensitive individuals should consider limiting prolonged outdoor exertion.";
        if (aqi <= 200) return "Generally okay, but people with existing breathing or heart conditions should reduce prolonged outdoor activity.";
        if (aqi <= 300) return "Discomfort likely on prolonged exposure. Avoid long or intense outdoor activity; consider a mask outdoors.";
        if (aqi <= 400) return "Health effects likely even on short exposure. Avoid outdoor activity; wear a mask if you must go out.";
        return "Serious health risk for everyone. Stay indoors as much as possible; use an air purifier if you have one.";
    }
}

