package ch.heiafr.pigroup6.passioncuisine.repository.workshop;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopDraft;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class WorkshopDraftSpecification implements Specification<WorkshopDraft> {
    private String key;
    private Object value;

    public WorkshopDraftSpecification(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<WorkshopDraft> root, CriteriaQuery<?> q, CriteriaBuilder b) {
        return b.equal(root.get(key), value);
    }
}
