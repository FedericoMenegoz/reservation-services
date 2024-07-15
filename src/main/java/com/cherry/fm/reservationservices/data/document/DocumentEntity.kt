package com.cherry.fm.reservationservices.data.document

import com.cherry.fm.reservationservices.DocumentType
import com.cherry.fm.reservationservices.ExpirationDate

data class DocumentEntity(
	val id: Long,
	val number: String,
	val type: DocumentType,
	val expiration: ExpirationDate,
)
