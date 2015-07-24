package com.ovoenergy.commons.validation

import org.joda.time.{DateTime, Years, Instant}
import org.joda.time.format.DateTimeFormat

import scala.util.matching.Regex
import scalaz.Scalaz._

trait StringValidators {
  type StringValidator = Validator[String]

  val notEmptyMessage = "Must be non empty"

  val numericMessage = "Must be a number"

  val emailMessage = "Must be a valid email address"

  val ukPhoneNumberMessage = "Must be a valid UK phone number"

  val dateMessage = "Must be a valid date"

  val ageMessage = "Must be at least 18"

  val accountNumberMessage = "Must be 8 digits"

  val sortCodeMessage = "Must be 6 digits"

  val notEmptyValidator = new StringValidator {
    def validate(id: String, string: String): ValidationResult[String] = string match {
      case value if value.isEmpty => ErrorDescription(id, notEmptyMessage).failNel
      case _ => string.successNel
    }
  }

  def regexValidator(regex: Regex, errorMessage: String) = new StringValidator {
    def validate(id: String, string: String): ValidationResult[String] = string match {
      case regex(_*) => string.successNel
      case _ => ErrorDescription(id, errorMessage).failNel
    }
  }

  val numericValidator = regexValidator( """^([0-9]+)$""".r, numericMessage)

  val emailValidator = regexValidator( """^([^@\.]+?(\.[^@\.]+?)*?@[^@`.]+?(\.[^@\.]+?)+?)$""".r, emailMessage)

  val ukPhoneNumberValidator = regexValidator( """^(\(?44\)? ?|\(?\+\(?44\)? ?|\(?0)(\d\)? ?){9,10}$""".r, ukPhoneNumberMessage)

  private def parseDateTime(format: String, id: String, string: String): ValidationResult[DateTime] = {
    val dateTimeFormat = DateTimeFormat.forPattern(format)
    try {
      dateTimeFormat.parseDateTime(string).success
    } catch {
      case e: IllegalArgumentException => ErrorDescription(id, s"$dateMessage [$format]").failNel
    }
  }

  def dateValidator(format: String) = new StringValidator {
    def validate(id: String, string: String): ValidationResult[String] =
      parseDateTime(format, id, string).map(_ => string)
  }

  def dateOfBirthValidator(format: String) = new StringValidator {
    def validate(id: String, string: String) : ValidationResult[String] =
      parseDateTime(format, id, string).flatMap {date =>
        if (Years.yearsBetween(date, new Instant()).getYears >= 18)
          string.successNel
        else
          ErrorDescription(id, ageMessage).failNel
      }
  }

  val accountNumberValidator = regexValidator("""^(\d{8})$""".r, accountNumberMessage)

  val sortCodeValidator = regexValidator("""^(\d{6})$""".r, sortCodeMessage)
}