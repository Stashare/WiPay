package ke.co.stashare.wipay.registration;

/**
 * Created by Ken Wainaina on 18/04/2017.
 */

public class Config {

    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://innovase.co.ke/ProjectAlpha/include/request_smss.php";
    public static final String URL_CHECKBIZ = "http://innovase.co.ke/ProjectAlpha/include/check_biz.php";
    public static final String URL_VERIFY_OTP = "http://innovase.co.ke/ProjectAlpha/include/verify_otp.php";


    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "AFRICASTKNG";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}
