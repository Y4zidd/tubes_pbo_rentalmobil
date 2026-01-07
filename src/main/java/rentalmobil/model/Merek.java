package rentalmobil.model;

public class Merek {

    private int idMerek;
    private String namaMerek;

    public Merek(int idMerek, String namaMerek) {
        this.idMerek = idMerek;
        this.namaMerek = namaMerek;
    }

    public int getIdMerek() {
        return idMerek;
    }

    public void setIdMerek(int idMerek) {
        this.idMerek = idMerek;
    }

    public String getNamaMerek() {
        return namaMerek;
    }

    public void setNamaMerek(String namaMerek) {
        this.namaMerek = namaMerek;
    }

    @Override
    public String toString() {
        return namaMerek;
    }
}

