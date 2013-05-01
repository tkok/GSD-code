package dk.itu.policyengine.domain;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class IntervalAdapter implements JsonSerializer<Interval> {

	 @Override
	 public JsonElement serialize(Interval interval, Type typeOfSrc, JsonSerializationContext context) {
	        JsonObject jsonObject = new JsonObject();
	        
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
	        
	        jsonObject.addProperty("fromTime", simpleDateFormat.format(interval.getFromTime()));
	        jsonObject.addProperty("toTime", simpleDateFormat.format(interval.getToTime()));

	        return jsonObject;
	    }
	}