//package util;
//
//import com.opensymphony.xwork2.ActionContext;
//import java.io.IOException;
//import java.util.Properties;
//import javax.servlet.jsp.JspException;
//import javax.servlet.jsp.tagext.TagSupport;
//
//public class Lang extends TagSupport {
//
//    private String key;
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    public int doStartTag() throws JspException {
//        try {
//            Properties properties = (Properties) ActionContext.getContext().getSession().get("lang");
//            String literal = properties.getProperty(key);
//            if (literal == null) {
//                pageContext.getOut().print(key);
//            } else {
//                pageContext.getOut().print(literal);
//            }
//
//        } catch (IOException e) {
//            throw new JspException("Error: IOException" + e.getMessage());
//        }
//        return SKIP_BODY;
//    }
//}
