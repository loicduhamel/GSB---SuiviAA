package fr.obiedzynski.suivaa;

import android.location.Location;

public class Cabinet {

    private Integer id;
    private String adresse;
    private String code_postal;
    private String ville;
    private String longitude;
    private String latitude;
    private Location location;

    public Cabinet(Integer id, String adresse, String code_postal, String ville, String longitude, String latitude) {
        this.id = (id);
        this.setAdresse(adresse);
        this.setCode_postal(code_postal);
        this.setVille(ville);
        this.setLongitude (longitude);
        this.setLatitude(latitude);

        location = new Location("");
        location.setLongitude(Double.parseDouble(longitude));
        location.setLongitude(Double.parseDouble(latitude));
    }

    public Cabinet() {
    }

    public Location getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
