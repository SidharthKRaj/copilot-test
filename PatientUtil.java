public class PatientUtil {
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private String bloodType;
    private int height;
    private int weight;
    private String insurance;

    public PatientUtil(String firstName, String lastName, String dob, String gender, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getBloodType() {
        return bloodType;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public void validateInsurance() throws Exception {
        if (insurance == null) {
            throw new Exception("Missing required input: insurance");
        }
        if (!insurance.matches("^[A-Z0-9]{10}$")) {
            throw new Exception("Invalid insurance: " + insurance);
        }
    }
}