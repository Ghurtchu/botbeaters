package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.domain.RandomNumber

trait RNGService {
  def gen: RandomNumber
}
