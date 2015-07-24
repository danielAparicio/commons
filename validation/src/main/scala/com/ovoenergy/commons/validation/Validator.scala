package com.ovoenergy.commons.validation

import scala.language.experimental.macros

trait Validator[X] {
  def validate(id: String, value: X): ValidationResult[X]

  def validate(value: X) : ValidationResult[X] = macro ValidationMacros.validate[X]
}