package control.ajax;

import com.opensymphony.xwork2.ActionSupport;

public class Redirect {

    private String url;
    public String redirect;

    public void execute() throws Exception {

        redirect = "www.ikariam.es";
        
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
