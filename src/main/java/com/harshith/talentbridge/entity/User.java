package com.harshith.talentbridge.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.harshith.talentbridge.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="users")

public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)//Postgre will Generate ID's
    private Long id;

    @Column(nullable=false,length=100)//name should not be empty
    private String name;

    @Column(nullable=false,unique=true)//email should not be empty and it should be unique also
    private String email;

    @Column(nullable=false)//password should not be empty
    private String password;

    @Enumerated(EnumType.STRING)//this belongs to the STRINGS that are there in ROLE
    @Column(nullable=false)
    private Role role;

    @CreationTimestamp
    @Column(nullable=false,updatable =false)//CreatedAt also shouls not be empty and it wont update again one if register the accounts time and date will be permanent
    private LocalDateTime createdAt;//we cant update it if we once REGISTERED

    public User(){

    }
    public User(String name ,String email,String password,Role role){
        this.name=name;
        this.email=email;
        this.password=password;
        this.role=role;
    }
    public Long getId(){
        return id;//return's the ID generated be POSTGRE
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public Role getRole(){
        return role;// person who is registering should enter the VALUES which are in enums/Role class
    }//Returns the user's role (STUDENT, RECRUITER, or ADMIN)
    public void setRole(Role role){
        this.role=role;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
}
