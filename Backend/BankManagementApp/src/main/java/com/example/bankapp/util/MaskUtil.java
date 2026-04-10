package com.example.bankapp.util;

public class MaskUtil {
	
	public static String maskAccountNumber(String acc) {
        if (acc == null || acc.length() < 4) return acc;
        return "XXXXXX" + acc.substring(acc.length() - 4);
    }

    public static String maskPan(String pan) {
        if (pan == null || pan.length() < 4) return pan;
        return pan.substring(0, 2) + "XXXXX" + pan.substring(pan.length() - 2);
    }

    public static String maskAadhaar(String aadhaar) {
        if (aadhaar == null || aadhaar.length() < 4) return aadhaar;
        return "XXXX-XXXX-" + aadhaar.substring(aadhaar.length() - 4);
    }
    
    public static String maskContact(String Contact) {
        if (Contact == null || Contact.length() < 4) return Contact;
        return "XXXX-XXXX-" + Contact.substring(Contact.length() - 4);
    }
}
