package com.ovoenergy.commons.validation

import org.specs2.mutable.Specification
import org.specs2.scalaz.ValidationMatchers

import scalaz._
import Scalaz._

class ValidatorSpec extends Specification with ValidationMatchers {
  val alwaysSuccess = new Validator[String] {
    def validate(id: String, value: String) = "success message".success
  }

  val alwaysFailing = new Validator[Int] {
    def validate(id: String, value: Int) = ErrorDescription(id, "failure message").failNel
  }

  "Validators" should {
    "succeed for a successful validation" in {
      val field = "hello"
      alwaysSuccess.validate(field) must beSuccessful("success message")
    }

    "fail with the actual value for a constant failing validation" in {
      alwaysFailing.validate(2) must beFailing(NonEmptyList(ErrorDescription("2", "failure message")))
    }

    "fail with the id for a field failing validation" in {
      val field = 1
      alwaysFailing.validate(field) must beFailing(NonEmptyList(ErrorDescription("field", "failure message")))
    }

    "fail with the id for a complex field failing validation" in {
      object Object {
        val innerField = 20
      }
      alwaysFailing.validate(Object.innerField) must beFailing(NonEmptyList(ErrorDescription("Object.innerField", "failure message")))
    }
  }
}