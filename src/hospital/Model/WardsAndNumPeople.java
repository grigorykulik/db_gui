package hospital.Model;

public class WardsAndNumPeople {
    private Integer wardId;
    private Integer numberOfPeople;

    public WardsAndNumPeople(Integer wardId, Integer numberOfPeople) {
        this.wardId = wardId;
        this.numberOfPeople = numberOfPeople;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
