package com.example.bankapp.util;

import com.example.bankapp.enums.AccountStatus;

public class AccountStatusEmailTemplate {

    public static String getSubject(AccountStatus status) {
        return switch (status) {
            case ACTIVE -> "Your Account Has Been Activated";
            case INACTIVE -> "Your Account Has Been Deactivated";
            case PENDING_KYC -> "KYC Verification Pending";
            case SUSPENDED -> "Account Suspended Notification";
            case REJECTED -> "Account Request Rejected";
		default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    public static String getBody(AccountStatus status, String name, String bankName, String supportEmail) {

        return switch (status) {

            case ACTIVE -> """
                Dear %s,

                Good news!

                Your account has been successfully activated. You can now log in and start using all our banking services without any restrictions.

                If you need any assistance, feel free to contact us at %s.

                Welcome aboard,
                %s Team
                """.formatted(name, supportEmail, bankName);

            case INACTIVE -> """
                Dear %s,

                This is to inform you that your account has been temporarily deactivated by the administrator.

                If you believe this action was taken in error or need further clarification, please contact our support team at %s.

                Regards,
                %s Team
                """.formatted(name, supportEmail, bankName);

            case PENDING_KYC -> """
                Dear %s,

                Your account is currently marked as Pending KYC Verification.

                To enable full access to your account, please complete the KYC process by logging into your dashboard and submitting the required documents.

                For any assistance, contact us at %s.

                Thank you for your cooperation,
                %s Team
                """.formatted(name, supportEmail, bankName);

            case SUSPENDED -> """
                Dear %s,

                We regret to inform you that your account has been suspended due to compliance or policy-related reasons.

                During this period, account operations will be restricted. For further details or clarification, please contact our support team at %s.

                Sincerely,
                %s Team
                """.formatted(name, supportEmail, bankName);

            case REJECTED -> """
                Dear %s,

                We regret to inform you that your account request has been rejected after review.

                If you require further clarification, please contact our support team at %s.

                Regards,
                %s Team
                """.formatted(name, supportEmail, bankName);
		default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }
}
