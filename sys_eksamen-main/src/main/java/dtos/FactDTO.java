package dtos;

import entities.Fact;

public class FactDTO {
    int id;
    String factBody;

    public FactDTO(Fact fact) {
        if (fact.getId() != null) this.id = Math.toIntExact(fact.getId());
       this.factBody=fact.getBody();
    }
}

