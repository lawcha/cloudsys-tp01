package ch.heiafr.pigroup6.passioncuisine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;
import java.time.LocalDate;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class IngredientList implements Serializable {
    private String title;
    private LocalDate date;
    private int count;
    private String ingredients;

    public IngredientList(String title, LocalDate date, int count, String ingredients) {
        this.title = title;
        this.date = date;
        this.count = count;
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public String getIngredients() {
        return ingredients;
    }
}
