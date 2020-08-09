package Model;

import android.app.LauncherActivity;
import android.widget.ImageView;

public class RVListItem extends LauncherActivity.ListItem {
    private String type;
    private String companyName;
    private String email;
    private String checkInMain;

    public RVListItem() {

    }

    public RVListItem(String type, String companyName, String email,String checkInMain) {
        this.type = type;
        this.companyName = companyName;
        this.email = email;
        this.checkInMain=checkInMain;
    }

    public String getCheckInMain() {
        return checkInMain;
    }

    public void setCheckInMain(String checkInMain) {
        this.checkInMain = checkInMain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}