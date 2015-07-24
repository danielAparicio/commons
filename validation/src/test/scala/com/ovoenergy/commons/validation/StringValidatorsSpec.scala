package com.ovoenergy.commons.validation

import org.specs2.mutable.Specification
import org.specs2.scalaz.ValidationMatchers

import scalaz._

class StringValidatorsSpec extends Specification with StringValidators with ValidationMatchers {
  "Not empty validator" should {
    "validate a non-empty string" in {
      val name = "John"
      notEmptyValidator.validate(name) must beSuccessful(name)
    }

    "fail for an empty string" in {
      val name = ""
      notEmptyValidator.validate(name) must beFailing(NonEmptyList(ErrorDescription("name", notEmptyMessage)))
    }
  }

  "Numeric validator" should {
    "validate a numeric string" in {
      val number = "15349871876"
      numericValidator.validate(number) must beSuccessful(number)
    }

    "fail for a non-numeric string" in {
      val number = "XS6893"
      numericValidator.validate(number) must beFailing(NonEmptyList(ErrorDescription("number", numericMessage)))
    }
  }

  "Email validator" should {
    "validate a simple email address" in {
      val email = "niceandsimple@example.com"
      emailValidator.validate(email) must beSuccessful(email)
    }

    "validate a lengthy email address" in {
      val email = "a.little.lengthy.but.fine@dept.example.com"
      emailValidator.validate(email) must beSuccessful(email)
    }

    "validate an email address with a dash" in {
      val email = "other.email-with-dash@example.com"
      emailValidator.validate(email) must beSuccessful(email)
    }

    // Technically a legal address, but we don't want to allow these
    "fail for an email address with local domain name" in {
      val email = "admin@mailserver1"
      emailValidator.validate(email) must beFailing(NonEmptyList(ErrorDescription("email", emailMessage)))
    }

    "fail for an email address with no @ sign" in {
      val email = "john.smith.com"
      emailValidator.validate(email) must beFailing(NonEmptyList(ErrorDescription("email", emailMessage)))
    }

    "fail for an email address with multiple @ signs" in {
      val email = "A@b@c@example.com"
      emailValidator.validate(email) must beFailing(NonEmptyList(ErrorDescription("email", emailMessage)))
    }

    "fail for an email address with a double dot before @ sign" in {
      val email = "john..smith@example.com"
      emailValidator.validate(email) must beFailing(NonEmptyList(ErrorDescription("email", emailMessage)))
    }

    "fail for an email address with a double dot after @ sign" in {
      val email = "john.smith@example..com"
      emailValidator.validate(email) must beFailing(NonEmptyList(ErrorDescription("email", emailMessage)))
    }

    "fail for an email address starting with @ sign" in {
      val email = "@example.com"
      emailValidator.validate(email) must beFailing(NonEmptyList(ErrorDescription("email", emailMessage)))
    }
  }

  "UK phone number validator" should {
    "validate a 10 digit phone number" in {
      val phone = "016977 1234"
      ukPhoneNumberValidator.validate(phone) must beSuccessful(phone)
    }

    "validate an 11 digit phone number" in {
      val phone = "055 1234 5679"
      ukPhoneNumberValidator.validate(phone) must beSuccessful(phone)
    }

    "validate a phone number beginning with +44" in {
      val phone = "+44 7798 456 789"
      ukPhoneNumberValidator.validate(phone) must beSuccessful(phone)
    }

    "validate a phone number beginning with (+44)" in {
      val phone = "(+44) 7798 456 789"
      ukPhoneNumberValidator.validate(phone) must beSuccessful(phone)
    }

    "fail for a blank phone number" in {
      val phone = "     "
      ukPhoneNumberValidator.validate(phone) must beFailing(NonEmptyList(ErrorDescription("phone", ukPhoneNumberMessage)))
    }

    "fail for a phone number containing illegal chars" in {
      val phone = "0121 987 45&!"
      ukPhoneNumberValidator.validate(phone) must beFailing(NonEmptyList(ErrorDescription("phone", ukPhoneNumberMessage)))
    }

    "fail for a phone number which starts with 1" in {
      val phone = "12345678901"
      ukPhoneNumberValidator.validate(phone) must beFailing(NonEmptyList(ErrorDescription("phone", ukPhoneNumberMessage)))
    }

    "fail for a phone number with non-UK country code" in {
      val phone = "+337876013546"
      ukPhoneNumberValidator.validate(phone) must beFailing(NonEmptyList(ErrorDescription("phone", ukPhoneNumberMessage)))
    }

    "fail for a phone number with both +44 and 0" in {
      val phone = "+4401234567890"
      ukPhoneNumberValidator.validate(phone) must beFailing(NonEmptyList(ErrorDescription("phone", ukPhoneNumberMessage)))
    }

    // Technically a legal phone number, but not a residential one
    "fail for an 8 digit phone number" in {
      val phone = "0800 1111"
      ukPhoneNumberValidator.validate(phone) must beFailing(NonEmptyList(ErrorDescription("phone", ukPhoneNumberMessage)))
    }
  }

