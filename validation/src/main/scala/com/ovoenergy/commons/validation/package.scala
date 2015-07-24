package com.ovoenergy.commons

import scalaz.ValidationNel

package object validation {
  type ValidationResult[X] = ValidationNel[ErrorDescription, X]
}
