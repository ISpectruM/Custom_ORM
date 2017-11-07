package entities;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

@Entity(name = "example_entity")
public class ExampleEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "town")
    private String town;

    public ExampleEntity() {
    }

    public ExampleEntity(String fullName, String town) {
        this.fullName = fullName;
        this.town = town;
    }
}
