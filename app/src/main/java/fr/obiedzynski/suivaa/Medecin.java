package fr.obiedzynski.suivaa;

public class Medecin {

    private Integer id;
    private String nom;
    private String prenom;
    private Integer id_cabinet;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getId_cabinet() {
        return id_cabinet;
    }

    public void setId_cabinet(Integer id_cabinet) {
        this.id_cabinet = id_cabinet;
    }

    public Medecin(Integer id, String nom, String prenom, Integer id_cabinet) {
        this.setId(id);
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setId_cabinet(id_cabinet);
    }
}
