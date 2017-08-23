package com.preslavrachev.cryptotrader.persistence.repository

import com.preslavrachev.cryptotrader.mvc.model.TradeRecord
import org.springframework.data.mongodb.repository.MongoRepository

interface TradeRecordRepository: MongoRepository<TradeRecord, String>
