package ch.heiafr.pigroup6.passioncuisine.model.workshop;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;

@Entity
@Table(name = "workshop_draft")

@NamedStoredProcedureQuery(name = "WorkshopDraft.edit",
        procedureName = "editworkshopdraft", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "k1", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "k2", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v1", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v2", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v3", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v4", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v5", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v6", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v7", type = Short.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v8", type = Short.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v9", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v10", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v11", type = Short.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v12", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "v13", type = String.class)
})

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class WorkshopDraft extends Workshop {
    // === Constructors
    public WorkshopDraft() {
        state = WorkshopState.draft;
    }
}
