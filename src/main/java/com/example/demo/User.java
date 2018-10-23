//package com.example.demo;
//
//import lombok.Data;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//@RestController
//@Entity
//@Data
//public class User {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private long id;
//
//
//	private String email;
//	private String firstName;
//	private String lastName;
//
//
//    public User( final String email, final String firstName, final String lastName) {
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//    public User( final String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(final String email) {
//		this.email = email;
//	}
//
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//}


//class UserCommandLineRunner implements CommandLineRunner{
//        private final UserRepo repository;
//
//        public UserCommandLineRunner(final UserRepo repository) {
//            this.repository = repository;
//        }
//
//        @Override
//        public void run(final String... args) throws Exception {
////            Stream.of("Alen", "Mike").forEach(name -> repository.save(new User(name)));
////            repository.findAll().forEach(System.out::println);
//        }
//    }
