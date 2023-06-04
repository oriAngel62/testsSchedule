package login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

class RequestBody {
    private Map<String, Object> properties;

    public RequestBody() {
        properties = new HashMap<>();
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(properties);
    }
}

