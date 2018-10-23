package com.example.demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.*;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class DemoApplication {
    private String adminPass = "d6c8306c-4b28-448e-b523-c8d068aa8b64";

	@GetMapping("/")
	String home() {
		return "Spring is here!";
	}
	@GetMapping("/admin")
    String admin(){return "<h1>Admin</h1>";}
    @RequestMapping(method = RequestMethod.POST,value="/login" )
    String login(@RequestBody String a) throws IOException {
        System.out.println(a);
        String resEmail ="";
        String resPass="";
        try {
            JSONObject b= new JSONObject(a);
             resEmail= b.getString("email");
             resPass= b.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!resEmail.isEmpty() && resEmail.toLowerCase().equals("admin@netcracker.com")){
            System.out.println("YES1");
            if (!resPass.isEmpty() && resPass.equals(adminPass)){
                System.out.println("____________CORRECT____________");
                String url = "http://localhost:8080/admin";

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");

            }
        }else{
            System.out.println(a);

        }
        return a;
    }
    @RequestMapping(method = RequestMethod.POST,value="/register" )
    String register(@RequestBody String a){
        System.out.println(a);
        return a;
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

@RestController
@Entity
@Data
class User {

	@Id
	@GeneratedValue
	private long id;


	private String email;
	private String firstName;
	private String lastName;


    public User( final String email, final String firstName, final String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User( final String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}

//@Service
class UserCommandLineRunner implements CommandLineRunner{
        private final UserRepo repository;

        public UserCommandLineRunner(final UserRepo repository) {
            this.repository = repository;
        }

        @Override
        public void run(final String... args) throws Exception {
            Stream.of("Alen", "Mike", "fdsafdsafds", "fdsafdsa").forEach(name -> repository.save(new User(name)));
            repository.findAll().forEach(System.out::println);
        }
        public List<User> getAllUsers(){
            List<User> l = new ArrayList<>();
            repository.findAll().forEach(l::add);
            return l;
        }
    }



interface UserRepo extends JpaRepository<User, Long> {
    List<User> findByEmail(String emailAddress);

}