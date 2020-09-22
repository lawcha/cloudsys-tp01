package ch.heiafr.pigroup6.passioncuisine.repository.workshop;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopDraft;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopTerminated;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class WorkshopTerminatedSpecification implements Specification<WorkshopTerminated> {
    private String key;
    private Object value;

    public WorkshopTerminatedSpecification(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<WorkshopTerminated> root, CriteriaQuery<?> q, CriteriaBuilder b) {
        return b.equal(root.get(key), value);
    }
}
