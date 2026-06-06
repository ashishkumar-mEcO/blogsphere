package com.blogapp.blogsphere.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity   //This class = database table
@Table(name = "users")    //Table name in MySQL
@Data        //Lombok — auto generates getters/setters
@NoArgsConstructor      //Lombok - creates a no-argument constructor
@AllArgsConstructor     //Lombok - creates a constructor containing all fields (variables) of the class.
public class User {

        @Id     //Primary key
        @GeneratedValue(strategy = GenerationType.IDENTITY)    //Auto increment ID
        private Long id;

        @Column(nullable = false)  //NOT NULL in database
        private String name;

        @Column(nullable = false, unique = true)  //No duplicate emails
        private String email;

        @Column(nullable = false)
        @JsonIgnore
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role = Role.USER;

        @Column(name = "created_at")
        private LocalDateTime createdAt = LocalDateTime.now();

        @JsonIgnore
        @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
        private List<Post> posts;

        public enum Role {
            USER, ADMIN
        }
}

