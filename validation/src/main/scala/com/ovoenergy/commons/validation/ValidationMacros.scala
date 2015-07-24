package com.ovoenergy.commons.validation

import scala.reflect.macros.blackbox

private object ValidationMacros {
  def validate[X: c.WeakTypeTag](c: blackbox.Context)(value: c.Expr[X]) = {
    import c.universe._
      c.Expr[ValidationResult[X]] {
        q"""${c.prefix}.validate(${show(value.tree)}, $value)"""
      }
  }
}