package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.domain.RandomNumber

trait RandomNumberGenerator {
  def generate: RandomNumber
}
