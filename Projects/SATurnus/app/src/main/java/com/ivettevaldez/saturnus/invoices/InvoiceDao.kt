package com.ivettevaldez.saturnus.invoices

import android.util.Log
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.querySorted
import com.vicpin.krealmextensions.save
import io.realm.Sort
import io.realm.exceptions.RealmException
import javax.inject.Inject

open class InvoiceDao @Inject constructor() {

    private val classTag = this::class.java.simpleName
    private val baseClassTag = Invoice::class.java.simpleName

    companion object {

        private const val FOLIO = "folio"
        private const val ISSUING_RFC = "issuing.rfc"
        private const val CREATED_AT = "createdAt"
    }

    open fun save(invoice: Invoice): Boolean {
        return try {
            invoice.save()
            true
        } catch (ex: RealmException) {
            Log.e(
                classTag,
                "@@@@@ Attempting to save a $baseClassTag with $FOLIO '${invoice.folio}'",
                ex
            )
            false
        }
    }

    open fun findByFolio(folio: String): Invoice? {
        val invoice = Invoice().queryFirst { equalTo(FOLIO, folio) }
        if (invoice == null) {
            Log.e(classTag, "@@@@@ $baseClassTag with $FOLIO '$folio' does not exist")
        }
        return invoice
    }

    open fun findAllByIssuingRfc(rfc: String): List<Invoice> {
        return Invoice().querySorted(CREATED_AT, Sort.DESCENDING) {
            equalTo(ISSUING_RFC, rfc)
        }
    }

    fun delete(folio: String) {
        val invoice = findByFolio(folio)
        if (invoice != null) {
            Invoice().delete { equalTo(FOLIO, folio) }
        }
    }
}