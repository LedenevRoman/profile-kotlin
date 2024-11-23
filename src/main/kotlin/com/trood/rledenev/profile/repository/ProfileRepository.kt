package com.trood.rledenev.profile.repository

import com.trood.rledenev.profile.entity.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileRepository : JpaRepository<Profile, UUID>
