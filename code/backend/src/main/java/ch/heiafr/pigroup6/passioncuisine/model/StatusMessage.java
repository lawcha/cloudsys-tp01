package ch.heiafr.pigroup6.passioncuisine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class StatusMessage {
    private int code;
    private String message;

    public StatusMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
