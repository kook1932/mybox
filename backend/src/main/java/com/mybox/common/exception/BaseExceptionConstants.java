package com.mybox.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class BaseExceptionConstants {

	@Getter @RequiredArgsConstructor
	public enum ExceptionClass {
		File("File"),
		Directory("Directory"),
		Validation("Validation");

		private final String exceptionClass;

		@Override
		public String toString() {
			return exceptionClass + " Exception.";
		}
	}
}
