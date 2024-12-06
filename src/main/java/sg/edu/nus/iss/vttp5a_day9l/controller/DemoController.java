package sg.edu.nus.iss.vttp5a_day9l.controller;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.vttp5a_day9l.model.Carpark;
import sg.edu.nus.iss.vttp5a_day9l.model.Weather;
import sg.edu.nus.iss.vttp5a_day9l.service.CarparkService;
import sg.edu.nus.iss.vttp5a_day9l.service.WeatherService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/demo")
public class DemoController {
    
    @Autowired
    CarparkService carparkService;

    @Autowired
    WeatherService weatherService;

    @GetMapping("/carparks")
    @ResponseBody
    public List<Carpark> getCarparkRates() {

        List<Carpark> carparks = carparkService.getCarparkRates();

        return carparks;
    }

    @GetMapping("/weather")
    @ResponseBody
    public Weather getMethodName(@RequestParam("apiKey") String apiKey, @RequestParam("country") String country, @RequestParam("airQuality") String airQuality) {

        ResponseEntity<String> resWeather = weatherService.getWeatherData(apiKey, country, airQuality);

        // use Json-P to extract out the data you want to Weather object
        // return the Weather object
        // can do this in service class
        // should do this in service class
        String rawString = resWeather.getBody();

        Weather weatherData = new Weather();

        JsonReader jReader = Json.createReader(new StringReader(rawString));
        JsonObject jObject = jReader.readObject();
        weatherData.setCountry(jObject.get("location").asJsonObject().getString("country"));
        weatherData.setRegion(jObject.get("location").asJsonObject().getString("region"));
        weatherData.setLatitude(jObject.get("location").asJsonObject().getJsonNumber("lat").doubleValue());
        weatherData.setLongitude(jObject.get("location").asJsonObject().getJsonNumber("lon").doubleValue());
        weatherData.setCo(jObject.get("current").asJsonObject().get("air_quality").asJsonObject().getJsonNumber("co").doubleValue());
        weatherData.setPm25(jObject.get("current").asJsonObject().get("air_quality").asJsonObject().getJsonNumber("pm2_5").doubleValue());

        return weatherData;
    }
}