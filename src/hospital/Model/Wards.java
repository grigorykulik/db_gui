package hospital.Model;

public class Wards {
    private Integer id;
    private String name;
    private Integer maxCount;

    public Wards(Integer id, String name, Integer maxCount) {
        this.id = id;
        this.name = name;
        this.maxCount=maxCount;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
