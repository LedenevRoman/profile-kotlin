package com.trood.rledenev.profile.entity

import jakarta.persistence.Embeddable

@Embeddable
class Interest(var name: String) {

    override fun toString(): String {
        return "Interest(name=$name)"
    }
}
