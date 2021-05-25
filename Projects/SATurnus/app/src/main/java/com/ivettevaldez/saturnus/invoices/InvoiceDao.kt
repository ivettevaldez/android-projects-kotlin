package com.ivettevaldez.saturnus.invoices

import android.util.Log
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.querySorted
import com.vicpin.krealmextensions.save
import io.realm.Sort
import io.realm.exceptions.RealmException
import javax.inject.Inject

class InvoiceDao @Inject constructor() {

    private val classTag = this::class.java.simpleName
    private val baseClassTag = Invoice::class.java.simpleName

    fun save(invoice: Invoice): Boolean {
        return try {
            invoice.save()
            true
        } catch (ex: RealmException) {
            Log.e(
                classTag,
                "@@@@@ Attempting to save a $baseClassTag with Folio ${invoice.folio}",
                ex
            )
            false
        }
    }

    fun findByFolio(folio: String): Invoice? {
        val invoice = Invoice().queryFirst {
            notEqualTo("deleted", true)
                .and()
            equalTo("folio", folio)
        }
        if (invoice == null) {
            Log.e(classTag, "@@@@@ $baseClassTag with Folio $folio does not exist")
        }
        return invoice
    }

    fun findAllByRfc(rfc: String): List<Invoice> {
        return Invoice().querySorted("createdAt", Sort.DESCENDING) {
            notEqualTo("deleted", true)
                .and()
            equalTo("rfc", rfc)
        }
    }

    fun delete(folio: String) {
        val invoice = findByFolio(folio)
        if (invoice != null) {
            Invoice().delete {
                equalTo("folio", folio)
            }
        }
    }
}