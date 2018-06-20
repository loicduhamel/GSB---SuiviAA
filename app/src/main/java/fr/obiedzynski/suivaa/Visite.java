package fr.obiedzynski.suivaa;


public class Visite {
    private Integer rdv;
    private String hArrivee;
    private String hDebut;
    private String hDepart;
    private String dVisite;
    private Integer id_medecin;
    private Integer id;

    public Visite(Integer rdv, String hArrivee, String hDebut, String hDepart, String dVisite, Integer id_medecin, Integer id) {
        this.rdv = rdv;
        this.hArrivee = hArrivee;
        this.hDebut = hDebut;
        this.hDepart = hDepart;
        this.dVisite = dVisite;
        this.id_medecin = id_medecin;
        this.id = id;
    }

    public Integer getRdv() {
        return rdv;
    }

    public void setRdv(Integer rdv) {
        this.rdv = rdv;
    }

    public String gethArrivee() {
        return hArrivee;
    }

    public void sethArrivee(String hArrivee) {
        this.hArrivee = hArrivee;
    }

    public String gethDebut() {
        return hDebut;
    }

    public void sethDebut(String hDebut) {
        this.hDebut = hDebut;
    }

    public String gethDepart() {
        return hDepart;
    }

    public void sethDepart(String hDepart) {
        this.hDepart = hDepart;
    }

    public String getdVisite() {
        return dVisite;
    }

    public void setdVisite(String dVisite) {
        this.dVisite = dVisite;
    }

    public Integer getId_medecin() {
        return id_medecin;
    }

    public void setId_medecin(Integer id_medecin) {
        this.id_medecin = id_medecin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
