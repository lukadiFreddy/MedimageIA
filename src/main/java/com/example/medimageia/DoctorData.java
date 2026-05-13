package com.example.medimageia;

public class DoctorData {

    private Integer id;
    private String nom;
    private String email;
    private String password;
    private String sexe;
    private String image;
    private String specialisation;
    private String status;

    public DoctorData(Integer id,
                      String nom,
                      String email,
                      String password,
                      String sexe,
                      String image,
                      String specialisation,
                      String status) {

        this.id = id;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.sexe = sexe;
        this.image = image;
        this.specialisation = specialisation;
        this.status = status;

    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSexe() {
        return sexe;
    }

    public String getImage() {
        return image;
    }

    public String getspecialisation() {
        return specialisation;
    }

    public String getstatus() {
        return status;
    }
}