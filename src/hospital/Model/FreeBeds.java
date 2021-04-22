package hospital.Model;

public class FreeBeds {
    private Integer id;
    private Integer numFreeBeds;

    public FreeBeds(Integer id, Integer numFreeBeds) {
        this.id = id;
        this.numFreeBeds = numFreeBeds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumFreeBeds() {
        return numFreeBeds;
    }

    public void setNumFreeBeds(Integer numFreeBeds) {
        this.numFreeBeds = numFreeBeds;
    }
}