  "Date validator" should {
    "validate a dd/MM/yyyy date" in {
      val date = "12/01/1995"
      dateValidator("dd/MM/yyyy").validate(date) must beSuccessful(date)
    }

    "validate a long format date" in {
      val date = "Wed, Jul 4, 01"
      dateValidator("EEE, MMM d, yy").validate(date) must beSuccessful(date)
    }

    "fail for an illegal date" in {
      val date = "30/02/2001"
      dateValidator("dd/MM/yyyy").validate(date) must beFailing(NonEmptyList(ErrorDescription("date", s"$dateMessage [dd/MM/yyyy]")))
    }

    "throw an exception for an illegal date format" in {
      val date = "12/01/1995"
      dateValidator("ASDFGHJK").validate(date) must throwAn[IllegalArgumentException]
    }
  }

  "Date of birth validator" should {
    "validate a legal date of birth" in {
      val dob = "01/03/1984"
      dateOfBirthValidator("dd/MM/yyyy").validate(dob) must beSuccessful(dob)
    }

    "fail for a date of birth younger than 18" in {
      val dob = "01/03/2010"
      dateOfBirthValidator("dd/MM/yyyy").validate(dob) must beFailing(NonEmptyList(ErrorDescription("dob", ageMessage)))
    }

    "fail for an illegal date of birth" in {
      val dob = "30/02/2001"
      dateOfBirthValidator("dd/MM/yyyy").validate(dob) must beFailing(NonEmptyList(ErrorDescription("dob", s"$dateMessage [dd/MM/yyyy]")))
    }
  }

  "Account number validator" should {
    "validate an 8 digit account number" in {
      val account = "12345678"
      accountNumberValidator.validate(account) must beSuccessful(account)
    }

    "fail for a too short account number" in {
      val account = "123456"
      accountNumberValidator.validate(account) must beFailing(NonEmptyList(ErrorDescription("account", accountNumberMessage)))
    }

    "fail for a too long account number" in {
      val account = "11111111111"
      accountNumberValidator.validate(account) must beFailing(NonEmptyList(ErrorDescription("account", accountNumberMessage)))
    }

    "fail for non numeric chars in account number" in {
      val account = "1234-5678"
      accountNumberValidator.validate(account) must beFailing(NonEmptyList(ErrorDescription("account", accountNumberMessage)))
    }
  }

  "Sort code validator" should {
    "validate a 6 digit sort code" in {
      val sortCode = "010203"
      sortCodeValidator.validate(sortCode) must beSuccessful(sortCode)
    }

    "fail for a too short sort code" in {
      val sortCode = "00012"
      sortCodeValidator.validate(sortCode) must beFailing(NonEmptyList(ErrorDescription("sortCode", sortCodeMessage)))
    }

    "fail for a too long sort code" in {
      val sortCode = "1111111"
      sortCodeValidator.validate(sortCode) must beFailing(NonEmptyList(ErrorDescription("sortCode", sortCodeMessage)))
    }

    "fail for non numeric chars in sort code" in {
      val sortCode = "11-12-13"
      sortCodeValidator.validate(sortCode) must beFailing(NonEmptyList(ErrorDescription("sortCode", sortCodeMessage)))
    }
  }
}