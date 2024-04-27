package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.generator.Generator;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

@Entity
public class Wizard implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty
    private String name;
    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard(){
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public void addArtifact(Artifact artifact) {
        artifact.setOwner(this);
        this.artifacts.add(artifact);
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }
    public void removeAllArtifacts(){
        this.artifacts.stream().forEach(artifact -> artifact.setOwner(null));
        this.artifacts = new ArrayList<>();
    }
    public void removeArtifact(Artifact artifactToBeAssigned){
        //Remove artifact owner
        artifactToBeAssigned.setOwner(null);
        this.artifacts.remove(artifactToBeAssigned);
    }

}
