package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.domain.RandomNumber

trait RNG {
  def gen: RandomNumber
}
