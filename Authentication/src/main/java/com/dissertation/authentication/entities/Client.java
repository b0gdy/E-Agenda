package com.dissertation.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
//@DiscriminatorValue("client")
public class Client extends User {

    private Long companyId;

    @ManyToOne
    @JoinColumn(name = "companyId", insertable = false, updatable = false)
    private Company company;

}
