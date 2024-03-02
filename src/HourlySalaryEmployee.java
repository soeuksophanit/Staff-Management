public class HourlySalaryEmployee extends StaffMembers{
    private int hourWorked;
    private double rate;

    public HourlySalaryEmployee(int id, String name, String address, int hourWorked, double rate) {
        super(id, name, address);
        this.hourWorked = hourWorked;
        this.rate = rate;
    }

    @Override
    public double pay() {
        return hourWorked*rate;
    }

    public int getHourWorked() {
        return hourWorked;
    }

    public void setHourWorked(int hourWorked) {
        this.hourWorked = hourWorked;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String toString() {
        return "HourlySalaryEmployee{" +
                "rate=" + rate +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
