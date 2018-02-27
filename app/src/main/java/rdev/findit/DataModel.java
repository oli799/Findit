package rdev.findit;

/**
 * Created by Oliver on 2018. 02. 17..
 */

public class DataModel {

    private String Id;
    private String Country;
    private String City;
    private String Desc;
    private String Contact;
    private String Name;
    private int Kep;


    public int getKep() {
        return Kep;
    }

    public void setKep(int kep) {
        Kep = kep;
    }




    public DataModel() {
    }

    public DataModel(int kep, String desc, String contact, String name){
        Kep = kep;
        Desc = desc;
        Contact = contact;
        Name = name;
    }

    public DataModel(String id, String country, String city, String desc, String contact,String name) {
        Id = id;
        Country = country;
        City = city;
        Desc = desc;
        Contact = contact;
        Name = name;

    }



    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDesc() {
        return Desc;
    }
    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}


