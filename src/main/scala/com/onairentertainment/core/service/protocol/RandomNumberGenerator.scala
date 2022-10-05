package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.model.RandomNumber

trait RandomNumberGenerator {
  def generate: RandomNumber
}
