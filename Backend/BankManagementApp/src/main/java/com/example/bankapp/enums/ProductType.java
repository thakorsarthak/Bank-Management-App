package com.example.bankapp.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ProductType {
	SAVING("01","Saving"),
	CURRENT("02","Current"),
	STUDENT("03","Saving"),
	SENIORCITIZEN("04","Current"),
	SALARY("05","Saving");

	private final String code;
    private final String name;

    private static final Map<String, ProductType> codeMap = new HashMap<>();

    static {
        for (ProductType p : values()) {
        	codeMap.put(p.code, p);
        }
    }

    ProductType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }

    public static Optional<ProductType> fromCode(String code) {
        return Optional.ofNullable(codeMap.get(code));
    }

    public static String getNameByCode(String code) {
        return fromCode(code).map(ProductType::getName).orElse("Unknown Product");
    }
}
