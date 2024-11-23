package com.trood.rledenev.profile.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "profiles")
class Profile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "interests", joinColumns = [JoinColumn(name = "profile_id")])
    @OrderColumn(name = "interest_order")
    var interests: List<Interest>? = emptyList()

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "links", joinColumns = [JoinColumn(name = "profile_id")])
    @OrderColumn(name = "link_order")
    var links: List<Link>? = emptyList()

    @Column(name = "name")
    var name: String? = null

    @Column(name = "surname")
    var surname: String? = null

    @Column(name = "job_title")
    var jobTitle: String? = null

    @Column(name = "phone")
    var phone: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "address")
    var address: String? = null

    @Column(name = "avatar_image_name")
    var avatarImageName: String? = null

    @Column(name = "is_public")
    var isPublic: Boolean? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Profile
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Profile(id=$id, interests=$interests, links=$links, name=$name, surname=$surname, jobTitle=$jobTitle, phone=$phone, address=$address, avatarImageName=$avatarImageName, isPublic=$isPublic)"
    }

}