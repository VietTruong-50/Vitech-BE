package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(
        exclude= {"products"},
        callSuper = false
)
@Entity
@Table(name = "image_model")
public class ImageModel extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 255)
    private String imageName;

    @Column(length = 500000)
    private byte[] imageByte;

    private String type;

    public ImageModel(String imageName, String type, byte[] imageByte){
        this.imageName = imageName;
        this.type = type;
        this.imageByte = imageByte;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "productImages")
    private Set<Product> products = new HashSet<>();

}
