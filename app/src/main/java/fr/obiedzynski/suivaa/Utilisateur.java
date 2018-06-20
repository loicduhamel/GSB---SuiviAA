package fr.obiedzynski.suivaa;

public class Utilisateur {

    private Integer id;
    private String nom;
    private String prenom;
    private String login;
    private String password;
    private Integer id_medecin;
    private Integer id_role;

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId_medecin() {
        return id_medecin;
    }

    public Integer getId_role() {
        return id_role;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId_medecin(Integer id_medecin) {
        this.id_medecin = id_medecin;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
    }

    public Utilisateur( Integer id, String nom, String prenom, String login, String password, Integer id_medecin, Integer id_role ){
        this.setId(id);
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setLogin(login);
        this.setPassword(password);
        this.setId_medecin(id_medecin);
        this.setId_role(id_role);
    }
}
