package code.oswaldogh89.contactchooser.Objects;

/**
 * Created by oswaldogh89 on 16/09/15.
 */
public class Contact {
    private String phoneNumber;
    private boolean isSelected;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


}
