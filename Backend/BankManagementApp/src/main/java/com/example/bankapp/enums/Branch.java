package com.example.bankapp.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Branch {
	AHMEDADAB("1001", "Ahmedabad"), SURAT("1002", "Surat"), MUMBAI("1003", "Mumbai");

	private final String code;
	private final String name;

	private static final Map<String, Branch> codeMap = new HashMap<>();

	static {

		for (Branch branch : values()) {
			codeMap.put(branch.code, branch);
		}
	}

	Branch(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static Optional<Branch> fromCode(String code) {
		return Optional.ofNullable(codeMap.get(code));
	}

	public static String getNameByCode(String code) {
		return fromCode(code).map(Branch::getName).orElse("Unknown Branch");
	}

}
