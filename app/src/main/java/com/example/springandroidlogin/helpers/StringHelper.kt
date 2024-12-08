package com.example.springandroidlogin.helpers

/**
 * Utility class for common string validation tasks such as email validation
 * and password validation.
 */
class StringHelper {

    /**
     * Validates the format of an email address.
     *
     * @param email The email string to validate.
     * @return True if the email is valid, false otherwise.
     */
    fun regexEmailValidationPattern(email: String): Boolean {
        // Regular expression pattern for validating email addresses.
        // - Starts with alphanumeric characters or certain symbols: . _ % + -
        // - Contains a single '@' symbol.
        // - Followed by a domain name with letters, numbers, dots, or hyphens.
        // - Ends with a valid top-level domain of at least 2 characters.
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
        return email.matches(emailPattern.toRegex())
    }

    /**
     * Validates the strength of a password and returns a specific error message
     * if the password does not meet the criteria.
     *
     * @param password The password string to validate.
     * @return A string containing the error message if the password is invalid,
     *         or null if the password is valid.
     */
    fun validatePassword(password: String): String? {
        return when {
            // Check if the password is empty
            password.isEmpty() -> "Password is required."

            // Ensure at least one uppercase letter is present
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter."

            // Ensure at least one lowercase letter is present
            !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter."

            // Ensure at least one digit is present
            !password.any { it.isDigit() } -> "Password must contain at least one number."

            // Ensure at least one special character is present
            !password.any { "!@#$%^&*()-_=+{}[]|:;\"'<>,.?/`~".contains(it) } -> "Password must contain at least one special character."

            // Ensure the password length is at least 8 characters
            password.length < 8 -> "Password must be at least 8 characters long."

            // Ensure no spaces are included in the password
            password.contains(" ") -> "Password must not contain spaces."

            // If all conditions are met, the password is valid
            else -> null
        }
    }
}
