package com.example.bankapp.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum AccountType {
	SAVING("01","Saving"),
	CURRENT("02","Current"),
	STUDENT("03","Student"),
	SENIORCITIZEN("04","Senior"),
	SALARY("05","Salary");

	private final String code;
    private final String name;

    private static final Map<String, AccountType> codeMap = new HashMap<>();

    static {
        for (AccountType p : values()) {
        	codeMap.put(p.code, p);
        }
    }

    AccountType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }

    public static Optional<AccountType> fromCode(String code) {
        return Optional.ofNullable(codeMap.get(code));
    }

    public static String getNameByCode(String code) {
        return fromCode(code).map(AccountType::getName).orElse("Unknown Product");
    }
}
