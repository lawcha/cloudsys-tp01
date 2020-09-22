package ch.heiafr.pigroup6.passioncuisine.model.workshop;

import javax.persistence.AttributeConverter;
import java.util.stream.Stream;

public enum WorkshopState {
    draft,
    published,
    closed;

    public static WorkshopState of(String name) {
        return Stream.of(values())
                .filter(p -> p.name().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
