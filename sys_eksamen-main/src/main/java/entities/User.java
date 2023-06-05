package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "user_name", length = 40, nullable = false)
  private String userName;

  @Basic(optional = false)
  @Size(min = 1, max = 255)
  @Column(name = "user_password", nullable = false)
  private String userPass;

  @JoinTable(name = "user_animals", joinColumns = {
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
    @JoinColumn(name = "animal_name", referencedColumnName = "animal_name")})
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Animal> animalList = new ArrayList<>();

  @JoinTable(name = "user_facts", joinColumns = {
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
    @JoinColumn(name = "fact_id", referencedColumnName = "id")})
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Fact> factList = new ArrayList<>();


  @JoinTable(name = "user_roles", joinColumns = {
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  @ManyToMany
  private List<Role> roleList = new ArrayList<>();

  @JoinTable
  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList<>();
    roleList.forEach((role) -> {
        rolesAsStrings.add(role.getRoleName());
      });
    return rolesAsStrings;
  }

  public User() {}

  //TODO Change when password is hashed
   public boolean verifyPassword(String pw){
    return BCrypt.checkpw(pw, userPass);
    }

  public User(String userName, String userPass) {
    this.userName = userName;
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPass() {
    return this.userPass;
  }

  public void setUserPass(String userPass) {
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());;
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }

  public List<Animal> getAnimalList() {
    return animalList;
  }

  public List<Fact> getFactList() {
    return factList;
  }

  public void setFactList(List<Fact> factList) {
    this.factList = factList;
  }

  public void setAnimalList(List<Animal> animalList) {
    this.animalList = animalList;
  }
}